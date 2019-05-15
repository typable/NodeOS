package os.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import os.server.HttpServer;
import os.server.handler.Controller;
import os.type.Logger;
import os.type.Logger.Messages;
import os.util.File;
import os.util.Property;

public class Core {

	public static Property<String> PROPERTIES;
	public static Property<String> LANGUAGES;
	public static List<Controller> CONTROLLERS;
	public static Logger LOGGER;
	
	public static final String CONFIG_PATH = "/config.properties";
	public static final String RESOURCE_PATH = "/src";
	
	public static int PORT;
	public static String ROOT;
	
	private List<Class<?>> instances;
	private HttpServer server;
	
	public Core() {
		
		try {
			
			PROPERTIES = new Property<String>();
			LANGUAGES = new Property<String>();
			CONTROLLERS = new ArrayList<Controller>();
			LOGGER = new Logger();
			
			instances = new ArrayList<Class<?>>();
			server = new HttpServer();
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void launch() throws Exception {
		
		loadControllers();
		loadConfigurations();
		
		PORT = Integer.parseInt(getRequired("port"));
		ROOT = Core.getRequired("root").replaceAll("\\*", getCurrentPath());
		
		loadLanguages();
		
		server.launch();
	}
	
	public void addController(Class<?> instance) {
		
		instances.add(instance);
	}
	
	private void loadControllers() throws Exception {
		
		for(Class<?> instance : instances) {
			
			HashMap<String, Object> fields = new HashMap<String, Object>();
			fields.put("logger", LOGGER);
			
			Controller controller = new Controller(instance);
			controller.inject(fields);
			
			CONTROLLERS.add(controller);
		}
	}
	
	private void loadConfigurations() throws IOException {
		
		File configFile = new File(getCurrentPath() + CONFIG_PATH);
		
		if(configFile.exists()) {
			
			try {
				
				configFile.load();
				
				PROPERTIES = configFile.getProps();
				
				LOGGER.info("Configurations loaded");
			}
			catch(IOException e) {
				
				LOGGER.error("Failed to load Configurations!", e);
			}
		}
		else {
			
			try {
				
				configFile.create();
				
				if(configFile.exists()) {
					
					LOGGER.info("Configuration file created");
					
					Property<String> props = new Property<String>();
					props.set("port", "80");
					props.set("root", "*/public");
					
					configFile.setProps(props);
					configFile.save();
					
					loadConfigurations();
				}
				else {
					
					LOGGER.error("Failed to create new file!");
				}
			}
			catch(IOException e) {
				
				LOGGER.error("Failed to create new file!", e);
			}
		}
	}
	
	private void loadLanguages() throws IOException {
		
		File file = new File(ROOT + RESOURCE_PATH + "/lang/lang.properties");
		
		if(file.exists()) {
			
			file.load();
			LANGUAGES = file.getProps();
		}
	}
	
	public static String getCurrentPath() throws IOException {
		
		return new java.io.File(".").getCanonicalPath().replaceAll("\\\\", "\\/");
	}
	
	public static String get(String key) {
		
		return PROPERTIES.hasKey(key) ? PROPERTIES.get(key) : null;
	}
	
	public static String getRequired(String key) {
		
		if(get(key) == null) {
			
			LOGGER.error(Messages.UNDEFINED.getMessage(key));
			stop();
		}
		
		return get(key);
	}
	
	public static void stop() {
		
		LOGGER.info(Messages.SERVER_STOPPED.getMessage());
		System.exit(0);
	}
}
