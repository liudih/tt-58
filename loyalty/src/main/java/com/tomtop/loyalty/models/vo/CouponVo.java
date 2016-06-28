package com.tomtop.loyalty.models.vo;

public class CouponVo {
	private String code;  //优惠券码
	private Boolean isCash; //是否是现金优惠券
	private String value;//值--现金券就是金额，折扣券就是折扣率
	private String unit;//单位--现金券就是现金的单位，折扣券就是OFF

	
	public CouponVo() {
		super();
	}

	public CouponVo(String code, Boolean isCash, String value, String unit) {
		super();
		this.code = code;
		this.isCash = isCash;
		this.value = value;
		this.unit = unit;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getIsCash() {
		return isCash;
	}

	public void setIsCash(Boolean isCash) {
		this.isCash = isCash;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
