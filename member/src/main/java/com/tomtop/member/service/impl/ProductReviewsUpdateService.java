package com.tomtop.member.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.member.mappers.interaction.InteractionCommentMapper;
import com.tomtop.member.models.dto.InteractionComment;
import com.tomtop.member.service.IProductReviewsUpdateService;


@Service
public class ProductReviewsUpdateService implements IProductReviewsUpdateService {
	
	public final static  String POSITIVE = "POSITIVE"; //好评
	public final static String NEUTRAL = "NEUTRAL"; //中评
	public final static String NEGATIVE = "NEGATIVE"; //差评
	

	@Autowired
	InteractionCommentMapper commentMapper;

	/**
	 * 新增评论
	 *
	 * @param Comment
	 * 
	 * @return int
	 */
	public Integer saveInteractionComment(InteractionComment comment) {
		try {
			return commentMapper.insertComment(comment);
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}
 

	 

}
