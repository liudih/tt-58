package com.tomtop.services;


import java.util.List;

import com.tomtop.entity.BaseBean;
import com.tomtop.entity.CollectCount;

/**
 * 产品收藏
 * 
 * @author liulj
 *
 */
public interface IProductCollectService {
	/**
	 * 获取收藏数
	 * 
	 * @param listingIds
	 * @return
	 */
	public List<CollectCount> getCollectCountByListingIds(
			List<String> listingIds,Integer website);
	
	/**
	 * 获取单个商品收藏数
	 * 
	 * @param listingIds
	 * @return
	 */
	public CollectCount getCollectCountByListingId(
			String listingIds,Integer website);
	
	/**
	 * 添加收藏
	 * 
	 * @param listingIds
	 * @return
	 */
	public BaseBean addCollectCount(String listingId,String email,Integer website);
	
	/**
	 * 根据邮箱获取收藏的listingId 集合
	 * 
	 * @param listingIds
	 * @return
	 */
	public List<String> getCollectListingIdByEmail(String email,Integer website);
}