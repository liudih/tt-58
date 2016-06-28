package com.tomtop.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.HomeBrand;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.IHomeBrandService;

/**
 * 首页品牌
 * 
 * @author liulj
 *
 */
@RestController
public class HomeBrandController {

	@Autowired
	IHomeBrandService homeBrandService;

	/**
	 * 获取List,根具语言和客户端
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/ic/v1/home/brand", method = RequestMethod.GET)
	public Result getListByClientLang(
			@RequestParam(value="client", required = false, defaultValue = "1") int client,
			@RequestParam(value="lang", required = false, defaultValue = "1") int lang) {
		Result res = new Result();
		List<HomeBrand> hbvo = homeBrandService.getHomeBrand(client, lang);
		if(hbvo != null && hbvo.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(hbvo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrMsg("brand is not find");
		}
		
		return res;
	}
}
