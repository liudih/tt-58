package com.tomtop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.HomeNewest;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IHomeNewestService;

/**
 * 首页 图片、评论、视频 三个接口合一
 * 
 * @author renyy
 *
 */
@RestController
public class HomeNewestController {

	
	@Autowired
	IHomeNewestService homeNewestService;

	/**
	 * 获取首页Customers Voices
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	
	@RequestMapping(value = "/ic/v1/customers/voices", method = RequestMethod.GET)
	public Result getCustomersVoices(
			@RequestParam(value="client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value="website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value="lang", required = false, defaultValue = "1") Integer lang) {
		HomeNewest hnvo = homeNewestService.getCustomersVoices(client, lang, website);
		if(hnvo == null){
			return new Result(Result.FAIL, "Customers Voices not find");
		}
		return new Result(Result.SUCCESS, hnvo);
	}
}
