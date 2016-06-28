package com.tomtop.member.mappers.user;


import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.bo.MemberData;
import com.tomtop.member.models.dto.MemberBase;

public interface MemberBaseMapper {

	List<MemberBase> getMemberList(List<String> list);

	List<MemberBase> getMemberBase(@Param("email") String email,
			@Param("siteId") Integer siteId,
			@Param("caccount") String caccount,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);
	
	@Insert("insert into t_member_base(cemail,cpasswd,igroupid,ccountry,bnewsletter,bactivated,cvhost,iwebsiteid,cfirstname,clastname,dcreatedate) "
			+ "values(#{cemail},#{cpasswd},#{igroupid},#{ccountry},#{bnewsletter},#{bactivated},#{cvhost},#{iwebsiteid},#{cfirstname},#{clastname},now())")
	Integer insertMemberBase(MemberBase mb);
	
	Integer update(MemberBase mb);
	
	MemberBase getMemberBaseWhere(MemberBase mb);
	
	@Select("select * from t_member_base where cemail = #{0} and iwebsiteid=#{1} limit 1")
	MemberBase getMemberBaseByEmailAndWebsiteId(String email, int siteid);

	@Select("select * from t_member_base where cuuid = #{0}limit 1")
	MemberBase getMemberBaseByUuid(String email);
	
	Integer updateMemberData(MemberData mb);
}