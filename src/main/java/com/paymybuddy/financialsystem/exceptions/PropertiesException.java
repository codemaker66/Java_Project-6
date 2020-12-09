package com.paymybuddy.financialsystem.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;

public class PropertiesException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final HttpStatus httpStatus;
	private final List<String> details;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public List<String> getDetails() {
		return details;
	}

	public PropertiesException(HttpStatus httpStatus, String message, List<String> details) {
		super(message);
		this.httpStatus = httpStatus;
		this.details = details;
	}

}
