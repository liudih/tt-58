package com.tomtop.member.service.impl;


import java.io.IOException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.member.mappers.user.MemberPhotoMapper;
import com.tomtop.member.models.bo.MemberPhotoBo;
import com.tomtop.member.models.dto.MemberPhoto;
import com.tomtop.member.service.IMemberPhotoService;
import com.tomtop.member.utils.CommonUtils;
import com.tomtop.member.utils.HttpSendRequest;
@Service
public class MemberPhotoServiceImpl implements IMemberPhotoService {

	@Autowired
	MemberPhotoMapper photoMapper;

	@Value("${event_register_url}")
	private String event_register_url;
	
	@Value("${event_register_key}")
	private String event_register_key;
	
	@Override
	public boolean updatePhoto(byte[] buff, String contenttype, String eamil, Integer siteId, String cimageurl) throws IOException {

		
		int result = 0;

		MemberPhoto photo  = photoMapper.getMemberPhotoByEamil(eamil, siteId);
		if (photo == null) {
			photo = new MemberPhoto();
//			photo.setBfile(buff);
			photo.setCcontenttype(contenttype);
			photo.setCemail(eamil);
//			photo.setCmd5(UUID.randomUUID().toString());
			photo.setIwebsiteid(siteId);
			photo.setCimageurl(cimageurl);
			result = photoMapper.insert(photo);
			if(result >0){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("code", "EVENT_CODE_UPLOAD_MEMBER_PHOTO");
				JSONObject param = new JSONObject();
				param.put("email", eamil);
				param.put("website", siteId);
				jsonObj.put("param", param);
				//会员头像成功送积分或者优惠卷
				HttpSendRequest.sendPostByAsync(this.event_register_url, jsonObj.toJSONString(), this.event_register_key);
				
			}
		} else {
//			photo.setBfile(buff);
//			photo.setCmd5(UUID.randomUUID().toString());
			photo.setCimageurl(cimageurl);
			photo.setCcontenttype(contenttype);
			result = photoMapper.updateByPrimaryKeySelective(photo);
		}
		
		return result > 0 ? true : false;
	}

	@Override
	public boolean updateMemberPhoto(MemberPhoto memberPhoto) {
		Integer result = 0;
		MemberPhoto memberPhotoByEamil = photoMapper.getMemberPhotoByEamil(
				memberPhoto.getCemail(), memberPhoto.getIwebsiteid());
		if (null != memberPhotoByEamil) {
			memberPhoto.setIid(memberPhotoByEamil.getIid());
			result = photoMapper.updateByPrimaryKeySelective(memberPhoto);
		} else {
			result = photoMapper.insert(memberPhoto);
		}
		return result > 0 ? true : false;
	}
	
	
	@Override
	public MemberPhotoBo getMemberPhoto(String email, Integer websiteId) {
		MemberPhotoBo mbb = new MemberPhotoBo();
		MemberPhoto mb = this.photoMapper.getMemberPhotoByEamil(email, websiteId);
		if(mb != null){
			try {
				mbb.setRes(CommonUtils.SUCCESS_RES);
				BeanUtils.copyProperties(mbb, mb);
			} catch (Exception e) {
				mbb.setRes(CommonUtils.ERROR_RES);
				mbb.setMsg("MemberPhoto copyProperties fail ");
				e.printStackTrace();
			}
		}else{
			mbb.setRes(CommonUtils.ERROR_RES);
			mbb.setMsg("email not found ");
		}
		return mbb;
	}
}
