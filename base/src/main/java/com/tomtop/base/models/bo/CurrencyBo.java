package com.tomtop.base.models.bo;

import com.tomtop.base.models.base.BaseBean;

public class CurrencyBo extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6793888137930699697L;
	private Integer id;
	private String name;//货币名称
	private String code;//标识
	private Integer symbolPositions;//1:left,2:right.货币在左边还是右边
	private String symbolCode;//货币符号
	private Double currentRate;//当前汇率
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return checkNull(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return checkNull(code);
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
		return checkNull(symbolCode);
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
}
