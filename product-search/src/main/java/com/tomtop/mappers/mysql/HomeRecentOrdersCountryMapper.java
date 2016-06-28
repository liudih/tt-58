package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;

public interface HomeRecentOrdersCountryMapper {
	@Select({
			"select t2.name from home_recent_orders_country ",
			"t1 inner join base_country t2 on t2.id=t1.country_id ",
			" and t1.language_id=t2.language_id and t2.is_enabled = 1 and t2.is_deleted = 0",
			"where t1.client_id=#{0} and t1.language_id=#{1} and t1.is_enabled = 1 and t1.is_deleted = 0" })
	List<String> getCountryNameListByClientLang(int client,int lang);
}