package com.tomtop.entity;

/**
 * 首页最近订单信息
 * 
 * @author liulj
 *
 */
public class HomeRecentOrders extends ProductBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2247038737543161052L;
	/**
	 * 国家
	 */
	private String country;
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String countryName) {
		this.country = countryName;
	}
}
