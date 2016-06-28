package com.tomtop.valueobjects;

/**
 * 
 * @author lijun
 *
 */
public class Coupon {

	String code;
	// 是否现金券
	boolean isCash;
	// 如果是现金券则该值是double，如果是折扣券该值是百分比
	String value;
	// 币种
	String unit;
	
	String minAmount;
	
	String validStartDate;
	
	String validEndDate;
	
	String validDays;
	
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean getIsCash() {
		return isCash;
	}

	public void setIsCash(boolean isCash) {
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

	public String getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(String minAmount) {
		this.minAmount = minAmount;
	}

	public String getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(String validStartDate) {
		this.validStartDate = validStartDate;
	}

	public String getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(String validEndDate) {
		this.validEndDate = validEndDate;
	}

	public String getValidDays() {
		return validDays;
	}

	public void setValidDays(String validDays) {
		this.validDays = validDays;
	}

}
