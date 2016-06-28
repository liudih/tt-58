package com.tomtop.loyalty.models;

import java.util.Date;

public class MemberIntegralHistory {
	private Integer iid;
	private Integer iwebsiteid;
	private String cemail;
	private String cdotype;
	private Integer iintegral;
	private String cremark;
	private Integer istatus;
	private Date dcreatedate;
	private String csource;
	private String createDateStr;
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	public String getCdotype() {
		return cdotype;
	}
	public void setCdotype(String cdotype) {
		this.cdotype = cdotype;
	}
	
	public Integer getIintegral() {
		return iintegral;
	}
	public void setIintegral(Integer iintegral) {
		this.iintegral = iintegral;
	}
	public String getCremark() {
		return cremark;
	}
	public void setCremark(String cremark) {
		this.cremark = cremark;
	}
	public Integer getIstatus() {
		return istatus;
	}
	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}
	public Date getDcreatedate() {
		return dcreatedate;
	}
	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
	public String getCsource() {
		return csource;
	}
	public void setCsource(String csource) {
		this.csource = csource;
	}
	public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	
}
