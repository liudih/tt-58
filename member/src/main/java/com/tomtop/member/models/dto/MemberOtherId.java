package com.tomtop.member.models.dto;


import java.io.Serializable;
import java.util.Date;

public class MemberOtherId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	Integer iid;
	/**
	 * 邮件
	 */
	String cemail;
	/**
	 * 账号来源
	 */
	String csource;
	/**
	 * 登入网站的账号ID
	 */
	String csourceid;
	/**
	 * 创建时间
	 */
	Date dcreatedate;
	/**
	 * 是否验证过
	 */
	Boolean bvalidated;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCsource() {
		return csource;
	}

	public void setCsource(String csource) {
		this.csource = csource;
	}

	public String getCsourceid() {
		return csourceid;
	}

	public void setCsourceid(String csourceid) {
		this.csourceid = csourceid;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Boolean getBvalidated() {
		return bvalidated;
	}

	public void setBvalidated(Boolean bvalidated) {
		this.bvalidated = bvalidated;
	}

}
