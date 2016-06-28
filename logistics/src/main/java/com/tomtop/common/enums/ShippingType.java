package com.tomtop.common.enums;

/**
 * 
 * @author Administrator
 *
 */
public enum ShippingType {
	SurfaceType("SurfaceType"), 		//平邮
	RegistType("RegistType"), 			//挂号
	ExpressType("ExpressType"), 		//快递
	SpecialType("SpecialType");			//特快

	private String typeName;

	private ShippingType(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return this.typeName;
	}
}