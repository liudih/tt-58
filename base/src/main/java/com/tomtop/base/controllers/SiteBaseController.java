package com.tomtop.base.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.SiteBo;
import com.tomtop.base.models.dto.SiteBase;
import com.tomtop.base.service.ISiteBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 站点信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/site")
public class SiteBaseController {

	@Autowired
	ISiteBaseService siteBaseService;
	
	/**
	 * 获取所有站点信息
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "site", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getSite(){
		Result res = new Result();
		List<SiteBo> sblist = siteBaseService.getSiteList();
		if(sblist != null && sblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(sblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
		return res;
	}
	
	/**
	 * 获取站点信息
	 * @param id
	 *        主键Id
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "site", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/{id}")
	public Result getSiteById(@PathVariable Integer id){
		Result res = new Result();
		SiteBo sb = siteBaseService.getSiteById(id);
		if(sb.getRes() != CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		sb.setRes(null);
		res.setData(sb);
		
		return res;
	}
	
	/**
	 * 获取站点信息
	 * @param name
	 *        站点名称
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "site", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/name/{name:.+}")
	public Result getSiteByName(@PathVariable String name){
		Result res = new Result();
		SiteBase sb = new SiteBase();
		sb.setName(name);
		List<SiteBo> sblist = siteBaseService.getSiteList(sb);
		if(sblist != null && sblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(sblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
}
