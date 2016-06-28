package com.tomtop.services.impl;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.entity.HomeBrand;
import com.tomtop.mappers.mysql.HomeBrandMapper;
import com.tomtop.services.IHomeBrandService;
import com.tomtop.utils.CommonDefn;


/**
 * 首页品牌管理
 * 
 * @author liulj
 *
 */
@Service
public class HomeBrandServiceImpl implements IHomeBrandService {

	Logger logger = Logger.getLogger(HomeFeaturedCategoryServiceImpl.class);
	
	@Autowired
	HomeBrandMapper homeBrandMappr;
	
	/**
	 * 获取品牌列表
	 * 
	 * @param client
	 * @param lang
	 * 
	 * @return
	 */
	@Cacheable(value="product_brand", keyGenerator="customKeyGenerator", cacheManager="dayCacheManager")
	@Override
	public List<HomeBrand> getHomeBrand(Integer client, Integer lang) {
		logger.info("================create redis getHomeBrand================");
		List<HomeBrand> hbvoList = homeBrandMappr.getHomeTopBrand(client, lang);
		if(hbvoList == null || hbvoList.size() == 0){
			if(lang > CommonDefn.ONE){
				hbvoList = homeBrandMappr.getHomeTopBrand(client, CommonDefn.ONE);
			}
		}
		
		return hbvoList;
	}

}