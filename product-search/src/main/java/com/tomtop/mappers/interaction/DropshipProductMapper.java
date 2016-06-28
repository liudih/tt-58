package com.tomtop.mappers.interaction;


import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.DropshipProduct;

public interface DropshipProductMapper {

	@Select("select count(iid) from t_dropship_product where cemail =#{0} and bstate=true and iwebsiteid=#{1} ")
	Integer getDropshipPrdouctCountByEmail(String email, Integer siteId);
	
	int insertSelective(DropshipProduct record);
	
	DropshipProduct getDropshipProduct(DropshipProduct record);
}