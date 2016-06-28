package com.tomtop.forms;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * retry payment form
 * 
 * @author lijun
 *
 */
public class RetryOrderForm {
	@NotNull
	String orderNum;

	@NotNull
	String paymentId;

	@Nullable
	String qiwiCountry;

	@Nullable
	String qiwiAccount;

	@Nullable
	String pay_typeCode;
	
	/**
	 * 信用卡支付时账单地址是否是收货地址
	 */
	@Nullable
	boolean billAddrIsShipAddrCredit;
	
	/**
	 * 信用卡支付时选择的账单地址id
	 */
	@Nullable
	Integer chooseBillAddrIdCredit;
	
	/**
	 * jcb支付时账单地址是否是收货地址
	 */
	@Nullable
	boolean billAddrIsShipAddrJcb;
	
	/**
	 * jcb支付时选择的账单地址id
	 */
	@Nullable
	Integer chooseBillAddrIdJcb;

	public boolean getBillAddrIsShipAddrCredit() {
		return billAddrIsShipAddrCredit;
	}

	public void setBillAddrIsShipAddrCredit(boolean billAddrIsShipAddrCredit) {
		this.billAddrIsShipAddrCredit = billAddrIsShipAddrCredit;
	}

	public Integer getChooseBillAddrIdCredit() {
		return chooseBillAddrIdCredit;
	}

	public void setChooseBillAddrIdCredit(Integer chooseBillAddrIdCredit) {
		this.chooseBillAddrIdCredit = chooseBillAddrIdCredit;
	}

	public boolean getBillAddrIsShipAddrJcb() {
		return billAddrIsShipAddrJcb;
	}

	public void setBillAddrIsShipAddrJcb(boolean billAddrIsShipAddrJcb) {
		this.billAddrIsShipAddrJcb = billAddrIsShipAddrJcb;
	}

	public Integer getChooseBillAddrIdJcb() {
		return chooseBillAddrIdJcb;
	}

	public void setChooseBillAddrIdJcb(Integer chooseBillAddrIdJcb) {
		this.chooseBillAddrIdJcb = chooseBillAddrIdJcb;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
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

	public String getPay_typeCode() {
		return pay_typeCode;
	}

	public void setPay_typeCode(String pay_typeCode) {
		this.pay_typeCode = pay_typeCode;
	}

	@Override
	public String toString() {
		return "RetryOrderForm [orderNum=" + orderNum + ", paymentId="
				+ paymentId + ", qiwiCountry=" + qiwiCountry + ", qiwiAccount="
				+ qiwiAccount + ", pay_typeCode=" + pay_typeCode
				+ ", billAddrIsShipAddrCredit=" + billAddrIsShipAddrCredit
				+ ", chooseBillAddrIdCredit=" + chooseBillAddrIdCredit
				+ ", billAddrIsShipAddrJcb=" + billAddrIsShipAddrJcb
				+ ", chooseBillAddrIdJcb=" + chooseBillAddrIdJcb + "]";
	}

}
