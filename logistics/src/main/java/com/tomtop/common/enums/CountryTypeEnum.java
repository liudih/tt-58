package com.tomtop.common.enums;

public enum CountryTypeEnum {
	
	Include("0"),
	Exclude("1"),
	All("2");
	
	private String typeName;

	private CountryTypeEnum(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return this.typeName;
	}
	
}
