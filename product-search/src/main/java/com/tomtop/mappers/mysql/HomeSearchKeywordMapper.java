package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.tomtop.entity.HomeSearchKeyword;

public interface HomeSearchKeywordMapper {

	@Select({
			"select sort, keyword",
			"from home_search_keyword",
			"where category_id = #{categoryId} and is_enabled = 1 and is_deleted = 0 and client_id=#{client} and language_id=#{lang} ORDER BY sort" })
	List<HomeSearchKeyword> getKeywordList(
			@Param("categoryId") int categoryId, @Param("client") int client,
			@Param("lang") int lang);

}