package com.tomtop.loyalty.models;

import java.io.Serializable;

public class Prefer implements Serializable {

	/**
	 * 应用优惠后的返回结果
	 */
	private static final long serialVersionUID = 1L;

	// 应用是否成功
	private Boolean isSuccess;

	// 优惠的金额,应用成功返回的为负数
	private Double value;

	private String code;

	// 优惠类型：coupon,推广码,积分
	private String preferType;

	private String errMsg;

	// 根据自己需要记录额外信息
	private String extra;

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getPreferType() {
		return preferType;
	}

	public void setPreferType(String preferType) {
		this.preferType = preferType;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Boolean isSuccess() {
		return null == isSuccess ? false : isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
