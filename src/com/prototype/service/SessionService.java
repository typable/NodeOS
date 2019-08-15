package com.prototype.service;

import java.util.UUID;

import com.prototype.Prototype;
import com.prototype.http.HTTPRequest;
import com.prototype.http.HTTPResponse;
import com.prototype.type.Cookie;
import com.prototype.type.Session;


public class SessionService {

	private Prototype prototype;

	public SessionService(Prototype prototype) {

		this.prototype = prototype;
	}

	public void prepareSession(HTTPRequest request, HTTPResponse response) {

		if(request.hasCookie(Session.USID)) {

			Cookie cookie = request.getCookie(Session.USID);

			for(Session session : prototype.getSessions()) {

				if(session.getUid() != null && cookie.getValue() != null) {

					if(session.getUid().equals(cookie.getValue())) {

						request.setSession(session);
					}
				}
			}
		}
		else {

			Session session = new Session(UUID.randomUUID().toString());

			Cookie cookie = Cookie.of(session);
			cookie.setAge(60 * 60 * 24);

			request.setSession(session);
			response.addCookie(cookie);
		}
	}
}