package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface HomeFeaturedCategorySkuMapper {
	@Select({
			"select listing_id,sort from home_featured_category_sku",
			"where featured_category_id = #{0} and is_deleted = 0 ",
			"and  is_enabled=1 and client_id=#{1} and language_id=#{2} and listing_id is not null ORDER BY sort" })
	List<String> getListByFcategoryClientLangua(int fcategoryid, int client, int lang);
}