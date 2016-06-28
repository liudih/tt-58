package com.tomtop.member.service;


import java.io.IOException;

import com.tomtop.member.models.bo.MemberPhotoBo;
import com.tomtop.member.models.dto.MemberPhoto;

public interface IMemberPhotoService {
	public boolean updatePhoto(byte[] bytes, String contenttype, String eamil, Integer siteId, String cimageurl)throws IOException;

	public boolean updateMemberPhoto(MemberPhoto memberPhoto);
	
	public MemberPhotoBo getMemberPhoto(String email, Integer websiteId);
}