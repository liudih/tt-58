package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.HomeFeaturedCategoryBanner;

public interface HomeFeaturedCategoryBannerMapper {
	
	@Select({
			"select position_id positionId, url, img_url imgUrl, title,sort",
			"from home_featured_category_banner",
			"where featured_category_id = #{0} and  is_deleted = 0 and  is_enabled=1 and client_id=#{1} and language_id=#{2} ORDER BY sort" })
	List<HomeFeaturedCategoryBanner> getListByFcategoryClientLangua(int fcategoryid, int client, int lang);
}