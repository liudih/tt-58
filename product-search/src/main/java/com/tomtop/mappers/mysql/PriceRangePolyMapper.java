package com.tomtop.mappers.mysql;


import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.PriceRangePoly;

public interface PriceRangePolyMapper {

	@Select({"select client_id client,filter_name filterName,aliases,show_aliases showAliases,greater,less,",
		    "greater_agg greaterAgg,less_agg lessAgg from price_range_poly where is_enabled=1 and is_deleted=0"})
	List<PriceRangePoly> getPriceRangePolyList();
}
