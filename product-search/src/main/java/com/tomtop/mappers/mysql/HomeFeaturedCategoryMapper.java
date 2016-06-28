package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.HomeFeaturedCategory;

public interface HomeFeaturedCategoryMapper {
	@Select({
			"select id, img_url imgUrl, number,category_id categoryId, sort ",
			" from home_featured_category where  is_deleted = 0 and is_enabled=1 and client_id=#{0} and language_id=#{1} ORDER BY sort" })
	List<HomeFeaturedCategory> getListClientLangua(int client, int lang);
}