package com.tomtop.enums;

/**
 * 支付状态
 * @author liuchengqiang
 * 2016年6月3日 下午5:44:10
 */
public enum PaymentStatus {
	UN_PAY(0,"待付款"),
	CONFIRMED(1,"收款成功"),
	PENDING(2,"待定"),
	FAILURE(3,"支付失败"),
	REFUNDED(4,"已退款");
	
	private int code;
	private String alias;
	
	private PaymentStatus(int code,String alias){
		this.code = code;
		this.alias = alias;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
