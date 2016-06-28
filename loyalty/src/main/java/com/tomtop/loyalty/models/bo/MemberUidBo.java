package com.tomtop.loyalty.models.bo;

import com.tomtop.framework.core.utils.Result;

public class MemberUidBo extends Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;//邮件
	private String account;//昵称
	private String aid;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
}
