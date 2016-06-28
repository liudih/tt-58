package com.tomtop.common.enums;

public enum FilterTypeEnum {
	
	PriceLown("1"),
	PriceHigh("2");
	
	private String typeName;

	private FilterTypeEnum(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return this.typeName;
	}
	
}
