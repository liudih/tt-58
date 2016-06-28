package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.Currency;
import com.tomtop.entity.HomeRecentOrders;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.mappers.mysql.HomeRecentOrdersCountryMapper;
import com.tomtop.mappers.mysql.HomeRecentOrdersSkuMapper;
import com.tomtop.services.IHomeRecentService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

/**
 * 首页获取recent的商品
 * 
 * @author liulj
 *
 */
@Service
public class HomeRecentServiceImpl extends BaseService implements IHomeRecentService {
	
	Logger logger = Logger.getLogger(HomeRecentServiceImpl.class);
	
	@Autowired
	HomeRecentOrdersSkuMapper recentSkuMapper;
	@Autowired
	HomeRecentOrdersCountryMapper recentCountryMapper;
	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	public List<String> getRecentList(int client, int lang) {
		logger.info("================create redis getRecentList================");
		List<String> dto = recentSkuMapper.getListByClientLang(client, lang);
		if (dto == null || dto.isEmpty()) {
			if(lang > CommonDefn.ONE){
				dto = recentSkuMapper.getListByClientLang(client, CommonDefn.ONE);
			}
		}
		return dto;
	}
	
	public List<String> getRecentCountryList(int client, int lang) {
		logger.info("================create redis getRecentCountryList================");
		List<String> dto = recentCountryMapper.getCountryNameListByClientLang(client, lang);
		if (dto == null || dto.isEmpty()) {
			if(lang > CommonDefn.ONE){
				dto = recentCountryMapper.getCountryNameListByClientLang(client, CommonDefn.ONE);
			}
		}
		return dto;
	}
	
	@Cacheable(value="recent_orders", keyGenerator = "customKeyGenerator", cacheManager = "cacheManager")
	@Override
	public List<HomeRecentOrders> getHomeRecentOrders(Integer lang,
			Integer client, Integer website, String currency) {
		List<String> listingIds = this.getRecentList(client, lang);
		List<String> cnlist = this.getRecentCountryList(client, lang);
		List<HomeRecentOrders> hrolist = new ArrayList<HomeRecentOrders>();
		if(listingIds != null && cnlist != null && listingIds.size() > 0 && cnlist.size() > 0){
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
			if(ieList != null && ieList.size() > 0){
				Currency currencyBean = this.getCurrencyBean(currency); 
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					HomeRecentOrders hro = new HomeRecentOrders();
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(hro, ie, currencyBean);
					hro.setCountry(cnlist.get(RandomUtils.nextInt(cnlist.size())));
					hrolist.add(hro);
				}
			}
		}
		
		return hrolist;
	}
	
	@Cacheable(value="recent_orders", keyGenerator = "customKeyGenerator", cacheManager = "cacheManager")
	@Override
	public List<HomeRecentOrders> getHomeRecentOrders(Integer lang,
			Integer client, Integer website, String currency,String depotName) {
		List<String> listingIds = this.getRecentList(client, lang);
		List<String> cnlist = this.getRecentCountryList(client, lang);
		List<HomeRecentOrders> hrolist = new ArrayList<HomeRecentOrders>();
		if(listingIds != null && cnlist != null && listingIds.size() > 0 && cnlist.size() > 0){
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
			if(ieList != null && ieList.size() > 0){
				Currency currencyBean = this.getCurrencyBean(currency); 
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					HomeRecentOrders hro = new HomeRecentOrders();
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(hro, ie, currencyBean,depotName);
					hro.setCountry(cnlist.get(RandomUtils.nextInt(cnlist.size())));
					hrolist.add(hro);
				}
			}
		}
		
		return hrolist;
	}
}
