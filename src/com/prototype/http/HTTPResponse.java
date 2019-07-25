package com.prototype.http;

import java.io.File;
import java.nio.file.Path;

import com.prototype.Prototype;
import com.prototype.http.constants.Header;
import com.prototype.http.constants.MediaType;
import com.prototype.http.constants.Status;
import com.prototype.type.Property;


public class HTTPResponse {

	private HTTPRequest request;
	private Status status;
	private Property<String> attributes;

	public HTTPResponse(HTTPRequest request) {

		this.request = request;
		attributes = new Property<>();
	}

	public void ok() {

		status = Status.OK;
	}

	public void notFound() {

		status = Status.NOT_FOUND;
	}

	public void forbidden() {

		status = Status.FORBIDDEN;
	}

	public void badRequest() {

		status = Status.BAD_REQUEST;
	}

	public void noContent() {

		status = Status.NO_CONTENT;
	}

	public void created() {

		status = Status.CREATED;
	}

	public void redirect(String url) {

		request.getHeaders().put(Header.LOCATION.getCode(), url);
		status = Status.TEMPORARY_REDIRECT;
	}

	public void view(String body, MediaType type) {

		request.setBody(body.getBytes(Prototype.constant().CHARSET));
		request.setType(type);
		status = Status.OK;
	}

	public void viewPage(Path path, MediaType type) {

		File file = path.toFile();

		if(file.exists() && file.isFile()) {

			try {

				byte[] data = Prototype.loader().read(path);

				request.setBody(data);
				request.setType(type);
				status = Status.OK;
			}
			catch(Exception e) {

				status = Status.NOT_FOUND;
			}
		}
		else {

			status = Status.NOT_FOUND;
		}
	}

	public void download(Path path) {

		File file = path.toFile();

		if(file.exists() && file.isFile()) {

			try {

				byte[] data = Prototype.loader().read(path);

				download(data, file.getName());
			}
			catch(Exception e) {

				status = Status.NOT_FOUND;
			}
		}
		else {

			status = Status.NOT_FOUND;
		}
	}

	public void download(byte[] data, String name) {

		request.getHeaders().put(Header.CONTENT_DISPOSITION.getCode(), "attachment; filename=\"" + name + "\"");
		request.setBody(data);
		status = Status.OK;
	}

	public void addAttribute(String key, String value) {

		attributes.put(key, value);
	}

	public void addHeader(Header header, String value) {

		addHeader(header.getCode(), value);
	}

	public void addHeader(String key, String value) {

		request.getHeaders().put(key, value);
	}

	public HTTPRequest getRequest() {

		return request;
	}

	public void setRequest(HTTPRequest request) {

		this.request = request;
	}

	public Status getStatus() {

		return status;
	}

	public void setStatus(Status status) {

		this.status = status;
	}

	public Double getVersion() {

		return request.getVersion();
	}

	public void setVersion(Double version) {

		request.setVersion(version);
	}

	public Property<String> getAttributes() {

		return attributes;
	}

	public void setAttributes(Property<String> attributes) {

		this.attributes = attributes;
	}

	public Property<String> getHeaders() {

		return request.getHeaders();
	}

	public void setHeaders(Property<String> headers) {

		request.setHeaders(headers);
	}

	public MediaType getType() {

		return request.getType();
	}

	public void setType(MediaType type) {

		request.setType(type);
	}

	public byte[] getBody() {

		return request.getBody();
	}

	public void setBody(byte[] body) {

		request.setBody(body);
	}
}