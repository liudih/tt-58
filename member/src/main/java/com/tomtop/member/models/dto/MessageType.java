package com.tomtop.member.models.dto;

public enum MessageType {

	ORDER_STATUS_CHANGE("order status change", "订单状态变更", 1), POINT_GET(
			"get point", "积分获取", 2), RED_ENVELOPE("rush for red envelope",
			"抢红包 ", 3), ACTIVITY("activity", "活动", 4), PERSONAL("personal",
			"个人", 5), WHOLESALE("wholesale", "wholesale", 6), DROPSHIP(
			"dropship", "dropship", 7);

	private String describeEN;
	private String describeCH;
	private int code;

	private MessageType(String describeEN, String describeCH, int code) {
		this.describeEN = describeEN;
		this.describeCH = describeCH;
		this.code = code;
	}

	public String getDescribeEN() {
		return describeEN;
	}

	public void setDescribeEN(String describeEN) {
		this.describeEN = describeEN;
	}

	public String getDescribeCH() {
		return describeCH;
	}

	public void setDescribeCH(String describeCH) {
		this.describeCH = describeCH;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static MessageType getType(int code) {
		MessageType[] types = MessageType.values();
		for (MessageType t : types) {
			if (t.getCode() == code) {
				return t;
			}
		}
		return null;
	}

}
