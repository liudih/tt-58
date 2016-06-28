package com.tomtop.entity.index;


public class SalePrice {

	private Double price;
	//开始时间
	private String beginDate;
	//结束时间
	private String endDate;
	//折扣比
	private Double discount;
	//创建时间戳
	private Long createStamp;
	
	public SalePrice(){
		
	}
	public SalePrice(Double price,String beginDate,String endDate,Long createStamp){
		this.price = price;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.createStamp = createStamp;
	}
	
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
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Long getCreateStamp() {
		return createStamp;
	}
	public void setCreateStamp(Long createStamp) {
		this.createStamp = createStamp;
	}
	
}
