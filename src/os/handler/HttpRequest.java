package os.handler;

import os.type.Cookie;
import os.type.Header;
import os.type.RequestMethod;
import os.type.Session;
import os.type.Status;
import os.util.Property;
import os.util.Utils;


public class HttpRequest {

	private String url;
	private RequestMethod requestMethod;
	private Status status;
	private Property<String> headers;
	private Property<String> parameters;
	private Session session;
	private byte[] body;

	public HttpRequest() {

		headers = new Property<String>();
		parameters = new Property<String>();
	}

	public String getParameter(String key) {

		return parameters.get(key);
	}

	public Cookie[] getCookies() {

		Cookie[] cookies;
		Property<String> property = new Property<String>();

		if(headers.hasKey(Header.COOKIE.getCode())) {

			String line = headers.get(Header.COOKIE.getCode());

			if(line.contains("; ")) {

				for(String arg : line.split("; ")) {

					Utils.addAttribute(property, "=", arg);
				}
			}
			else {

				Utils.addAttribute(property, "=", line);
			}
		}

		cookies = new Cookie[property.size()];

		int i = 0;

		for(String key : property.keys()) {

			cookies[i] = new Cookie(key, property.get(key));

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

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {

		this.url = url;
	}

	public RequestMethod getRequestMethod() {

		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {

		this.requestMethod = requestMethod;
	}

	public Status getStatus() {

		return status;
	}

	public void setStatus(Status status) {

		this.status = status;
	}

	public Property<String> getHeaders() {

		return headers;
	}

	public void setHeaders(Property<String> headers) {

		this.headers = headers;
	}

	public Property<String> getParameters() {

		return parameters;
	}

	public void setParameters(Property<String> parameters) {

		this.parameters = parameters;
	}

	public Session getSession() {

		return session;
	}

	public void setSession(Session session) {

		this.session = session;
	}

	public byte[] getBody() {

		return body;
	}

	public void setBody(byte[] body) {

		this.body = body;
	}
}
