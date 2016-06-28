package com.tomtop.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IProductRecommendService;
import com.tomtop.utils.CommonDefn;

/**
 * 你可能喜欢的产品
 * 
 * @author liulj
 *
 */
@RestController
public class YouMayLikeProduct {
	
	@Autowired
	private IProductRecommendService productRecommendService;
	
	/**
	 * 也查看的产品 AlsoViewed (第二版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/product/alsoViewed", method = RequestMethod.GET)
	public Result getYouAlsoViewedProucts2(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "website", required = false, defaultValue = "1") int website, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value = "size", required = false, defaultValue = "25") int size) {
		return new Result(Result.SUCCESS, productRecommendService.getRecommendProduct(listingId, website, lang, currency, size, CommonDefn.VIEWED,null));
	}
	
	/**
	 * 也买过的产品 AlsoBought (第二版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/product/alsoBought", method = RequestMethod.GET)
	public Result getYouAlsoBoughtProucts2(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "website", required = false, defaultValue = "1") int website, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value = "size", required = false, defaultValue = "25") int size) {
		return new Result(Result.SUCCESS, productRecommendService.getRecommendProduct(listingId, website, lang, currency, size, CommonDefn.BOUGHT,null));
	}
	
	/**
	 * 可能喜欢的产品youMayLike Recommend (第二版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/product/youMayLike", method = RequestMethod.GET)
	public Result getYouMayLikeProucts2(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "website", required = false, defaultValue = "1") int website, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value = "size", required = false, defaultValue = "25") int size) {
		return new Result(Result.SUCCESS, productRecommendService.getRecommendProduct(listingId, website, lang, currency, size, CommonDefn.RECOMMEND,null));
	}
	
	/**
	 * 也查看的产品 AlsoViewed (第三版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v3/product/alsoViewed", method = RequestMethod.GET)
	public Result getYouAlsoViewedProucts3(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "website", required = false, defaultValue = "1") int website, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value = "size", required = false, defaultValue = "25") int size,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName) {
		return new Result(Result.SUCCESS, productRecommendService.getRecommendProduct(listingId, website, lang, currency, size, CommonDefn.VIEWED,depotName));
	}
	
	/**
	 * 也买过的产品 AlsoBought (第三版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v3/product/alsoBought", method = RequestMethod.GET)
	public Result getYouAlsoBoughtProucts3(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "website", required = false, defaultValue = "1") int website, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value = "size", required = false, defaultValue = "25") int size,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName) {
		return new Result(Result.SUCCESS, productRecommendService.getRecommendProduct(listingId, website, lang, currency, size, CommonDefn.BOUGHT,depotName));
	}
	
	/**
	 * 可能喜欢的产品youMayLike Recommend (第三版)
	 * 
	 * @param listingId
	 * @param currency
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v3/product/youMayLike", method = RequestMethod.GET)
	public Result getYouMayLikeProucts3(
			@RequestParam("listingId") String listingId,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "client", required = false, defaultValue = "1") int client, 
			@RequestParam(value = "website", required = false, defaultValue = "1") int website, 
			@RequestParam(value = "lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value = "size", required = false, defaultValue = "25") int size,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName) {
		return new Result(Result.SUCCESS, productRecommendService.getRecommendProduct(listingId, website, lang, currency, size, CommonDefn.RECOMMEND,depotName));
	}
}
