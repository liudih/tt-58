package com.tomtop.member.service;

import java.util.List;

import com.tomtop.member.models.dto.InteractionComment;
import com.tomtop.member.models.dto.ReviewsInMemberCenter;

public interface IMemberReviewsService {

	Integer getTotalReviewsCountByMemberEmailAndSiteId(String email,
			Integer status,
			Integer dateType, Integer websiteId);

	Integer getPendingReviewsCountByMemberEmailAndSiteId(String email,
			Integer websiteId);

	Integer getApprovedReviewsCountByMemberEmailAndSiteId(String email,
			Integer websiteId);

	Integer getFailedReviewsCountByMemberEmailAndSiteId(String email,
			Integer websiteId);

	List<ReviewsInMemberCenter> getMyReviewsPageByEmail(String email,
			Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, Integer websiteId);

	public ReviewsInMemberCenter getReviewsInMemberCenterById(Integer id,String email);
	
	public InteractionComment getReviewById(Integer id,String email);

	public Boolean updateInteractionComment(InteractionComment review);

	public Boolean deleteInteractionComment(Integer iid,String email);

	public int insertSelective(InteractionComment record) throws Exception;

}
