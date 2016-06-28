package com.tomtop.dto.order;

public class OrderDiscount {
	
	private String order_number;
	private Integer website;
	private String code;
	private Double discount;
	private String currency;
	private String type;
	public String getOrder_number() {
		return order_number;
	}
	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	public Integer getWebsite() {
		return website;
	}
	public void setWebsite(Integer website) {
		this.website = website;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
