package com.tomtop.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IBaseInfoService;
import com.tomtop.services.IBaseService;
import com.tomtop.services.INewArrivalsService;
import com.tomtop.services.IPriceRangPolyService;
import com.tomtop.services.impl.BaseInfoServiceImpl;
import com.tomtop.services.impl.PriceRangPolyServiceImpl;

/**
 * 控制缓存
 * 
 * @author liulj
 *
 */
@RestController
public class CacheManageController {

	//private static Logger logger = Logger.getLogger(CacheManageController.class);
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	IBaseInfoService baseInfoService;
	@Autowired
	IBaseService baseService;
	@Autowired
	INewArrivalsService newArrivalsService;
	@Autowired
	IPriceRangPolyService priceRangePolyService;
	
	/**
	 *  清除base_layout的布局信息缓存
	 * 
	 */
	@CacheEvict(value = "base_layout", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/base_layout/clean", method = RequestMethod.GET)
	public Result cleanbaseLayout() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除product_category的缓存
	 * 
	 */
	@CacheEvict(value = "product_category", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/ic/v1/cache/product_category/clean", method = RequestMethod.GET)
	public Result cleanProductCategory() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除首页品牌缓存
	 * 
	 */
	@CacheEvict(value = "product_brand", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_brand/clean", method = RequestMethod.GET)
	public Result cleanHomeBrand() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除daily_deal缓存
	 * 
	 */
	@CacheEvict(value = "daily_deal", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/daily_deal/clean", method = RequestMethod.GET)
	public Result cleanHomeDaily() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除fcategory缓存
	 * 
	 */
	@CacheEvict(value = "fcategory", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/fcategory/clean", method = RequestMethod.GET)
	public Result cleanFcategory() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除首页Customers Voices模块
	 * @return
	 */
	@CacheEvict(value = "customers_voices", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/customers_voices/clean", method = RequestMethod.GET)
	public Result cleanHomeNewest() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除首页Recent Orders模块
	 * @return
	 */
	@CacheEvict(value = "recent_orders", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/recent_orders/clean", method = RequestMethod.GET)
	public Result cleanRecentOrders() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除首页关键字
	 * @return
	 */
	@CacheEvict(value = "home_keyword", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/home_keyword/clean", method = RequestMethod.GET)
	public Result cleanHomerecentOrdersCountry() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除首页 TOP-SELLERS、 NEW-ARRIVALS、MOREITEM-TO-CONSIDER
	 * @return
	 */
	@CacheEvict(value = "base_layout_module_content", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/base_layout_module_content/clean", method = RequestMethod.GET)
	public Result cleanBaseLayoutModuleContent() {
		return new Result(Result.SUCCESS, null);
	}
	/**
	 * 清除explain
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_explain", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_explain/clean", method = RequestMethod.GET)
	public Result cleanProductExplain() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除CategoryPath
	 * 
	 * @return
	 */
	@CacheEvict(value = "category_path", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/category_path/clean", method = RequestMethod.GET)
	public Result cleanCategoryPath() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除热门缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_hot", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_hot/clean", method = RequestMethod.GET)
	public Result cleanHot() {
		return new Result(Result.SUCCESS, null);
	}
	/**
	 * 清除商品详情对应的评论详情缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_review", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_review/clean", method = RequestMethod.GET)
	public Result cleanProductStorageShipping() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除商品详情 商品专题的缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "product_topic", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/product_topic/clean", method = RequestMethod.GET)
	public Result cleanProductTopic() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清除website 所有List
	 * 
	 * @return
	 */
	@CacheEvict(value = "website", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/website/clean", method = RequestMethod.GET)
	public Result cleanWebsite() {
		return new Result(Result.SUCCESS, null);
	}
	
	
	/*@CacheEvict(value = "new_agg", allEntries = true)
	@RequestMapping(value = "/ic/v1/cache/new_agg/clean", method = RequestMethod.GET)
	public Result cleanNewAgg() {
		List<Integer> siteList = baseService.getAllWebsiteId();
		for (Integer site : siteList) {
			String url = "http://localhost:9001/ic/v1/product/new/agg?website=" + site;
			HttpClientUtil.doGet(url);
			logger.info("link  = " + url );
		}
		
		return new Result(Result.SUCCESS, null);
	}*/
	/**
	 * 清理价格内存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/cache/priceRange/clean", method = RequestMethod.GET)
	public Result cleanPriceRange(){
		PriceRangPolyServiceImpl.priceRangeMap = priceRangePolyService.getPriceRangeMap();
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清商品所有的缓存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/cache/clean", method = RequestMethod.GET)
	public Result cleanAll() {
		redisTemplate.execute(new RedisCallback<Integer>() {
			@Override
			public Integer doInRedis(RedisConnection redisconnection)
					throws DataAccessException {
				redisconnection.flushDb();
				return 1;
			}
		});
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 更新货币缓存
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/cache/baseCurrency/clean", method = RequestMethod.GET)
	public Result cleanbaseCurrency() {
		BaseInfoServiceImpl.currencyMap = baseInfoService.getAllRoteCurrency();
		return new Result(Result.SUCCESS, null);
	}
	/**
	 * 更新语言缓存
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/cache/baseLangage/clean", method = RequestMethod.GET)
	public Result cleanbaseLang() {
		BaseInfoServiceImpl.langageMap = baseInfoService.getAllRoteLangage();
		BaseInfoServiceImpl.langageStrMap = baseInfoService.getAllStrRoteLangage();
		return new Result(Result.SUCCESS, null);
	}
	/**
	 * 清base缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "base", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/ic/v1/cache/base/clean", method = RequestMethod.GET)
	public Result cleanBase() {
		BaseInfoServiceImpl.currencyMap = baseInfoService.getAllRoteCurrency();
		BaseInfoServiceImpl.langageMap = baseInfoService.getAllRoteLangage();
		BaseInfoServiceImpl.langageStrMap = baseInfoService.getAllStrRoteLangage();
		return new Result(Result.SUCCESS, null);
	}

}
