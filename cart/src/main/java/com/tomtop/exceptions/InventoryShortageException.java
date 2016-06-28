package com.tomtop.exceptions;

/**
 * 
 * @author liuchengqiang
 * 库存不足异常
 */
public class InventoryShortageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InventoryShortageException(String message) {
		super(message);
	}

}
