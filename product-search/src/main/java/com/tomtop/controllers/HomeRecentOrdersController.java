package com.tomtop.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.HomeRecentOrders;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IHomeRecentService;

/**
 * 获取首页最近订单
 * 
 * @author liulj
 * 
 */
@RestController
public class HomeRecentOrdersController {

	@Autowired
	IHomeRecentService recentService;

	/**
	 * 获取首页最近订单接口
	 * 
	 * @param client
	 * @param lang
	 * @param currency
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/recent_orders", method = RequestMethod.GET)
	public Result getInfo(@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency) {
		List<HomeRecentOrders> hroList =  recentService.getHomeRecentOrders(lang, client, website, currency);
		if(hroList != null && hroList.size() > 0){
			return new Result(Result.SUCCESS, hroList);
		}
		
		return new Result(Result.FAIL, null);
	}
	
	/**
	 * 获取首页最近订单接口
	 * 
	 * @param client
	 * @param website
	 * @param lang
	 * @param currency
	 * @param depotName
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/home/recent_orders", method = RequestMethod.GET)
	public Result getRecentOrdersV2(@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName) {
		List<HomeRecentOrders> hroList =  recentService.getHomeRecentOrders(lang, client, website, currency,depotName);
		if(hroList != null && hroList.size() > 0){
			return new Result(Result.SUCCESS, hroList);
		}
		
		return new Result(Result.FAIL, null);
	}
}
