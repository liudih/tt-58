package com.tomtop.member.mappers.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.Statistics;

public interface InteractionMapper {
	
	@Select({"select 'collect' as name ,count(*) qty from t_product_collect ",
			"where cemail=#{0} and iwebsiteid=#{1} UNION ",
			"select 'review' as name,count(*) qty from t_interaction_comment ",
			"where cmemberemail=#{0} and iwebsiteid=#{1}"})
	List<Statistics> getCount(String email,Integer website);
}
