package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.Currency;
import com.tomtop.entity.HomeDailyDeal;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.mappers.mysql.HomeDailyDealMapper;
import com.tomtop.services.IHomeDailyDealService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

/**
 * 首页获取每日推荐业务逻辑类
 * 
 * @author liulj
 *
 */
@Service
public class HomeDailyDealServiceImpl extends BaseService implements IHomeDailyDealService {

	Logger logger = Logger.getLogger(HomeFeaturedCategoryServiceImpl.class);
	
	@Autowired
	HomeDailyDealMapper dailDealMapper;
	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	/**
	 * 获取每日推荐
	 * 
	 * @param startDate
	 *            开始时间
	 * @param client
	 * @param language
	 * @return
	 */
	private List<HomeDailyDeal> getListByStartDate(Date startDate, int client,
			int language) {
		List<HomeDailyDeal> dto = dailDealMapper.getListByStartDate(startDate,
				client, language);
		if (dto == null || dto.isEmpty()) {
			if(language > CommonDefn.ONE){
				dto =  dailDealMapper.getListByStartDate(startDate, client, CommonDefn.ONE);
			}
		}
		return dto;
	}
	
	/**
	 * 获取每日推荐商品详细
	 * 
	 * @param startDate
	 *            开始时间
	 * @param client
	 * @param website
	 * @param language
	 * @param currency
	 * @return
	 */
	@Cacheable(value="daily_deal", cacheManager = "dayCacheManager", keyGenerator = "customKeyGenerator")
	@Override
	public List<HomeDailyDeal> getHomeDailyDealList(Date startDate, int client,
			int language,int website,String currency){
		logger.info("================create redis getHomeDailyDealList================");
		List<HomeDailyDeal> hdd = this.getListByStartDate(startDate, client, language);
		List<String> listings = new ArrayList<String>();
		Map<String,Float> lmap = new HashMap<String,Float>();
		if(hdd != null && hdd.size() > 0){
			for (HomeDailyDeal hd : hdd) {
				listings.add(hd.getListingId());
				lmap.put(hd.getListingId(), hd.getDiscount());
			}
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listings), language, website);
			if(ieList != null && ieList.size() > 0){
				Currency currencyBean = this.getCurrencyBean(currency); 
				List<HomeDailyDeal> hddList = new ArrayList<HomeDailyDeal>();
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					HomeDailyDeal deal = new HomeDailyDeal();
					deal.setDiscount(lmap.get(ie.getListingId()));
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(deal, ie, currencyBean);
					hddList.add(deal);
				}
				
				return hddList;
			}
		}
		
		return hdd;
	}

	/**
	 * 获取每日推荐商品详情(增加仓库)
	 * 
	 * @param startDate
	 *            开始时间
	 * @param depotName
	 *            仓库名称
	 * @param client
	 * @param website
	 * @param language
	 * @param currency
	 * @return
	 */
	@Cacheable(value="daily_deal", cacheManager = "dayCacheManager", keyGenerator = "customKeyGenerator")
	@Override
	public List<HomeDailyDeal> getHomeDailyDealList(Date startDate,String depotName, int client,
			int language,int website,String currency){
		logger.info("================create redis getHomeDailyDealList================");
		List<HomeDailyDeal> hdd = this.getListByStartDate(startDate, client, language);
		List<String> listings = new ArrayList<String>();
		Map<String,Float> lmap = new HashMap<String,Float>();
		if(hdd != null && hdd.size() > 0){
			for (HomeDailyDeal hd : hdd) {
				listings.add(hd.getListingId());
				lmap.put(hd.getListingId(), hd.getDiscount());
			}
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listings), language, website);
			if(ieList != null && ieList.size() > 0){
				Currency currencyBean = this.getCurrencyBean(currency); 
				List<HomeDailyDeal> hddList = new ArrayList<HomeDailyDeal>();
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					HomeDailyDeal deal = new HomeDailyDeal();
					deal.setDiscount(lmap.get(ie.getListingId()));
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(deal, ie, currencyBean,depotName);
					hddList.add(deal);
				}
				
				return hddList;
			}
		}
		
		return hdd;
	}
}
