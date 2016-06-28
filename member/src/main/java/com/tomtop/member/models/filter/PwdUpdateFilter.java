package com.tomtop.member.models.filter;

import java.io.Serializable;

import com.tomtop.member.models.base.FilterBaseBean;

public class PwdUpdateFilter  extends FilterBaseBean  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	String cemail;
	String cpassword;
	String cnewpassword;
	String ccnewpassword;
	Integer client;
	String cuuid;
	
	public Integer getClient() {
		return client;
	}
	public void setClient(Integer client) {
		this.client = client;
	}
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	public String getCpassword() {
		return cpassword;
	}
	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}
	public String getCnewpassword() {
		return cnewpassword;
	}
	public void setCnewpassword(String cnewpassword) {
		this.cnewpassword = cnewpassword;
	}
	public String getCcnewpassword() {
		return ccnewpassword;
	}
	public void setCcnewpassword(String ccnewpassword) {
		this.ccnewpassword = ccnewpassword;
	}
	 
	public String getCuuid() {
		return cuuid;
	}
	public void setCuuid(String cuuid) {
		this.cuuid = cuuid;
	}
	
}
