package com.tomtop.base.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.VhostBo;
import com.tomtop.base.models.dto.VhostBase;
import com.tomtop.base.service.IVhostBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 来源信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/vhost")
public class VhostBaseController {

	@Autowired
	IVhostBaseService vhostBaseService;
	
	/**
	 * 获取所有来源信息
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "vhost", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getClient(){
		Result res = new Result();
		List<VhostBo> vblist = vhostBaseService.getVhostList();
		if(vblist != null && vblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(vblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 获取来源信息
	 * 
	 * @param vhost
	 *        vhost
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "vhost", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/{vhost}")
	public Result getClientById(@PathVariable String vhost){
		Result res = new Result();
		VhostBase vb = new VhostBase();
		vb.setVhost(vhost);
		List<VhostBo> vblist = vhostBaseService.getVhostList(vb);
		if(vblist != null && vblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(vblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
}
