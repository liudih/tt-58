package com.tomtop.forms;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * paypal 和 钱海共用 所以钱海需要再做必须字段的验证
 * 
 * @author lijun
 *
 */
public class PlaceOrderForm {
	@NotNull
	Integer addressId;

	@Nullable
	Integer billId;

	//邮寄方式
	@NotNull
	String shipMethodCode;

	@Nullable
	String message;

	@NotNull
	String paymentId;

	@NotNull
	Integer storageid;

	@Nullable
	Integer billAddressId;

	@Nullable
	String qiwiCountry;

	@Nullable
	String qiwiAccount;

	@Nullable
	String userName;

	@Nullable
	String userEmail;

	@Nullable
	String pay_typeCode;

	@Nullable
	String pay_cpf;
	
	//发卡行
	@Nullable
	String icIssuer;

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getShipMethodCode() {
		return shipMethodCode;
	}

	public void setShipMethodCode(String shipMethodCode) {
		this.shipMethodCode = shipMethodCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getStorageid() {
		return storageid;
	}

	public void setStorageid(Integer storageid) {
		this.storageid = storageid;
	}

	public Integer getBillAddressId() {
		return billAddressId;
	}

	public void setBillAddressId(Integer billAddressId) {
		this.billAddressId = billAddressId;
	}

	public String getQiwiCountry() {
		return qiwiCountry;
	}

	public void setQiwiCountry(String qiwiCountry) {
		this.qiwiCountry = qiwiCountry;
	}

	public String getQiwiAccount() {
		return qiwiAccount;
	}

	public void setQiwiAccount(String qiwiAccount) {
		this.qiwiAccount = qiwiAccount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPay_typeCode() {
		return pay_typeCode;
	}

	public void setPay_typeCode(String pay_typeCode) {
		this.pay_typeCode = pay_typeCode;
	}

	public String getPay_cpf() {
		return pay_cpf;
	}

	public void setPay_cpf(String pay_cpf) {
		this.pay_cpf = pay_cpf;
	}

	public String getIcIssuer() {
		return icIssuer;
	}

	public void setIcIssuer(String icIssuer) {
		this.icIssuer = icIssuer;
	}

	@Override
	public String toString() {
		return "PlaceOrderForm [addressId=" + addressId + ", billId=" + billId
				+ ", shipMethodCode=" + shipMethodCode + ", message=" + message
				+ ", paymentId=" + paymentId + ", storageid=" + storageid
				+ ", billAddressId=" + billAddressId + ", qiwiCountry="
				+ qiwiCountry + ", qiwiAccount=" + qiwiAccount + ", userName="
				+ userName + ", userEmail=" + userEmail + ", pay_typeCode="
				+ pay_typeCode + ", pay_cpf=" + pay_cpf + ", icIssuer="
				+ icIssuer + "]";
	}

}
