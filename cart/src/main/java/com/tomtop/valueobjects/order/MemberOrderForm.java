package com.tomtop.valueobjects.order;


import java.io.Serializable;

public class MemberOrderForm implements Serializable {
	private static final long serialVersionUID = 1L;
	Integer pageSize;
	Integer pageNum;
	Integer status;
	Integer interval;
	String productName;
	Integer isShow;
	Integer siteId;
	String start;
	String end;
	Integer orderId;
	String paymentId;
	String email;
	String transactionId;
	String vhost;
	String orderNumber;
	String firstName;
	private String tag;
	
	/**
	 * 语言 
	 */
	Integer lang;
	

	public Integer getPageSize() {
		return Integer.valueOf((this.pageSize != null) ? this.pageSize
				.intValue() : 10);
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return Integer.valueOf((this.pageNum != null) ? this.pageNum.intValue()
				: 1);
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getInterval() {
		return Integer.valueOf((this.interval != null) ? this.interval
				.intValue() : 0);
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public String getProductName() {
		if (this.productName==null || "".equals(this.productName)) {
			return null;
		}
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getIsShow() {
		return Integer.valueOf((this.isShow != null) ? this.isShow.intValue()
				: 1);
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getStart() {
		return this.start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return this.end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getPaymentId() {
		if (this.paymentId==null || "".equals(this.paymentId)) {
			return null;
		}
		return this.paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getEmail() {
		if (this.email==null || "".equals(this.email)) {
			return null;
		}
		return this.email.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTransactionId() {
		if (this.transactionId==null || "".equals(this.transactionId)) {
			return null;
		}
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getVhost() {
		if (this.vhost==null || "".equals(this.vhost)) {
			return null;
		}
		return this.vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getOrderNumber() {
		if (this.orderNumber==null || "".equals(this.orderNumber)) {
			return null;
		}
		return this.orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getFirstName() {
		if (this.firstName==null || "".equals(this.firstName)) {
			return null;
		}
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getLang() {
		if(this.lang==null){
			return 1;
		}
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}
}
