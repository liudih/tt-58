package com.tomtop.entry.po;

import java.io.Serializable;

public class SkuMapTemplate implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5541697697025691012L;
	private String sku;
	private String templateId;
	private String mapKey;
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getMapKey() {
		return mapKey;
	}
	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}
	
}
