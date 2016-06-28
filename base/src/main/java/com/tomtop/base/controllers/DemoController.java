package com.tomtop.base.controllers;

import java.util.Map;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

/**
 * spring boot mvc demo
 */
@RestController
@RequestMapping("/")
public class DemoController{

	
	MapperFactoryBean<?> a;

	MapperScannerConfigurer b;
	
	DataSourceAutoConfiguration c;
	
	@RequestMapping
	public Map<String,String> index() {
		Map<String,String> map = Maps.newLinkedHashMap();
		map.put("base", "This is Spring Base poject");
		return map;
	}

}
