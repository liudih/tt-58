package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.ProductBaseRecommend;


/**
 * 获取推荐类数据
 * 
 * @author liulj
 *
 */
public interface IProductRecommendService {

	public List<ProductBaseRecommend> getRecommendProduct(String listingId,Integer website, Integer lang,
			String currency,Integer size,String type,String depotName);
	
}
