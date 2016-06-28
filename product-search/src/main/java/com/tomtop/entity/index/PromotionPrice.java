package com.tomtop.entity.index;

/**
 * 促销价
 * @author ztiny
 * @Date 2015-12-21
 */
public class PromotionPrice {

	//促销价格
	private Double price;
	//开始时间
	private String beginDate;
	//结束时间
	private String endDate;

	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
