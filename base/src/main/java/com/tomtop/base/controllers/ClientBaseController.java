package com.tomtop.base.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.ClientBo;
import com.tomtop.base.models.dto.ClientBase;
import com.tomtop.base.service.IClientBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 客户端信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/client")
public class ClientBaseController {

	@Autowired
	IClientBaseService clientBaseService;
	
	/**
	 * 获取所有客户端信息
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "client", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getClient(){
		Result res = new Result();
		List<ClientBo> cblist = clientBaseService.getClientList();
		if(cblist != null && cblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 获取客户端信息
	 * 
	 * @param id
	 *        主键Id
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "client", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/{id}")
	public Result getClientById(@PathVariable Integer id){
		Result res = new Result();
		ClientBo cb = clientBaseService.getClientById(id);
		if(cb.getRes() != CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		cb.setRes(null);//设置为null对象值res不显示,只针对于返回单个对象值时使用
		res.setData(cb);
		
		return res;
	}
	
	/**
	 * 获取客户端信息
	 * 
	 * @param name
	 *        客户端名称
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "client", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/name/{name}")
	public Result getClientByName(@PathVariable String name){
		Result res = new Result();
		ClientBase cb = new ClientBase();
		cb.setName(name);
		List<ClientBo> cbList = clientBaseService.getClientList(cb);
		if (cbList != null && cbList.size() > 0) {
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cbList.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
}
