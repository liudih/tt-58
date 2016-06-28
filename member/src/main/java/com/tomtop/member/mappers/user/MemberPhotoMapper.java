package com.tomtop.member.mappers.user;


import org.apache.ibatis.annotations.Select;

import com.tomtop.member.models.dto.MemberPhoto;


public interface MemberPhotoMapper {

	int deleteByPrimaryKey(Integer iid);

	int insert(MemberPhoto record);

	int insertSelective(MemberPhoto record);

	MemberPhoto selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(MemberPhoto record);

	int updateByPrimaryKey(MemberPhoto record);

	@Select("select iid, cemail, ccontenttype, bfile, cmd5,cimageurl "
			+ "from t_member_photo where cemail=#{0} and iwebsiteid = #{1} limit 1")
	MemberPhoto getMemberPhotoByEamil(String eamil, Integer websiteId);
}
