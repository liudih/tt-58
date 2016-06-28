package com.tomtop.controllers;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.ShoppingCartProduct;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IProductBaseService;

/**
 * 获取购物车产品信息
 * 
 * @author liulj
 *
 */
@RestController
public class ShoppingCartProductController {


	@Autowired
	IProductBaseService productBaseService;
	
	/**
	 * 获取产品list
	 * 
	 * @param listings
	 * @param lang
	 * @param client
	 * @param website
	 * @param currency
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/product/shoppingCart", method = RequestMethod.GET)
	public Result getProducts(
			@RequestParam(value = "listings") String listings,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		if (StringUtils.isNotBlank(listings)&& StringUtils.isNotBlank(currency)) {
			List<ShoppingCartProduct> scpList = productBaseService.getProductShopping(listings, lang, website, currency);
			return new Result(Result.SUCCESS, scpList);
		}
		return new Result(Result.FAIL, null);
	}
	
	/**
	 * 获取产品list (第二版)
	 * 
	 * @param listings
	 * @param lang
	 * @param client
	 * @param website
	 * @param currency
	 * @param depotName
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/product/shoppingCart", method = RequestMethod.GET)
	public Result getProductsV2(
			@RequestParam(value = "listings") String listings,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName) {
		if (StringUtils.isNotBlank(listings)&& StringUtils.isNotBlank(currency)) {
			Map<Integer,List<ShoppingCartProduct>> scpMap = productBaseService.getProductShopping(listings, lang, website, currency,depotName);
			return new Result(Result.SUCCESS, scpMap);
		}
		return new Result(Result.FAIL, null);
	}
}
