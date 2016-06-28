package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.BaseLayout;

public interface BaseLayoutMapper {
	
	@Select({"select name, url, keyword, description,title, remark",
			"from base_layout where code=#{0} and client_id=#{1} and language_id=#{2} and is_enabled=1 and is_deleted=0" })
	List<BaseLayout> getBaseLayoutByCode(String code, int client, int lang);
}