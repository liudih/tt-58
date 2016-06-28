package com.tomtop.member.service;

import java.util.List;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.MemberReviewsBo;
import com.tomtop.member.models.bo.ReviewsBo;

public interface IReviewService {
	public List<MemberReviewsBo> getMemberReviewsBo(String email,Integer page, Integer size, Integer status,
			Integer dateType, Integer website);
	public BaseBo addReview(ReviewsBo rb,MultipartHttpServletRequest request) throws Exception;
	public BaseBo updateReview(ReviewsBo rb,String[] imageUrls,MultipartHttpServletRequest request) throws Exception;
}
