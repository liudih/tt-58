package com.tomtop.forms;

import javax.validation.constraints.NotNull;

public class OrderConfirmForm {
	@NotNull
	String orderNum;
	@NotNull
	String cfirstname;

	String clastname;
	@NotNull
	String address1;

	String address2;

	@NotNull
	String cprovince;
	@NotNull
	String ccity;
	@NotNull
	String cpostalcode;
	@NotNull
	String ctelephone;
	@NotNull
	String PayerID;
	@NotNull
	String token;
	@NotNull
	String shipMethodCode;
	@NotNull
	String countryCode; // 国家缩写

	String leaveMessage;

	String countryName; // 国家

	String countrysn; // 国家缩写

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getCfirstname() {
		return cfirstname;
	}

	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname;
	}

	public String getClastname() {
		return clastname;
	}

	public void setClastname(String clastname) {
		this.clastname = clastname;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCprovince() {
		return cprovince;
	}

	public void setCprovince(String cprovince) {
		this.cprovince = cprovince;
	}

	public String getCcity() {
		return ccity;
	}

	public void setCcity(String ccity) {
		this.ccity = ccity;
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

	public String getPayerID() {
		return PayerID;
	}

	public void setPayerID(String payerID) {
		PayerID = payerID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getShipMethodCode() {
		return shipMethodCode;
	}

	public void setShipMethodCode(String shipMethodCode) {
		this.shipMethodCode = shipMethodCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getLeaveMessage() {
		return leaveMessage;
	}

	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCountrysn() {
		return countrysn;
	}

	public void setCountrysn(String countrysn) {
		this.countrysn = countrysn;
	}
}
