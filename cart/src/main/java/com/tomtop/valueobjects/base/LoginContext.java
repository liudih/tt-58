package com.tomtop.valueobjects.base;

import java.io.Serializable;

public class LoginContext implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8632142753906799616L;

	String email;
	int groupID;
	Serializable payload;
	String ltc;
	String stc;
	String currencyCode;
	String countryCode;

	public LoginContext(String ltc, String stc, String email, int groupID,
			Serializable payload) {
		super();
		this.ltc = ltc;
		this.stc = stc;
		this.email = email;
		this.groupID = groupID;
		this.payload = payload;
	}

	public LoginContext() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public Serializable getPayload() {
		return payload;
	}

	public boolean isLogin() {
		return (email != null && !"".equals(email));
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Long Term Cookie
	 *
	 * @see CoookieTrackingFilter
	 * @return
	 */
	public String getLTC() {
		return ltc;
	}

	/**
	 * Short Term Cookie
	 *
	 * @return
	 */
	public String getSTC() {
		return stc;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public String toString() {
		return "LoginContext [memberID=" + email + ", groupID=" + groupID
				+ ", payload=" + payload + ", ltc=" + ltc + ", stc=" + stc
				+ "]";
	}

}
