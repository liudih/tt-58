package com.tomtop.entity;

import java.util.List;
import com.tomtop.entity.index.StartNum;

public class ProductBaseDepotDtl extends BaseBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//详情基本信息
	private List<ProductDepotDetails> pdbList;
	//desc
	private String desc;
	//shippingPayment
	private String shippingPayment = null;
	//warranty
	private String warranty = null;
	//SEO title
	private String metaTitle;
	//Seo 描述
	private String metaDescription;
	//Seo关键字
	private String metaKeyword;
	//评论总数
	private Integer count;
	//评论星级平均数
	private Double start;
	//星级对应总评论的总数
	private List<StartNum> startNum;
	
	public List<ProductDepotDetails> getPdbList() {
		return pdbList;
	}
	public void setPdbList(List<ProductDepotDetails> pdbList) {
		this.pdbList = pdbList;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getShippingPayment() {
		return shippingPayment;
	}
	public void setShippingPayment(String shippingPayment) {
		this.shippingPayment = shippingPayment;
	}
	public String getWarranty() {
		return warranty;
	}
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	public String getMetaTitle() {
		return metaTitle;
	}
	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getMetaKeyword() {
		return metaKeyword;
	}
	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getStart() {
		return start;
	}
	public void setStart(Double start) {
		this.start = start;
	}
	public List<StartNum> getStartNum() {
		return startNum;
	}
	public void setStartNum(List<StartNum> startNum) {
		this.startNum = startNum;
	}
}
