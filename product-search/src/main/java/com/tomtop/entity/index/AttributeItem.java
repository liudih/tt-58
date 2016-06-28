package com.tomtop.entity.index;

/**
 * 多属性
 * @author ztiny
 * @Date 2015-12-21
 */
public class AttributeItem {
	//属性对应的ID
	private Integer keyId;
	//属性对应的键
	private String key;
	//属性值对应的ID
	private Integer valueId;
	//属性值对应的真实值
	private String value;
	//属性是否显示
	private Boolean isShow;
	//是否为sku属性 true为是 false为否
	private Boolean isMult;
	//显示类型 1：文本 2：色块 3：图片
	private Integer displayType;
	
	public Integer getKeyId() {
		return keyId;
	}
	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Integer getValueId() {
		return valueId;
	}
	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Boolean getIsShow() {
		return isShow;
	}
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	public Boolean getIsMult() {
		return isMult;
	}
	public void setIsMult(Boolean isMult) {
		this.isMult = isMult;
	}
	public Integer getDisplayType() {
		return displayType;
	}
	public void setDisplayType(Integer displayType) {
		this.displayType = displayType;
	}

}
