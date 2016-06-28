package com.tomtop.member.service;

import java.util.List;

import com.tomtop.member.models.bo.InteractionVideoBo;
import com.tomtop.member.models.dto.InteractionProductMemberVideo;

public interface IInteractionProductVideoService {

	InteractionProductMemberVideo getCommentVideoByCommentIdLimit1(Integer commentId);

	int insertSelective(InteractionProductMemberVideo record);
	
	Integer getTotalVideoCountByMemberEmailAndSiteId(String email, Integer websiteId);
	
	public List<InteractionVideoBo> getMyReviewsVideos(String email,
			Integer total, Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, Integer websiteId);
	
	boolean deleteVideosByListingIdAndEmail(String email,
			Integer websiteId, String listingId);
	
    InteractionProductMemberVideo getVideoByCommentIdAndEmailAndSiteId(Integer commentId, String email, Integer websiteId);
    
    int updateByPrimaryKeySelective(InteractionProductMemberVideo record);
}
