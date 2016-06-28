package com.tomtop.member.mappers.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.Statistics;

public interface MemberIntegralHistoryMapper {
 
	@Select("select sum(iintegral) from t_member_integral_history where cemail = #{0} and iwebsiteid = #{1} and istatus = 1")
	Integer getSumByEmail(String email, Integer siteId);
	
	@Select({"select 'coupon' as name, count(*) qty from t_member_coupon ",
			"where istatus in(0,3) and cemail=#{0} and iwebsiteid=#{1} ",
			"union select 'integral' as name, sum(iintegral) qty from t_member_integral_history ",
			"where cemail=#{0} and iwebsiteid=#{1} and istatus=1"})
	List<Statistics> getStatisticsAvailableLoyalty(String email, Integer siteId);
 
}
