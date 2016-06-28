package com.tomtop.loyalty.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Point {
	public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
	private Integer id;
	private Integer website;
	private String email;
	private String dotype;
	private Integer integral;
	private String remark;
	private Integer status;
	private Date createdate;
	private String source;
	private String createDateStr;

	public String getValidDate(String pattern) {
		if (this.createdate == null) {
			return "";
		}
		String result = null;
		try {
			SimpleDateFormat formater = new SimpleDateFormat(pattern);
			result = formater.format(this.createdate);
		} catch (Exception e) {
			SimpleDateFormat formater = new SimpleDateFormat(PATTERN);
			result = formater.format(this.createdate);
		}
		return result;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWebsite() {
		return website;
	}

	public void setWebsite(Integer website) {
		this.website = website;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDotype() {
		return dotype;
	}

	public void setDotype(String dotype) {
		this.dotype = dotype;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
		this.createDateStr = this.getValidDate(PATTERN);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

}
