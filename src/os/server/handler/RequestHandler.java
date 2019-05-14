package os.server.handler;

import java.lang.reflect.Method;

import os.server.type.Request;

public class RequestHandler {

	private Controller controller;
	private Request request;
	private Method method;
	
	public RequestHandler(Controller controller, Request request, Method method) {
		
		this.controller = controller;
		this.request = request;
		this.method = method;
	}
	
	public void call(Object... args) throws Exception {
		
		method.setAccessible(true);
		method.invoke(controller.getInstance(), args);
	}

	public Controller getController() {
		
		return controller;
	}

	public void setController(Controller controller) {
		
		this.controller = controller;
	}

	public Request getRequest() {
		
		return request;
	}

	public void setRequest(Request request) {
		
		this.request = request;
	}

	public Method getMethod() {
		
		return method;
	}

	public void setMethod(Method method) {
		
		this.method = method;
	}
}