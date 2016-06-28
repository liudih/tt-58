package com.tomtop.mappers.mysql;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.HomeDailyDeal;

public interface HomeDailyDealMapper {
	
	@Select({
			"select listing_id listingId,discount ",
			"from home_daily_deal where start_date=#{startDate, jdbcType=DATE} and client_id=#{client} ",
			"and language_id=#{language} and is_enabled = 1 and is_deleted=0  and listing_id is not null" })
	List<HomeDailyDeal> getListByStartDate(
			@Param("startDate") Date startDate, @Param("client") int client,
			@Param("language") int language);
}