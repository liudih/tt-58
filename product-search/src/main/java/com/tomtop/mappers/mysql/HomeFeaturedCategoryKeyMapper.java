package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.HomeFeaturedCategoryKey;

public interface HomeFeaturedCategoryKeyMapper {
	@Select({
			"select keyword, sort from home_featured_category_key",
			"where featured_category_id = #{0} and is_deleted = 0 and is_enabled=1 and client_id=#{1} and language_id=#{2} ORDER BY sort" })
	List<HomeFeaturedCategoryKey> getListByFcategoryClientLangua(int fcategoryid, int client, int lang);
}