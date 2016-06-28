package com.tomtop.dto.paypal;

public class PaypalLog {

	Integer iid;
	Integer iwebsiteid;
	String corderid;
	String ccontent;
	String dcreatedate;
	String ctransactionid;

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

	public String getCorderid() {
		return corderid;
	}

	public void setCorderid(String corderid) {
		this.corderid = corderid;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public String getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(String dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCtransactionid() {
		return ctransactionid;
	}

	public void setCtransactionid(String ctransactionid) {
		this.ctransactionid = ctransactionid;
	}

}
