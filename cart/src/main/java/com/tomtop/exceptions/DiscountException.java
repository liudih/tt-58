package com.tomtop.exceptions;

import com.tomtop.services.impl.LoyaltyService.Type;

/**
 * 
 * @author lijun
 *
 */
public class DiscountException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String code;
	private final Type type;
	private final String message;

	public DiscountException(String code, Type type, String message) {
		this.code = code;
		this.type = type;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public Type getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

}
