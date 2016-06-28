package com.tomtop.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface HomeSearchAutocompleteMapper {

	@Select({
			"<script>",
			"<bind name=\"keyword\" value=\"_parameter.keyword+'%'\" /> ",
			"select keyword from home_search_autocomplete",
			"where keyword like #{keyword} and is_enabled = 1 and is_deleted = 0 and client_id=#{client} and language_id=#{lang}",
			"order by keyword limit 10", "</script>" })
	List<String> getKeywordList(
			@Param("keyword") String keyword, @Param("client") int client,
			@Param("lang") int lang);
}