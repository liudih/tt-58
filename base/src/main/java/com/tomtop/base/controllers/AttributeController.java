package com.tomtop.base.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.service.IProductAttributeService;
import com.tomtop.framework.core.utils.Result;

@RestController
public class AttributeController {

	@Autowired
	IProductAttributeService productAttributeService;
	
	@Cacheable(value = "productAttribute", keyGenerator = "customKeyGenerator",cacheManager = "dayCacheManager"  )
	@RequestMapping(value = "/base/productAttribute/v1", method = RequestMethod.GET)
	public Result getProductAttributeByLang(@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang) {
		HashMap<String, String> attMap = productAttributeService.getProductAttributeMap(lang);
		return new Result(1,attMap);
	}
	
	@Cacheable(value = "productAttributeKeyList", keyGenerator = "customKeyGenerator",cacheManager = "dayCacheManager"  )
	@RequestMapping(value = "/base/attribute/keyList/v1", method = RequestMethod.GET)
	public Result getProductAttributeKeyListByLang(@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang) {
		HashMap<String, List<String>> attMap = productAttributeService.getProductAttributeKeyList(lang);
		return new Result(1,attMap);
	}
}
