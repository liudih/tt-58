package com.tomtop.member.service;

import com.tomtop.member.models.dto.InteractionComment;


public interface IProductReviewsUpdateService {
	/**
	 * 新增评论
	 *
	 * @param Comment
	 * 
	 * @return int
	 */
	public Integer saveInteractionComment(InteractionComment comment);

}
