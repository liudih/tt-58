package com.tomtop.entry.bo;

import java.io.Serializable;

public class ShippingCalculateLessParamBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6712445167049262729L;
	
	private String listingId;			  //SKU
	private String[] chrd;				  //捆绑商品	
	private int qty;					  ///数量
	/*private String templateId;			  //模板ID
*/	//private double price;				  //单价
	/*public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}*/
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	/*public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}*/
	public String[] getChrd() {
		return chrd;
	}
	public void setChrd(String[] chrd) {
		this.chrd = chrd;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	
	
	
}
