package com.tomtop.valueobjects;

import com.tomtop.services.impl.LoyaltyService.Type;

/**
 * 折扣BO
 * 
 * @author lijun
 *
 */
public class Discount {

	// 优惠券 推广码 积分
	private final String code;
	// 优惠券 推广码 积分 优惠的折扣
	private Double discount;
	// 币种
	private final String currency;

	private final Type type;

	public Discount(String code, Double discount, String currency, Type type) {
		this.code = code;
		this.discount = discount;
		this.currency = currency;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public Double getDiscount() {
		return discount;
	}

	public String getCurrency() {
		return currency;
	}

	public Type getType() {
		return type;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

}
