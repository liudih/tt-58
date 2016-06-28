package com.tomtop.base.controllers;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.tomtop.base.models.vo.BaseLabelAttributeVo;
import com.tomtop.base.service.IBaseLabelAttributeService;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 属性标签
 * 
 * @author liulj
 *
 */
@RestController
public class BaseLabelAttributeController {

	@Autowired
	private IBaseLabelAttributeService service;

	@Cacheable(value = "labelAttribute", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/base/labelAttribute/v1", method = RequestMethod.GET)
	public Result getListByClientLang(@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client) {
		return new Result(Result.SUCCESS, Lists.transform(
				service.getListByClientLang(client, lang),
				p -> BeanUtils.mapFromClass(p, BaseLabelAttributeVo.class))
				.toArray());
	}

	@Cacheable(value = "labelAttribute", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/base/labelAttribute/v1/getListByCategoryId/{categoryId}", method = RequestMethod.GET)
	public Result getListByCategoryId(@PathVariable("categoryId") int categoryId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client) {
		return new Result(
				Result.SUCCESS,
				Lists.transform(
						service.getListByCategoryId(categoryId,client,lang),
						p -> BeanUtils.mapFromClass(p,
								BaseLabelAttributeVo.class))
						.stream()
						.collect(
								Collectors
										.groupingBy(BaseLabelAttributeVo::getAttributeKey)));
	}

	@Cacheable(value = "labelAttribute", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/base/labelAttribute/v1/getListByKey/{key}", method = RequestMethod.GET)
	public Result getListByKey(@PathVariable("key") String key,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client) {
		if (StringUtils.isNoneBlank(key)) {
			return new Result(
					Result.SUCCESS,
					Lists.transform(
							service.getListByKey(key,client,lang),
							p -> BeanUtils.mapFromClass(p,
									BaseLabelAttributeVo.class))
							.stream()
							.collect(
									Collectors
											.groupingBy(BaseLabelAttributeVo::getCategoryId)));
		}
		return new Result(Result.FAIL, null);
	}
}
