package com.tomtop.services;


import java.util.List;

import com.tomtop.entity.ProductReview;
import com.tomtop.entity.rc.FiveReview;

public interface IInteractionCommentService {
	
	public List<ProductReview> getProductReviewBoList(String listingId,Integer siteId);
	public List<ProductReview> getProductReviewPage(String listingId,Integer siteId,Integer page,Integer size);
	public Integer getProductReviewCount(String listingId,Integer siteId);
	public List<FiveReview> getRcHomeFiveReview(Integer website,Integer lang);
}
