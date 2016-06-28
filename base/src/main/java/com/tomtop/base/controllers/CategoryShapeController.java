package com.tomtop.base.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tomtop.base.service.ICategoryShapeService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 品类对应的形态操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/shape")
public class CategoryShapeController {

	@Autowired
	ICategoryShapeService categoryShapeService;
	
	/**
	 * 获取品类对应的形态信息
	 * 
	 * @param lang
	 *        语言Id
	 * @param client
	 *         客户端Id
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "shape", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/map")
	public Result getClientByName(
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client){
		Result res = new Result();
		Map<String,Integer> cmap = categoryShapeService.getCategoryShapeType(client);
		if (cmap != null && cmap.size() > 0) {
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cmap);
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
}
