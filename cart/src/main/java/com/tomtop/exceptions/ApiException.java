package com.tomtop.exceptions;

public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 7655424350902376109L;

	public ApiException(String message) {
		super(message);
	}
}
