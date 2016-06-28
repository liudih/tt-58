package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;
import com.tomtop.entity.LayoutModuleContent;

/**
 * 布局模块内容mapper
 * 
 * @author liulj
 *
 */
public interface BaseLayoutModuleContentMapper {

	@Select({"select id, layout_code layoutCode,listing_id listingId,layout_id loyoutId, layout_module_code layoutModuleCode, layout_module_id layoutModuleId,"
			,"category_id categoryId, sku, sort from base_layout_module_content "
			,"where language_id=#{0} and client_id=#{1} and layout_code=#{2} and is_show=1 "
			,"and is_enabled=1 and is_deleted=0 and listing_id is not null  order by layout_module_code,sort"})
	List<LayoutModuleContent> getListByLayoutModule(Integer language,Integer clientId,String layoutcode);
}