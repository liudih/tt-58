package com.tomtop.member.models.filter;

import com.tomtop.member.models.base.FilterBaseBean;

public class AddressFilter extends FilterBaseBean {
	
	private static final long serialVersionUID = 1L;
	private Integer iid;
	//会员邮箱
	private String cmemberemail;
	// 地址类型
	private Integer iaddressid;
	// 是否默认
	private Boolean bdefault;
	// 姓名首
	private String cfirstname;
	// 姓名中间
	private String cmiddlename;
	// 姓名尾
	private String clastname;
	// 公司
	private String ccompany;
	// 街道地址
	private String cstreetaddress;
	// 城市
	private String ccity;
	// 国家
	private Integer icountry;
	// 洲(省)
	private String cprovince;
	// 邮政编码
	private String cpostalcode;
	// 联系电话
	private String ctelephone;
	// 传真
	private String cfax;
	// 税号
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
