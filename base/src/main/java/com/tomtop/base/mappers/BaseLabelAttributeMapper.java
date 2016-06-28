package com.tomtop.base.mappers;

import java.util.List;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.tomtop.base.models.dto.BaseLabelAttributeDto;

public interface BaseLabelAttributeMapper {

	@Select({
			"select",
			"id, language_id, client_id,parent_id,label_id, is_change, level, category_id, attribute_key, attribute_value, sort ",
			"from base_label_attribute where is_enabled=1 and is_deleted=0 and client_id=#{0} and language_id=#{1} order by sort" })
	@ResultMap("BaseResultMap")
	List<BaseLabelAttributeDto> getListByClientLang(Integer client, Integer lang);

	@Select({
			"select",
			"id, language_id, client_id,parent_id,label_id, is_change, level, category_id, attribute_key, attribute_value, sort ",
			"from base_label_attribute where is_enabled=1 and is_deleted=0 and category_id=#{0} and client_id=#{1} and language_id=#{2} order by sort" })
	@ResultMap("BaseResultMap")
	List<BaseLabelAttributeDto> getListByCategoryId(Integer categoryId,Integer client, Integer lang);

	@Select({
			"select",
			"id, language_id, client_id,parent_id,label_id, is_change, level, category_id, attribute_key, attribute_value, sort ",
			"from base_label_attribute where is_enabled=1 and is_deleted=0 and attribute_key=#{0} and client_id=#{1} and language_id=#{2} order by sort" })
	@ResultMap("BaseResultMap")
	List<BaseLabelAttributeDto> getListByKey(String key,Integer client, Integer lang);

}