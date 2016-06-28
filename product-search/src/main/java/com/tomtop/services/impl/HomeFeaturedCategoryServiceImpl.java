package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.Currency;
import com.tomtop.entity.HomeFeaturedCategory;
import com.tomtop.entity.HomeFeaturedCategoryContent;
import com.tomtop.entity.HomeFeaturedCategorySku;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.mappers.mysql.HomeFeaturedCategoryBannerMapper;
import com.tomtop.mappers.mysql.HomeFeaturedCategoryKeyMapper;
import com.tomtop.mappers.mysql.HomeFeaturedCategoryMapper;
import com.tomtop.mappers.mysql.HomeFeaturedCategorySkuMapper;
import com.tomtop.services.ICategoryService;
import com.tomtop.services.IHomeFeaturedCategoryService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

/**
 * 首页获取每日精选品类及商品
 * 
 * @author liulj
 *
 */
@Service
public class HomeFeaturedCategoryServiceImpl extends BaseService implements
		IHomeFeaturedCategoryService {
	
	Logger logger = Logger.getLogger(HomeFeaturedCategoryServiceImpl.class);
	
	@Autowired
	HomeFeaturedCategoryMapper homeFeaturedCategoryMapper;
	@Autowired
	HomeFeaturedCategoryBannerMapper homeFeaturedCategoryBannerMapper;
	@Autowired
	HomeFeaturedCategoryKeyMapper homeFeaturedCategoryKeyMapper;
	@Autowired
	HomeFeaturedCategorySkuMapper homeFeaturedCategorySkuMapper;
	@Autowired
	ICategoryService categoryService;
	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	/**
	 * 获取精选品类
	 * 
	 * @param client
	 *            客户端Id
	 * @param lang
	 * 			   语言Id
	 * @param website 
	 * 			  站点
	 * @return
	 */
	@Cacheable(value="fcategory", keyGenerator = "customKeyGenerator")
	@Override
	public List<HomeFeaturedCategory> getHomeFeaturedCategory(int client,int lang,int website) {
		logger.info("================create redis getHomeFeaturedCategory================");
		List<HomeFeaturedCategory> dto = homeFeaturedCategoryMapper.getListClientLangua(client, lang);
		if (dto == null || dto.isEmpty()) {
			if(lang > CommonDefn.ONE){
				dto =  homeFeaturedCategoryMapper.getListClientLangua(client, lang);
			}
		}
		if (dto != null && !dto.isEmpty()) {
			HashMap<Integer,String> cmap = categoryService.getCategoryIdName(lang,website);
			for (int i = 0; i < dto.size(); i++) {
				HomeFeaturedCategory hfc = dto.get(i);
				hfc.setName(cmap.get(hfc.getCategoryId()));
				dto.set(i, hfc);
			}
		}
		return dto;
	}
	
	/**
	 * 获取精选品类的商品
	 *  @param categoryId
	 *            品类Id
	 * @param client
	 *            客户端Id
	 * @param lang
	 * 			   语言Id
	 * @param website 
	 * 			  站点
	 * @param currency
	 *          币种
	 * @return
	 */
	@Cacheable(value="fcategory", keyGenerator = "customKeyGenerator")
	@Override
	public HomeFeaturedCategoryContent getHomeFeaturedCategoryContent(int categoryId,int client,int lang,int website,String currency){
		logger.info("================create redis getHomeFeaturedCategoryContent================");
		HomeFeaturedCategoryContent hfcc = new HomeFeaturedCategoryContent();
		hfcc.setBanners(homeFeaturedCategoryBannerMapper.getListByFcategoryClientLangua(categoryId, client, lang));
		hfcc.setKeys(homeFeaturedCategoryKeyMapper.getListByFcategoryClientLangua(categoryId, client, lang));
		List<String> listingIds = homeFeaturedCategorySkuMapper.getListByFcategoryClientLangua(categoryId, client, lang);
		List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
		if(ieList != null && ieList.size() > 0){
			Currency currencyBean = this.getCurrencyBean(currency); 
			List<HomeFeaturedCategorySku> hfcskuList = new ArrayList<HomeFeaturedCategorySku>();
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				HomeFeaturedCategorySku hfcsku = new HomeFeaturedCategorySku();
				//通过公共的方法设置父类的属性
				productPublicUtil.transformProductBase(hfcsku, ie, currencyBean);
				hfcsku.setSort(i+1);
				hfcskuList.add(hfcsku);
			}
			hfcc.setSkus(hfcskuList);
		}
		return hfcc;
	}
	
	/**
	 * 获取精选品类的商品
	 *  @param categoryId
	 *            品类Id
	 * @param client
	 *            客户端Id
	 * @param lang
	 * 			   语言Id
	 * @param website 
	 * 			  站点
	 * @param currency
	 *          币种
	 * @return
	 */
	@Cacheable(value="fcategory", keyGenerator = "customKeyGenerator")
	@Override
	public HomeFeaturedCategoryContent getHomeFeaturedCategoryContent(int categoryId,int client,int lang,int website,String currency,String depotName){
		logger.info("================create redis getHomeFeaturedCategoryContent================");
		HomeFeaturedCategoryContent hfcc = new HomeFeaturedCategoryContent();
		hfcc.setBanners(homeFeaturedCategoryBannerMapper.getListByFcategoryClientLangua(categoryId, client, lang));
		hfcc.setKeys(homeFeaturedCategoryKeyMapper.getListByFcategoryClientLangua(categoryId, client, lang));
		List<String> listingIds = homeFeaturedCategorySkuMapper.getListByFcategoryClientLangua(categoryId, client, lang);
		List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
		if(ieList != null && ieList.size() > 0){
			Currency currencyBean = this.getCurrencyBean(currency); 
			List<HomeFeaturedCategorySku> hfcskuList = new ArrayList<HomeFeaturedCategorySku>();
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				HomeFeaturedCategorySku hfcsku = new HomeFeaturedCategorySku();
				//通过公共的方法设置父类的属性
				productPublicUtil.transformProductBase(hfcsku, ie, currencyBean,depotName);
				hfcsku.setSort(i+1);
				hfcskuList.add(hfcsku);
			}
			hfcc.setSkus(hfcskuList);
		}
		return hfcc;
	}
}
