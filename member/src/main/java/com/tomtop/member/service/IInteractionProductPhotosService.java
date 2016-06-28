package com.tomtop.member.service;

import java.util.List;

import com.tomtop.member.models.bo.InteractionPhotoBo;
import com.tomtop.member.models.dto.InteractionProductMemberPhotos;

public interface IInteractionProductPhotosService {

	List<String> getCommentImgageByCommentIdLimit5(Integer commentId);

	int insertSelective(InteractionProductMemberPhotos record);
	
	public List<InteractionPhotoBo> getMyReviewsPhotos(String email,
			Integer total, Integer pageIndex, Integer pageSize, Integer status,
			Integer dateType, Integer websiteId);
	
	Integer getTotalPhotoCountByMemberEmailAndSiteId(String email, Integer websiteId);
	
	boolean deletePhotosByListingIdAndEmail(String email,
			Integer websiteId, String listingId);
	
	boolean deletePhotosByListingIdAndEmail(String email,
			Integer websiteId, String listingId, String imageUrl);
}
