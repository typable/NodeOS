package com.prototype.http;

import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.RequestMethod;
import com.prototype.type.Parameter;
import com.prototype.type.Property;


public class HTTPRequest {

	private String url;
	private RequestMethod method;
	private Double version;
	private Property<String> headers;
	private Property<Parameter> parameters;
	private MediaType type;
	private byte[] body;

	public HTTPRequest() {

		headers = new Property<>();
	}

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {

		this.url = url;
	}

	public RequestMethod getMethod() {

		return method;
	}

	public void setMethod(RequestMethod method) {

		this.method = method;
	}

	public Double getVersion() {

		return version;
	}

	public void setVersion(Double version) {

		this.version = version;
	}

	public Property<String> getHeaders() {

		return headers;
	}

	public void setHeaders(Property<String> headers) {

		this.headers = headers;
	}

	public Property<Parameter> getParameters() {

		return parameters;
	}

	public void setParameters(Property<Parameter> parameters) {

		this.parameters = parameters;
	}

	public MediaType getType() {

		return type;
	}

	public void setType(MediaType type) {

		this.type = type;
	}

	public byte[] getBody() {

		return body;
	}

	public void setBody(byte[] body) {

		this.body = body;
	}
}
