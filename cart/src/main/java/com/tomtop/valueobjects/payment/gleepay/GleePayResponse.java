package com.tomtop.valueobjects.payment.gleepay;

/**
 * 鼎付支付响应
 * @author liuchengqiang
 * @time 2016年6月20日 下午4:31:28
 */
public class GleePayResponse {
	
	//商户号
	private String merNo;
	
	//网关接入号
	private String gatewayNo;
	
	//交易流水号
	private String tradeNo;
	
	//订单号
	private String orderNo;
	
	//货币币种
	private String orderCurrency;
	
	//交易金额	
	private String orderAmount;
	
	/**
	 * 交易状态
	 * -2: 待确认 
	 * -1: 待处理 
	 * 0: 失败 
	 * 1: 成功
	 */
	private String orderStatus;
	
	//交易结果信息（Code+具体信息）
	private String orderInfo;
	
	//账单地址
	private String billAddress;
	
	//数据签名
	private String signInfo;
	
	//支付方式
	private String paymentMethod;
	
	/**
	 * 返回类型 
	 * 1: 浏览器实时返回 
	 * 2 ：服务器实时返回 
	 * 3 ：服务器异步返回
	 */
	private String returnType;
	
	//备注
	private String Remark;

	public String getMerNo() {
		return merNo;
	}

	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}

	public String getGatewayNo() {
		return gatewayNo;
	}

	public void setGatewayNo(String gatewayNo) {
		this.gatewayNo = gatewayNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderCurrency() {
		return orderCurrency;
	}

	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}

	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	public String getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	@Override
	public String toString() {
		return "GleePayResponse [merNo=" + merNo + ", gatewayNo=" + gatewayNo
				+ ", tradeNo=" + tradeNo + ", orderNo=" + orderNo
				+ ", orderCurrency=" + orderCurrency + ", orderAmount="
				+ orderAmount + ", orderStatus=" + orderStatus + ", orderInfo="
				+ orderInfo + ", billAddress=" + billAddress + ", signInfo="
				+ signInfo + ", paymentMethod=" + paymentMethod
				+ ", returnType=" + returnType + ", Remark=" + Remark + "]";
	}
	
	public boolean isSuccess(){
		return "1".equals(orderStatus);
	}
}
