package com.tomtop.controllers;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.BaseLayout;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IBaseLayoutService;

/**
 * 布局
 * 
 * @author liulj
 *
 */
@RestController
public class BaseLayoutController {

	@Autowired
	private IBaseLayoutService baseLayoutService;

	/**
	 * 获取布局信息
	 * 
	 * @param code
	 * @param client 客户端Id
	 * @param lang  语言Id
	 * 
	 * @author renyy
	 */
	@Cacheable(value="base_layout", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/base/layout", method = RequestMethod.GET)
	public Result getListByCode(@RequestParam("code") String code,
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		Result res = new Result();
		if(StringUtils.isNotBlank(code)){
			List<BaseLayout> blist = baseLayoutService.getBaseLayoutByCode(code, client, lang);
			if(blist != null && blist.size() > 0){
				res.setRet(Result.SUCCESS);
				res.setData(blist);
			}else{
				res.setRet(Result.FAIL);
				res.setErrMsg("base layout list not find");
			}
		}else{
			res.setRet(Result.FAIL);
			res.setErrMsg("code is null");
		}
		return res;
	}
}
