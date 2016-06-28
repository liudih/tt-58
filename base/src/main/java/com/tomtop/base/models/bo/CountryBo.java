package com.tomtop.base.models.bo;

import com.tomtop.base.models.base.BaseBean;

public class CountryBo extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7369890670524437723L;
	private Integer id;
	private String name;//国家名称
	private String nationalFlagImgUrl;//国旗图片url
	private String isoCodeTwo;//ISO Code (2)-2位的ISO国家代码
	private String isoCodeThree;//ISO Code(3)-3位的ISO国家代码
	private String currency;//货币
	private Integer officialLanguageId;//官方语言
	private Integer languageId;//语言ID
	
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
	public String getNationalFlagImgUrl() {
		return checkNull(nationalFlagImgUrl);
	}
	public void setNationalFlagImgUrl(String nationalFlagImgUrl) {
		this.nationalFlagImgUrl = nationalFlagImgUrl;
	}
	public String getIsoCodeTwo() {
		return checkNull(isoCodeTwo);
	}
	public void setIsoCodeTwo(String isoCodeTwo) {
		this.isoCodeTwo = isoCodeTwo;
	}
	public String getIsoCodeThree() {
		return checkNull(isoCodeThree);
	}
	public void setIsoCodeThree(String isoCodeThree) {
		this.isoCodeThree = isoCodeThree;
	}
	public String getCurrency() {
		return checkNull(currency);
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
}
