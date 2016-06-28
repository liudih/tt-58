package com.tomtop.exceptions;

/**
 * 用户未登陆异常
 * 
 * @author lijun
 *
 */
public class UserNoLoginException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "user no login";
	}

}
