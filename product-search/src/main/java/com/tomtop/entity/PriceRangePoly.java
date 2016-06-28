package com.tomtop.entity;

import java.io.Serializable;

public class PriceRangePoly implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3684114386117410891L;
	private Integer client;
	private String filterName;
	private String aliases;
	private String showAliases;
	private String greater;
	private String less;
	private Double greaterAgg;
	private Double lessAgg;
	
	public Integer getClient() {
		return client;
	}
	public void setClient(Integer client) {
		this.client = client;
	}
	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	public String getAliases() {
		return aliases;
	}
	public void setAliases(String aliases) {
		this.aliases = aliases;
	}
	public String getShowAliases() {
		return showAliases;
	}
	public void setShowAliases(String showAliases) {
		this.showAliases = showAliases;
	}
	public String getGreater() {
		return greater;
	}
	public void setGreater(String greater) {
		this.greater = greater;
	}
	public String getLess() {
		return less;
	}
	public void setLess(String less) {
		this.less = less;
	}
	public Double getGreaterAgg() {
		return greaterAgg;
	}
	public void setGreaterAgg(Double greaterAgg) {
		this.greaterAgg = greaterAgg;
	}
	public Double getLessAgg() {
		return lessAgg;
	}
	public void setLessAgg(Double lessAgg) {
		this.lessAgg = lessAgg;
	}
	
}
