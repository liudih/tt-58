package com.tomtop.services;

import java.util.Date;
import java.util.List;

import com.tomtop.entity.HomeDailyDeal;

/**
 * 每日推荐
 * 
 * @author renyy
 *
 */
public interface IHomeDailyDealService {
	
	/**
	 * 获取每日推荐商品详情
	 * 
	 * @param startDate
	 *            开始时间
	 * @param client
	 * @param language
	 * @param currency
	 * @return
	 */
	public List<HomeDailyDeal> getHomeDailyDealList(Date startDate, int client,
			int language,int website,String currency);
	
	/**
	 * 获取每日推荐商品详情(增加仓库)
	 * 
	 * @param startDate
	 *            开始时间
	 * @param depotName
	 *            仓库名称
	 * @param client
	 * @param language
	 * @param currency
	 * @return
	 */
	public List<HomeDailyDeal> getHomeDailyDealList(Date startDate,String depotName, int client,
			int language,int website,String currency);
}