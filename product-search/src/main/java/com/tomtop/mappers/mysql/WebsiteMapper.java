package com.tomtop.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface WebsiteMapper {

	@Select({"select id from base_site"})
	List<Integer> getWebsite();
}
