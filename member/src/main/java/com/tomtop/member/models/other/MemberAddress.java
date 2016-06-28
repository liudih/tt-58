package com.tomtop.member.models.other;

import com.tomtop.member.models.base.FilterBaseBean;

public class MemberAddress extends FilterBaseBean {
	
	private static final long serialVersionUID = 1L;
	private Integer iid;
	private String cmemberemail;
	private Integer iaddressid;
	private Boolean bdefault;
	private String cfirstname;
	private String cmiddlename;
	private String clastname;
	private String ccompany;
	private String cstreetaddress;
	private String ccity;
	private Integer icountry;
	private String cprovince;
	private String cpostalcode;
	private String ctelephone;
	private String cfax;
	private String cvatnumber;
	private String countryFullName;
	private String countryCode;
	private Integer ishipAddressId;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public Integer getIaddressid() {
		return iaddressid;
	}

	public void setIaddressid(Integer iaddressid) {
		this.iaddressid = iaddressid;
	}

	public Boolean getBdefault() {
		return bdefault;
	}

	public void setBdefault(Boolean bdefault) {
		this.bdefault = bdefault;
	}

	public String getCfirstname() {
		return cfirstname;
	}

	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname;
	}

	public String getCmiddlename() {
		return cmiddlename;
	}

	public void setCmiddlename(String cmiddlename) {
		this.cmiddlename = cmiddlename;
	}

	public String getClastname() {
		return clastname;
	}

	public void setClastname(String clastname) {
		this.clastname = clastname;
	}

	public String getCcompany() {
		return ccompany;
	}

	public void setCcompany(String ccompany) {
		this.ccompany = ccompany;
	}

	public String getCstreetaddress() {
		return cstreetaddress;
	}

	public void setCstreetaddress(String cstreetaddress) {
		this.cstreetaddress = cstreetaddress;
	}

	public String getCcity() {
		return ccity;
	}

	public void setCcity(String ccity) {
		this.ccity = ccity;
	}

	public Integer getIcountry() {
		return icountry;
	}

	public void setIcountry(Integer icountry) {
		this.icountry = icountry;
	}

	public String getCprovince() {
		return cprovince;
	}

	public void setCprovince(String cprovince) {
		this.cprovince = cprovince;
	}

	public String getCpostalcode() {
		return cpostalcode;
	}

	public void setCpostalcode(String cpostalcode) {
		this.cpostalcode = cpostalcode;
	}

	public String getCtelephone() {
		return ctelephone;
	}

	public void setCtelephone(String ctelephone) {
		this.ctelephone = ctelephone;
	}

	public String getCfax() {
		return cfax;
	}

	public void setCfax(String cfax) {
		this.cfax = cfax;
	}

	public String getCvatnumber() {
		return cvatnumber;
	}

	public void setCvatnumber(String cvatnumber) {
		this.cvatnumber = cvatnumber;
	}

	public String getCountryFullName() {
		return countryFullName;
	}

	public void setCountryFullName(String countryFullName) {
		this.countryFullName = countryFullName;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getIshipAddressId() {
		return ishipAddressId;
	}

	public void setIshipAddressId(Integer ishipAddressId) {
		this.ishipAddressId = ishipAddressId;
	}

}
