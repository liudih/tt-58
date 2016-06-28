package com.tomtop.member.models.bo;

public class RegisterMemberBo extends BaseBo {

	private String email;//邮件
	private String account;//昵称
	private String country;//用户国家
	private String token;
	private String uuid;
	
	public String getEmail() {
		return checkNull(email);
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccount() {
		return checkNull(account);
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCountry() {
		return checkNull(country);
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
