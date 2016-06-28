package com.tomtop.member.mappers.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.MemberOtherId;



public interface MemberOtherMapper {

	@Insert("insert into t_member_other_id (cemail, csource, csourceid, bvalidated) "
			+ "values (#{cemail},#{csource},#{csourceid},#{bvalidated})")
	int insert(MemberOtherId otherId);
	
	@Select("select iid,cemail, csource, csourceid,dcreatedate, bvalidated from t_member_other_id "
			+ "where csource=#{0} AND csourceid=#{1} limit 1")
	MemberOtherId getBySource(String source, String sourceId);
}
