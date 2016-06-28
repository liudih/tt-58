package com.tomtop.exceptions;

/**
 * 无效仓库异常
 * 
 * @author lijun
 *
 */
public class InvalidStorageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "invalid storage";
	}

}
