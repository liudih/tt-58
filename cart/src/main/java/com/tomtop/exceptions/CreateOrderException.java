package com.tomtop.exceptions;

/**
 * 
 * @author lijun
 *
 */
public class CreateOrderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CreateOrderException(String message) {
		super(message);
	}

}
