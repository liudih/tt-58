package com.tomtop.valueobjects.product;

import com.tomtop.utils.Utils;

public class ProductVoV2 {

	private String symbol;
	private Double nowprice;
	private String imageUrl;
	private Double origprice;
	private String sku;
	private String url;
	private String listingId;
	private String title;
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Double getNowprice() {
		return nowprice;
	}
	public String getNowpriceStr(String currency) {
		return Utils.money(this.getNowprice(), currency);
	}
	
	public void setNowprice(Double nowprice) {
		this.nowprice = nowprice;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Double getOrigprice() {
		return origprice;
	}
	
	public String getOrigpriceStr(String currency) {
		return Utils.money(this.getOrigprice(), currency);
	}
	
	public void setOrigprice(Double origprice) {
		this.origprice = origprice;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isDiscount(){
		Double chajia = this.getOrigprice()-this.getNowprice();
		if(chajia>0){
			return true;
		}else{
			return false;
		}
	}
	
	public String getDiscount(){
		if(this.isDiscount()){
			return Utils.percent((this.getOrigprice()-this.getNowprice())/this.getOrigprice());
		}else{
			return "";
		}
	}
	
}
