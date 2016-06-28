package com.tomtop.entity;

public class CategoryLable extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7626889739183668382L;
	private Integer categoryId;
	private String name;
	private Integer level;
	private Integer iparentid;
	private String cpath;
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getIparentid() {
		return iparentid;
	}
	public void setIparentid(Integer iparentid) {
		this.iparentid = iparentid;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	} 
}
