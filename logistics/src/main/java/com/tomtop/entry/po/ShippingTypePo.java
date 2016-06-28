package com.tomtop.entry.po;

import java.io.Serializable;

public class ShippingTypePo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1892331140058632419L;
	private int id;
	private String typeName;
	private int typeOrder;
	private String shippingCode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getTypeOrder() {
		return typeOrder;
	}
	public void setTypeOrder(int typeOrder) {
		this.typeOrder = typeOrder;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	@Override
	public String toString() {
		return "ShippingTypePo [id=" + id + ", typeName=" + typeName
				+ ", typeOrder=" + typeOrder + ", shippingCode=" + shippingCode
				+ "]";
	}
	
	
	
}
