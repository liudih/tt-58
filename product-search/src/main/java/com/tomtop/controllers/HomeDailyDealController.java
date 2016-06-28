package com.tomtop.controllers;


import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.HomeDailyDeal;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IHomeDailyDealService;
import com.tomtop.utils.FormatDateUtils;

/**
 * 每日撕推荐action
 * 
 * @author liulj
 *
 */
@RestController
public class HomeDailyDealController {

	@Autowired
	private IHomeDailyDealService homeDailyDealService;


	/**
	 * 获取每日推荐的产品
	 * 
	 * @param date
	 *            开始时间
	 * @param client
	 * @param website
	 * @param language
	 * @param currency
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/dailyDeal", method = RequestMethod.GET)
	public Result getListByStartDate(
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="website", required = false, defaultValue = "1") int website,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam("date") String date) {
		try{
			List<HomeDailyDeal> hddList = homeDailyDealService.getHomeDailyDealList(DateUtils.parseDate(date, FormatDateUtils.YMD), client, lang, website, currency);
			if(hddList != null && hddList.size() > 0){
				return new Result(Result.SUCCESS, hddList);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new Result(Result.FAIL, null);
	}
	
	/**
	 * 获取每日推荐的产品 V2
	 * 
	 * @param date
	 *            开始时间
	 * @param client
	 * @param website
	 * @param language
	 * @param currency
	 * @param depotName
	 * @return
	 */
	@RequestMapping(value = "/ic/v2/home/dailyDeal", method = RequestMethod.GET)
	public Result getListByStartDateV2(
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="website", required = false, defaultValue = "1") int website,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang,
			@RequestParam(value="currency",required = false, defaultValue = "USD") String currency,
			@RequestParam(value="depotName",required = false, defaultValue = "CN") String depotName,
			@RequestParam("date") String date) {
		try{
			List<HomeDailyDeal> hddList = homeDailyDealService.getHomeDailyDealList(DateUtils.parseDate(date, FormatDateUtils.YMD),depotName, client, lang, website, currency);
			if(hddList != null && hddList.size() > 0){
				return new Result(Result.SUCCESS, hddList);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new Result(Result.FAIL, null);
	}
}
