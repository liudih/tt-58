package com.tomtop.base.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.ParameterBo;
import com.tomtop.base.models.dto.ParameterBase;
import com.tomtop.base.service.IParameterBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 参数信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/parameter")
public class ParameterBaseController {

	@Autowired
	IParameterBaseService parameterBaseService;
	
	/**
	 * 获取所有参数信息
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "parameter", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getParameter(){
		Result res = new Result();
		List<ParameterBo> pblist = parameterBaseService.getParameterList();
		if(pblist != null && pblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(pblist.toArray());
			return res;
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
	}
	
	/**
	 * 获取参数信息
	 * @param id
	 *        主键Id
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "parameter", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/{id}")
	public Result getParameterById(@PathVariable Integer id){
		Result res = new Result();
		ParameterBo pb = parameterBaseService.getParameterById(id);
		if(pb.getRes() != CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		pb.setRes(null);
		res.setData(pb);
		
		return res;
	}
	
	/**
	 * 获取参数信息
	 * @param languageId
	 *        语言ID
	 * @param clientId
	 *        客户端ID
	 * @param type
	 *        参数类型
	 *          
	 * @return BaseResult
	 */
	@Cacheable(value = "parameter", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/where")
	public Result getParameterByWhere(
			@RequestParam(value = "lang", required = false) Integer languageId,
			@RequestParam(value = "client", required = false) Integer clientId,
			@RequestParam(value = "type", required = false) String type
			){
		Result res = new Result();
		ParameterBase pb = new ParameterBase();
		if(languageId != null){
			pb.setLanguageId(languageId);
		}
		if(clientId != null){
			pb.setClientId(clientId);
		}
		if(type != null && !"".equals(type)){
			pb.setType(type);
		}
		List<ParameterBo> pblist = parameterBaseService.getParameterList(pb);
		if(pblist != null && pblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(pblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
}
