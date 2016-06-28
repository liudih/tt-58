package com.tomtop.member.models.dto;

public enum MessageStatus {
	DELETE("delete", 0), READ("read", 1), EDIT("edit", 2), PUBLISH("publish", 3), unread(
			"unread", 4);

	private String describe;
	private int code;

	private MessageStatus(String describe, int code) {
		this.describe = describe;
		this.code = code;
	}

	public String getDescribe() {
		return describe;
	}

	public int getCode() {
		return code;
	}

}
