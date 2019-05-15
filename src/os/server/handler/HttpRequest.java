package os.server.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import os.server.HttpServer;
import os.type.Cookie;
import os.type.Header;
import os.type.RequestMethod;
import os.util.Formatter;
import os.util.Utils;

public class HttpRequest extends Handler {
	
	public HttpRequest(HttpServer server, Socket socket) throws IOException {

		super.setSocket(socket);
	}
	
	public void request() throws IOException {
		
		String line;
		
		while(Utils.notEmpty(line = getIn().readLine())) {
			
			if(line.contains("HTTP")) {
				
				String method = line.split(" ")[0];
				
				setMethod(RequestMethod.valueOf(method));
				
				String query = line.split(" ")[1];
				
				if(query.contains("?")) {
					
					setUrl(query.split("\\?")[0]);
					
					query = query.split("\\?")[1];
					
					if(query.contains("&")) {
						
						for(String bodyParts : query.split("&")) {
							
							Utils.keySet(getParameter(), "=", bodyParts);
						}
					}
					else {
						
						Utils.keySet(getParameter(), "=", query);
					}
				}
				else {
					
					setUrl(query);
				}
			}
			else {
				
				Utils.keySet(getHeader(), ": ", line);
			}
		}
		
		if(getMethod() == RequestMethod.POST) {
			
			int contentLength = getContentLength();
			
			if(contentLength > 0) {
				
				char[] buffer = new char[contentLength];
				
				getIn().read(buffer, 0, contentLength);
				
				String body = String.valueOf(buffer);

				body = Formatter.parseURL(body);
				
				if(body.contains("&")) {
					
					for(String bodyParts : body.split("&")) {
						
						Utils.keySet(getParameter(), "=", bodyParts);
					}
				}
				else {
					
					Utils.keySet(getParameter(), "=", body);
				}
				
				setBody(body);
			}
		}
		
		String lang = getParameter().get("lang");
		
		if(lang != null && (lang.equals("en") || lang.equals("de"))) {
			
			setLanguage(lang);
		}
		else {
			
			setLanguage("en");
		}
	}
	
	public int getContentLength() {
		
		if(getHeader().hasKey(Header.CONTENT_LENGTH)) {
			
			try {
				
				return Integer.parseInt(getHeader().get(Header.CONTENT_LENGTH));
			}
			catch(NumberFormatException ex) {
				
				return 0;
			}
		}
		
		return 0;
	}
	
	public Cookie[] getCookies() {
		
		Cookie[] cookies;
		HashMap<String, String> cookiesMap = new HashMap<String, String>();
		
		if(getHeader().hasKey(Header.COOKIE)) {
			
			String line = getHeader().get(Header.COOKIE);
			
			if(line.contains("; ")) {
				
				for(String lineParts : line.split("; ")) {
					
					Utils.keySet(cookiesMap, "=", lineParts);
				}
			}
			else {
				
				Utils.keySet(cookiesMap, "=", line);
			}
		}
		
		cookies = new Cookie[cookiesMap.size()];
		
		int i = 0;
		
		for(String key : cookiesMap.keySet()) {
			
			cookies[i] = new Cookie(key, cookiesMap.get(key));
			
			i++;
		}
		
		return cookies;
	}
	
	public Cookie getCookie(String key) {
		
		for(Cookie cookie : getCookies()) {
			
			if(cookie.getKey().equals(key)) {
				
				return cookie;
			}
		}
		
		return null;
	}
}
