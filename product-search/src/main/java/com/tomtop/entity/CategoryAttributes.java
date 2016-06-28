package com.tomtop.entity;

import java.io.Serializable;
/**
 * 品类聚合属性
 * @author renyy
 *
 */
public class CategoryAttributes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5324868217425453079L;
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
