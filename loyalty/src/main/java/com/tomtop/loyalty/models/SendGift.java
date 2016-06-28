package com.tomtop.loyalty.models;

public class SendGift {

	private String email;
	private Integer website;
	private Integer point;
	private Integer amount;
	private Integer ruleid;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getWebsite() {
		return website;
	}
	public void setWebsite(Integer website) {
		this.website = website;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getRuleid() {
		return ruleid;
	}
	public void setRuleid(Integer ruleid) {
		this.ruleid = ruleid;
	}

}
