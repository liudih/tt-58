package com.tomtop.member.mappers.tracking;

import org.apache.ibatis.annotations.Select;

public interface AffiliateInfoMapper {

	@Select("select caid from t_affiliate_info where cemail=#{0} and iwebsiteid=#{1} and bstatus=true")
	String getAidByEmail(String email,Integer siteId);
}
