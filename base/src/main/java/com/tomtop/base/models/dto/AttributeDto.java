package com.tomtop.base.models.dto;

import java.io.Serializable;

public class AttributeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6112148800389044449L;
	private String keyName;
	private String valueName;
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

}
