package com.tomtop.base.models.dto;

import java.io.Serializable;

import com.tomtop.base.models.base.TableBaseBean;


/**
 * 国家基类
 * 
 * @author renyy
 *
 */
public class CountryBase extends TableBaseBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;//国家名称
	private String nationalFlagImgUrl;//国旗图片url
	private String isoCodeTwo;//ISO Code (2)-2位的ISO国家代码
	private String isoCodeThree;//ISO Code(3)-3位的ISO国家代码
	private String addressFormat;//地址格式
	private Integer isRequiredPostcode;//邮编是否必填,0:不必须,1:必须
	private String currency;//货币
	private Integer officialLanguageId;//官方语言
	private Integer languageId;//语言ID
	private String weigthUnit;//重量单位
	private String lengthUnit;//测量单位
	private Integer sort;//排序号
	
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
	public String getNationalFlagImgUrl() {
		return nationalFlagImgUrl;
	}
	public void setNationalFlagImgUrl(String nationalFlagImgUrl) {
		this.nationalFlagImgUrl = nationalFlagImgUrl;
	}
	public String getIsoCodeTwo() {
		return isoCodeTwo;
	}
	public void setIsoCodeTwo(String isoCodeTwo) {
		this.isoCodeTwo = isoCodeTwo;
	}
	public String getIsoCodeThree() {
		return isoCodeThree;
	}
	public void setIsoCodeThree(String isoCodeThree) {
		this.isoCodeThree = isoCodeThree;
	}
	public String getAddressFormat() {
		return addressFormat;
	}
	public void setAddressFormat(String addressFormat) {
		this.addressFormat = addressFormat;
	}
	public Integer getIsRequiredPostcode() {
		return isRequiredPostcode;
	}
	public void setIsRequiredPostcode(Integer isRequiredPostcode) {
		this.isRequiredPostcode = isRequiredPostcode;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getOfficialLanguageId() {
		return officialLanguageId;
	}
	public void setOfficialLanguageId(Integer officialLanguageId) {
		this.officialLanguageId = officialLanguageId;
	}
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	public String getWeigthUnit() {
		return weigthUnit;
	}
	public void setWeigthUnit(String weigthUnit) {
		this.weigthUnit = weigthUnit;
	}
	public String getLengthUnit() {
		return lengthUnit;
	}
	public void setLengthUnit(String lengthUnit) {
		this.lengthUnit = lengthUnit;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	
	
	
}
