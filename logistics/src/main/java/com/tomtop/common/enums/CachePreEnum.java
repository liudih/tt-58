package com.tomtop.common.enums;

public enum CachePreEnum {
	
	filterNamePre("sp_result_filter_"),
	ShippingTypePre("sp_temp_type_"),
	ShippingTitleDescriptionPre("sp_titlDes_"),
	ShippingTemplatePre("sp_temp_detail_"),
	ShippingProductBasePre("sp_product_base_");
	
	private String typeName;

	private CachePreEnum(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return this.typeName;
	}
	
}
