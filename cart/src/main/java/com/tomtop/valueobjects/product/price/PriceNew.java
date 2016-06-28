package com.tomtop.valueobjects.product.price;

import java.io.Serializable;
import java.util.Date;

/**
 * 价格对象(重构,后面稳定了去掉名字后缀'New')
 * 
 * @author shuliangxing
 *
 * @date 2016年6月8日 下午5:23:47
 */
public class PriceNew implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 471278938764657L;

	/** 商品唯一标识 */
	private String listingId;

	/** 成本价 */
	private double costPrice;

	/** 现价 */
	private double price;

	/** 促销价 */
	private double salePrice;

	/**
	 * 促销价开始时间
	 */
	private Date salePriceBegin;

	/**
	 * 促销价结束时间
	 */
	private Date salePriceEnd;

	/**
	 * 折扣 (0.0 - 1.0] NULL表示没有折扣
	 * 
	 */
	private Double discount;

	/**
	 * 货币类型
	 */
	private String currency;

	/**
	 * 货币使用的符号
	 */
	private String symbol = "$";

	/**
	 * 兑换率
	 */
	private double rate = 1.0;

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Date getSalePriceBegin() {
		return salePriceBegin;
	}

	public void setSalePriceBegin(Date salePriceBegin) {
		this.salePriceBegin = salePriceBegin;
	}

	public Date getSalePriceEnd() {
		return salePriceEnd;
	}

	public void setSalePriceEnd(Date salePriceEnd) {
		this.salePriceEnd = salePriceEnd;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "PriceNew [listingId=" + listingId + ", costPrice=" + costPrice
				+ ", price=" + price + ", salePrice=" + salePrice
				+ ", salePriceBegin=" + salePriceBegin + ", salePriceEnd="
				+ salePriceEnd + ", discount=" + discount + ", currency="
				+ currency + ", symbol=" + symbol + ", rate=" + rate + "]";
	}

}
