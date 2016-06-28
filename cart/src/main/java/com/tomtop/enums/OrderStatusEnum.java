package com.tomtop.enums;

/**
 * 订单状态
 * @author liuchengqiang
 * @time 2016年6月3日 下午5:43:39
 */
public enum OrderStatusEnum {

	PAYMENT_PENDING(1,"待付款"),
	PAYMENT_CONFIRMED(2,"收款成功"),
	ORDER_CANCELLED(3,"订单已取消"),
	PROCESSING(4,"订单正在处理中"),
	ON_HOLD(5,"订单审核中"),
	DISPATCHED(6,"订单已发货"),
	COMPLETED(7,"订单已完成"),
	REFUNDED(8,"已退款"),
	PAYMENT_PROCESSING(9,"付款处理中");
	
	private int code;
	private String alias;
	
	private OrderStatusEnum(int code,String alias){
		this.code = code;
		this.alias = alias;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
