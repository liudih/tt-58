package com.tomtop.mappers.member;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.DropshipBase;

public interface DropshipMapper {

	@Select("select b.cemail,l.clevelname,l.discount,l.iproductcount from t_dropship_base b "
			+ "LEFT JOIN t_dropship_level l on b.idropshiplevel=l.iid "
			+ "where b.cemail=#{0} and b.iwebsiteid=#{1} ")
	public DropshipBase getDropshipBaseDto(String email,Integer siteId);
}
