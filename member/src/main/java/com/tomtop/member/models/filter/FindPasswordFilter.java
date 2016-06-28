package com.tomtop.member.models.filter;

import com.tomtop.member.models.base.FilterBaseBean;


public class FindPasswordFilter extends FilterBaseBean {

	private static final long serialVersionUID = 8560802821562783178L;
	private String email = "";
	private String pwd = "";
	private String cid = "";
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
}
