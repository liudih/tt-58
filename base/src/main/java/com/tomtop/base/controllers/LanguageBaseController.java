package com.tomtop.base.controllers;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.LanguageBo;
import com.tomtop.base.models.dto.LanguageBase;
import com.tomtop.base.service.ILanguageBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 语言信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/language")
public class LanguageBaseController {

	@Autowired
	ILanguageBaseService languageBaseService;
	
	/**
	 * 获取所有语言信息
	 * 
	 * @return BaseResult
	 */
	@PostConstruct
	@Cacheable(value = "language", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getLanguage(){
		Result res = new Result();
		List<LanguageBo> lblist = languageBaseService.getLanguageList();
		if(lblist != null && lblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(lblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 获取语言信息
	 * @param id
	 *        主键Id
	 *        
	 * @return BaseResult
	 */
	@ResponseBody
	@Cacheable(value = "language", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/{id}")
	public Result getLanguageById(@PathVariable Integer id){
		Result res = new Result();
		LanguageBo lb = languageBaseService.getLanguageById(id);
		if(lb.getRes() != CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		lb.setRes(null);
		res.setData(lb);
		
		return res;
	}
	
	/**
	 * 获取语言信息
	 * 
	 * @param name
	 *        语言名称
	 * @param code
	 *        语言代码
	 *        
	 * @return BaseResult
	 */
	@ResponseBody
	@Cacheable(value = "language", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/where")
	public Result getLanguageByWhere(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "code", required = false) String code){
		Result res = new Result();
		LanguageBase lb = new LanguageBase();
		if(name != null && !"".equals(name)){
			lb.setName(name);
		}
		if(code != null && !"".equals(code)){
			lb.setCode(code);
		}
		List<LanguageBo> lblist = languageBaseService.getLanguageList(lb);
		if(lblist != null && lblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(lblist.toArray());
			return res;
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
	}
}
