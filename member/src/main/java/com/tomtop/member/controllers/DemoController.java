package com.tomtop.member.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这个类是给运维测试用的
 */
@RestController
@RequestMapping("/")
public class DemoController{

//	@Autowired
//	private GlobalConfigSettings setting;
	
	@RequestMapping("test")
	public String test() {
		return "test";
	}
	
	@RequestMapping()
	public String demo() {
		//setting.getUrlByKey(1);
		return "this is member";
	}
	
}
