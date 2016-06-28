package com.tomtop.entity;

import java.io.Serializable;
import java.util.List;

import com.tomtop.entity.index.ProductImageEntity;

/**
 * 导出数据详情类
 */
public class ReportProductDataBo implements Serializable {

	
	private static final long serialVersionUID = 4053065535671032374L;
	private String listingId;
	private String productUrl;//产品URL地址
	private String price; //现价
	private String oldprice; //原价
	private List<ProductImageEntity> imgs; 
	private List<Integer> categoryIds; 
	private String currencyCode;
	private String title;//产品标题
	private String desc;//产品描述
	private String sku;
	private Boolean topseller;//是否热卖,有hot标签的
	private String brand;//品牌名字,没有的话为空
	private String searchTerms; //关键字，没有的话就写产品标题/名称
	private String shortDesc; //简要描述
	private Integer status; //状态
	private Integer storageId;//仓库Id
	
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getProductUrl() {
		return productUrl;
	}
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOldprice() {
		return oldprice;
	}
	public void setOldprice(String oldprice) {
		this.oldprice = oldprice;
	}
	public List<ProductImageEntity> getImgs() {
		return imgs;
	}
	public void setImgs(List<ProductImageEntity> imgs) {
		this.imgs = imgs;
	}
	public List<Integer> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Boolean getTopseller() {
		return topseller;
	}
	public void setTopseller(Boolean topseller) {
		this.topseller = topseller;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getSearchTerms() {
		return searchTerms;
	}
	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getStorageId() {
		return storageId;
	}
	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}
}
