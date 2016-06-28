package com.tomtop.loyalty.models.vo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class CouponVoMore extends CouponVo{
	// 优惠券有效开始时间
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date validStartDate;
	// 优惠券有效结束
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date validEndDate;
	//最低消费
	private double minAmount;
	//有效天数
	private Integer validDays;
	//创建时间
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	
//	//币种
//	private String currency;
	
	public CouponVoMore() {
		super();
	}

	public CouponVoMore(String code, Boolean isCash, String value, String unit,
		Date validStartDate, Date validEndDate, double minAmount,
		Integer validDays, Date createDate) {
		super(code, isCash, value, unit);
		this.validStartDate = validStartDate;
		this.validEndDate = validEndDate;
		this.minAmount = minAmount;
		this.validDays = validDays;
		this.createDate = createDate;
	}


	public Date getValidStartDate() {
		return validStartDate;
	}
	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}
	public Date getValidEndDate() {
		return validEndDate;
	}
	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}
	public double getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
	}
	public Integer getValidDays() {
		return validDays;
	}
	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
