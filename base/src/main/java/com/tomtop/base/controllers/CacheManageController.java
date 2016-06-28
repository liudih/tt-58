package com.tomtop.base.controllers;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tomtop.framework.core.utils.Result;

/**
 * 控制缓存
 * 
 * @author renyy
 *
 */
@RestController
public class CacheManageController {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Value("${cleanCurrency}")
	private String cleanCurrency;
	@Value("${cleanLangage}")
	private String cleanLangage;

	@Resource(name = "restTemplate")
	private RestTemplate restTemplate;
	
	/**
	 * 清client缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "client", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/client", method = RequestMethod.GET)
	public Result cleanClient() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清base_label_attribute缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "labelAttribute", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/labelAttribute", method = RequestMethod.GET)
	public Result cleanBaseLabelAttribute() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清country缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "country", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/country", method = RequestMethod.GET)
	public Result cleanCountry() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清ShippingMethod缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "ShippingMethod", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/shippingMethod", method = RequestMethod.GET)
	public Result cleanShippingMethod() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清currency缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "currency", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/currency", method = RequestMethod.GET)
	public Result cleanCurrency() {
		String msg = restTemplate.getForObject(cleanCurrency, String.class);
		return new Result(Result.SUCCESS, msg);
	}

	/**
	 * 清language缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "language", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/language", method = RequestMethod.GET)
	public Result cleanLanguage() {
		String msg = restTemplate.getForObject(cleanLangage, String.class);
		return new Result(Result.SUCCESS, msg);
	}

	/**
	 * 清parameter缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "parameter", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/parameter", method = RequestMethod.GET)
	public Result cleanParameter() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清resource缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "resource", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/resource", method = RequestMethod.GET)
	public Result cleanResource() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清site缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "site", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/site", method = RequestMethod.GET)
	public Result cleanBase() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清vhost缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "vhost", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/vhost", method = RequestMethod.GET)
	public Result cleanVhost() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清productAttribute缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "productAttribute", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/productAttribute", method = RequestMethod.GET)
	public Result cleanProductAttribute() {
		return new Result(Result.SUCCESS, null);
	}
	
	/**
	 * 清cms缓存
	 * 
	 * @return
	 */
	@CacheEvict(value = "cms", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/cache/v1/clean/cms", method = RequestMethod.GET)
	public Result cleanCms() {
		return new Result(Result.SUCCESS, null);
	}

	/**
	 * 清所有的缓存
	 * 
	 * @return
	 */
	@RequestMapping(value = "/cache/v1/clean", method = RequestMethod.GET)
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
}
