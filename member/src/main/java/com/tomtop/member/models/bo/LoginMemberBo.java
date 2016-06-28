package com.tomtop.member.models.bo;


public class LoginMemberBo extends BaseBo {

	private Integer id;//用户ID
	private String email;//邮件
	private String account;//昵称
	private String country;//用户国家
	private String token;
	private String uuid;
	private String aid;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
}
