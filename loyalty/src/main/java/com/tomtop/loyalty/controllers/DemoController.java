package com.tomtop.loyalty.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

/**
 * spring boot mvc demo
 */
@RestController
@RequestMapping("/")
public class DemoController{

	@RequestMapping()
	public Map<String,String> index() {
		Map<String,String> map = Maps.newLinkedHashMap();
		map.put("loyalty", "Welcome to spring loyalty project");
		return map;
	}
}
