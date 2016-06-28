package com.tomtop.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;

@RestController
public class HashCodeController {
	
	/**
	 * 邮箱获取收藏的商品数
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/hashcode/{listingId}", method = RequestMethod.GET)
	public Result hashCode(@PathVariable("listingId") String listingId) {
		return new Result(Result.SUCCESS, listingId.hashCode());
	}
}
