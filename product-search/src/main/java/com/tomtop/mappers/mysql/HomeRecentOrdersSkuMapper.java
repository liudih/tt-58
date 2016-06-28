package com.tomtop.mappers.mysql;

import java.util.List;
import org.apache.ibatis.annotations.Select;

public interface HomeRecentOrdersSkuMapper {

	@Select({
			"select listing_id from home_recent_orders_sku",
			"where client_id=#{0} and language_id=#{1} ",
			"and is_enabled = 1 and is_deleted = 0 and listing_id is not null" })
	List<String> getListByClientLang(int client, int lang);
}