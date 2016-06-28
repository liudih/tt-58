package com.tomtop.base.controllers;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.dto.ContentCatalogue;
import com.tomtop.base.models.dto.ContentDeails;
import com.tomtop.base.service.IContentCatalogueService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 事件服务信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/cms")
public class CmsController {

	@Autowired
	IContentCatalogueService cclService;
	
	/**
	 * 获取cms信息List
	 * @param name
	 * @param lang
	 * @param client
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "cms", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/list")
	public Result getContentCatalogue(@RequestParam(value = "name", required = false, defaultValue = "Footer") String name,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client){
		Result res = new Result();
		List<ContentCatalogue> cclist = cclService.getContentCatalogue(name, client, lang);
		if(cclist != null && cclist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cclist);
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 获取cms信息map 2级 -> 3级
	 * @param name
	 * @param lang
	 * @param client
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "cms", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/map")
	public Result getContentMap(@RequestParam(value = "name", required = false, defaultValue = "Footer") String name,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client){
		Result res = new Result();
		LinkedHashMap<String, List<ContentCatalogue>> cclinkMap = cclService.getContentCatalogueByTitle(name, client, lang);
		if(cclinkMap != null && cclinkMap.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cclinkMap);
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	/**
	 * 获取cms信息详细信息
	 * @param cid 目录的Id
	 * @param lang
	 * @param client
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "cms", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/deails/{cid}")
	public Result getDeailsByCid(@PathVariable Integer cid,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client){
		Result res = new Result();
		List<ContentDeails> cdlist = cclService.getContentDeails(cid, lang);
		if(cdlist != null && cdlist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cdlist);
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 获取cms信息详细信息
	 * @param cpath
	 * @param lang
	 * @param client
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "cms", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/deails")
	public Result getEvent(@RequestParam(value = "url") String url,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client){
		Result res = new Result();
		if(url != null){
			List<ContentDeails> cdlist = cclService.getContentDeailsByPath(url, lang, client);
			if(cdlist != null && cdlist.size() > 0 ){
				res.setRet(CommonUtils.SUCCESS_RES);
				res.setData(cdlist);
				return res;
			}
		}
		res.setRet(CommonUtils.NOT_DATA_RES);
		res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		
		return res;
	}
}
