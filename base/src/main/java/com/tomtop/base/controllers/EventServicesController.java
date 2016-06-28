package com.tomtop.base.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tomtop.base.models.base.BaseBean;
import com.tomtop.base.models.bo.EventServicesBo;
import com.tomtop.base.models.bo.SubEventServicesBo;
import com.tomtop.base.service.IEventServices;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 事件服务信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/event")
public class EventServicesController {

	@Autowired
	IEventServices eventService;
	/**
	 * 获取全部事件服务
	 * 
	 * @return BaseResult
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/v1/all")
	public Result getEventAll(){
		Result res = new Result();
		List<EventServicesBo> eslist = eventService.getEventServicesAll();
		if(eslist != null && eslist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(eslist);
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 根据code获取事件服务
	 * @param code
	 * 
	 * @return BaseResult
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getEvent(@RequestParam("code") String code){
		Result res = new Result();
		List<EventServicesBo> eslist = eventService.getEventServicesByCode(code);
		if(eslist != null && eslist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(eslist);
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 根据code获取订阅事件服务
	 * @param code
	 * 
	 * @return BaseResult
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/v1/sub")
	public Result getSubEvent(@RequestParam("code") String code){
		Result res = new Result();
		List<SubEventServicesBo> eslist = eventService.getSubEventServicesByCode(code);
		if(eslist != null && eslist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(eslist);
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 添加事件服务
	 * @param EventServicesBo
	 * 
	 * @return BaseResult
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/v1/add")
	public Result addEvent(@RequestBody EventServicesBo esbo){
		Result res = new Result();
		BaseBean bb = eventService.insertEventServices(esbo.getCode(), esbo.getName(), esbo.getType(),
				esbo.getParamRemark(), esbo.getRemark());
		
		if (bb.getRes() == CommonUtils.SUCCESS_RES) {
			res.setRet(CommonUtils.SUCCESS_RES);
		} else {
			res.setRet(bb.getRes());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 添加订阅事件服务
	 * @param SubEventServicesBo
	 * 
	 * @return BaseResult
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST,value = "/v1/add/sub")
	public Result addSubEvent(@RequestBody JSONObject o){
		Result res = new Result();
		SubEventServicesBo esbo = o.parseObject(o.toString(), SubEventServicesBo.class);
		BaseBean bb = eventService.insertSubEventServices(esbo.getCode(), esbo.getName(), esbo.getType(),
				esbo.getUrl(), esbo.getMethod(),esbo.getParam());
		
		if (bb.getRes() == CommonUtils.SUCCESS_RES) {
			res.setRet(CommonUtils.SUCCESS_RES);
		} else {
			res.setRet(bb.getRes());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
}
