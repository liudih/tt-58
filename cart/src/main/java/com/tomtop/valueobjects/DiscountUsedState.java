package com.tomtop.valueobjects;

/**
 * 优惠券使用结果
 * 
 * @author lijun
 *
 */
public class DiscountUsedState {
	// 折扣金额
	private final Double discount;
	private final String code;
	// 错误信息
	private final String errMsg;
	private final boolean succeed;

	public DiscountUsedState(Double discount, String code, String errMsg,
			boolean succeed) {
		this.discount = discount;
		this.code = code;
		this.errMsg = errMsg;
		this.succeed = succeed;
	}

	public Double getDiscount() {
		return discount;
	}

	public String getCode() {
		return code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public boolean isSucceed() {
		return succeed;
	}

}
