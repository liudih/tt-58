package com.tomtop.controllers.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tomtop.utils.Result;

/**
 * 缓存控制类
 * 
 * @author shuliangxing
 *
 * @date 2016年6月6日 下午6:25:25
 */
@Controller
@RequestMapping("/cache")
public class CacheManageController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 清产品信息缓存
	 * 
	 * @return
	 */
	@ResponseBody
	@CacheEvict(value = "api_search_product", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/v1/clean/api_search_product", method = RequestMethod.GET)
	public Result cleanClient() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清除单个产品缓存
	 * 
	 * @return
	 */
	@ResponseBody
	@CacheEvict(value = "api_search_product", key="#listingId+'/'+#lang+'/'+#website+'/'+#storage")
	@RequestMapping(value = "/v1/clean/api_search_product/{listingId}/{lang}/{website}/{storage}")
	public Result clean(@PathVariable("listingId") String listingId,
			@PathVariable("lang") String lang, @PathVariable("website")String website,
			@PathVariable("storage")String storage) {
		return new Result(Result.SUCCESS, null);
	}
}
