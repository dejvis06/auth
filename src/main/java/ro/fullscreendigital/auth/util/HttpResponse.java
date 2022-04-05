package ro.fullscreendigital.auth.util;

import org.springframework.http.HttpStatus;

public class HttpResponse {

	private int httpStatusCode;

	private HttpStatus httpStatus;

	private String reason;

	private String message;

	private Object object;

	public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message, Object object) {
		super();
		this.setHttpStatusCode(httpStatusCode);
		this.httpStatus = httpStatus;
		this.reason = reason;
		this.message = message;
		this.object = object;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
