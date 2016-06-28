package com.tomtop.base.models.dto;

import java.io.Serializable;
import java.util.Date;

import com.tomtop.base.models.base.TableBaseBean;


/**
 * 货币基类
 * @author renyy
 *
 */
public class CurrencyBase extends TableBaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;//货币名称
	private String code;//标识
	private Integer symbolPositions;//1:left,2:right.货币在左边还是右边
	private String symbolCode;//货币符号
	private Double currentRate;//当前汇率
	private Double newRate;//新汇率值
	private Integer decimalPlaces;//汇率小数位
	private Date synchroDate;//同步时间
	private Integer sort;//排序
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getSymbolPositions() {
		return symbolPositions;
	}
	public void setSymbolPositions(Integer symbolPositions) {
		this.symbolPositions = symbolPositions;
	}
	public String getSymbolCode() {
		return symbolCode;
	}
	public void setSymbolCode(String symbolCode) {
		this.symbolCode = symbolCode;
	}
	public Double getCurrentRate() {
		return currentRate;
	}
	public void setCurrentRate(Double currentRate) {
		this.currentRate = currentRate;
	}
	public Double getNewRate() {
		return newRate;
	}
	public void setNewRate(Double newRate) {
		this.newRate = newRate;
	}
	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}
	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}
	public Date getSynchroDate() {
		return synchroDate;
	}
	public void setSynchroDate(Date synchroDate) {
		this.synchroDate = synchroDate;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
