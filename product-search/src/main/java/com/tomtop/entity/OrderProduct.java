package com.tomtop.entity;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("deprecation")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class OrderProduct extends ProductBase {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4898310861851235334L;
	/**
	 * 属性集合
	 */
	private Map<String,String> attributeMap;
	/**
	 * 商品状态
	 */
	private Integer status;
	/**
	 * 货币使用的符号
	 */
	private String symbol;
	
	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}
	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
