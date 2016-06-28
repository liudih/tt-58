package com.tomtop.base.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.ResourceBo;
import com.tomtop.base.models.dto.ResourceBase;
import com.tomtop.base.service.IResourceBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 资源信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/resource")
public class ResourceBaseController {

	@Autowired
	IResourceBaseService resourceBaseService;
	
	/**
	 * 获取所有资源信息
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "resource", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getResource(){
		Result res = new Result();
		List<ResourceBo> rblist = resourceBaseService.getResourceList();
		if(rblist != null && rblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(rblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 获取资源信息
	 * @param id
	 *        主键id
	 *          
	 * @return BaseResult
	 */
	@Cacheable(value = "resource", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/{id}")
	public Result getResourceById(@PathVariable Integer id){
		Result res = new Result();
		ResourceBo rb = resourceBaseService.getResourceById(id);
		if(rb.getRes() != CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		rb.setRes(null);
		res.setData(rb);
		
		return res;
	}
	
	/**
	 * 获取资源信息
	 * @param languageId
	 *        语言ID
	 * @param clientId
	 *        客户端ID
	 * @param key
	 *        资源标识
	 * @param value
	 *        资源值
	 * @param isEnabled
	 *        是否启用
	 *          
	 * @return BaseResult
	 */
	@Cacheable(value = "resource", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/where")
	public Result getResourceWhere(
			@RequestParam(value = "lang", required = false) Integer languageId,
			@RequestParam(value = "client", required = false) Integer clientId,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "isEnabled", required = false) Integer isEnabled
			){
		Result res = new Result();
		ResourceBase rb = new ResourceBase();
		if(languageId != null){
			rb.setLanguageId(languageId);
		}
		if(clientId != null){
			rb.setClientId(clientId);
		}
		if(isEnabled != null){
			rb.setIsEnabled(isEnabled);
		}
		if(key != null && !"".equals(key)){
			rb.setKey(key);
		}
		if(value != null && !"".equals(value)){
			rb.setValue(value);
		}
		List<ResourceBo> rblist = resourceBaseService.getResourceList(rb);
		if(rblist != null && rblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(rblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}

	/**
	 * 获取所有资源信息Map
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "resource", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/map")
	public Result getResourceMap(	
			@RequestParam(value = "lang", required = false, defaultValue="1") Integer languageId,
			@RequestParam(value = "client", required = false, defaultValue="1") Integer clientId
			){
		Result res = new Result();
		Map<String,String> rbMap = resourceBaseService.getResourceMap(languageId,clientId);
		if(rbMap != null && rbMap.size() > 0 ){
			res.setRet(1);
			res.setData(rbMap);
		}else{
			rbMap.put("ret", "0");
		}
		return res;
	}
}
