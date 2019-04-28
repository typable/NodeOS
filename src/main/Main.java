package main;

import os.server.Server;
import os.server.handler.Handler;
import os.server.note.Request;
import os.server.type.ContentType;
import os.server.type.RequestMethod;
import os.util.Utils;

public class Main extends Server {

	private final String COOKIE_NAME = "uuid";
	
	private final String NAV_TEMPLATE = "/src/template/nav.html";
	
	public Main() {
		
		launch(80);
	}

	public static void main(String[] args) {
		
		new Main();
	}
	
	@Request(url = "/")
	public void getRoot(Handler request, Handler response) {
		
		String cookie = request.getHeader().getCookies().get(COOKIE_NAME);
		String code = "";
		
		if(Utils.notEmpty(cookie)) {
			
			// TODO
			
			String userProfile = "https://lh3.googleusercontent.com/-a8ulId-i9cY/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rcieSoPf-8Y_6Sch5sY7H7PTUttJw/s96-c-mo/photo.jpg";
			
			code = "<div class='icon m-1 tooltip'><div class='material-icons'>notifications_none</div><div class='tooltip-text tooltip-icon'>Notifications</div></div>"
					+ "<div class='account m-1 tooltip'><div class='image image-round'><img src='" + userProfile + "'></div><div class='tooltip-text tooltip-icon' style='transform: translateX(calc(-50% + 22px));'>Account</div></div>" +
					"<form action='/logout' method='post'><button class='signin m-1'>Log Out</button></form>";
		}
		else {
			
			code = "<a href='/login' class='text-none'><div class='signin m-1'>Log In</div></a>";
		}
		
		response.addTemplate("nav", NAV_TEMPLATE);
		response.addAttribute("login", code);
		response.showPage("/index.html", ContentType.HTML);
	}
	
	@Request(url = "/login")
	public void getLogin(Handler request, Handler response) {
		
		response.addTemplate("nav", NAV_TEMPLATE);
		response.addAttribute("login", "");
		response.showPage("/login.html", ContentType.HTML);
	}
	
	@Request(url = "/login", method = RequestMethod.POST)
	public void postLogin(Handler request, Handler response) {
		
		String username = request.getParameter().get("username");
		String password = request.getParameter().get("password");
		
		response.getHeader().setCookie(COOKIE_NAME, response.requestKey(username, password), 60 * 60 * 24);
		response.redirect("/");
	}
	
	@Request(url = "/logout", method = RequestMethod.POST)
	public void postLogout(Handler request, Handler response) {
		
		response.getHeader().setCookie(COOKIE_NAME, null, 0);
		response.redirect("/");
	}
}
