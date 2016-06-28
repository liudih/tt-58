package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.HomeBrand;

public interface HomeBrandMapper {

	@Select({"select name,code,url,logo_url logoUrl from home_brand ",
			"where client_id=#{0} and language_id=#{1} and is_enabled=1 and is_deleted=0" })
	List<HomeBrand> getHomeTopBrand(Integer client,Integer lang);
}