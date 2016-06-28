package com.tomtop.base.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.base.mappers.EventServicesMapper;
import com.tomtop.base.mappers.SubEventServicesMapper;
import com.tomtop.base.models.base.BaseBean;
import com.tomtop.base.models.bo.EventServicesBo;
import com.tomtop.base.models.bo.SubEventServicesBo;
import com.tomtop.base.models.dto.EventServicesDto;
import com.tomtop.base.models.dto.SubEventServicesDto;
import com.tomtop.base.service.IEventServices;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.base.utils.DateUtil;

/**
 * 事件服务
 * @author renyy
 *
 */
@Service
public class EventServicesImpl implements IEventServices {
	private static final Logger logger = LoggerFactory
			.getLogger(EventServicesImpl.class);
	@Autowired
	EventServicesMapper eventSvcMapper;
	@Autowired
	SubEventServicesMapper subEventSvcMapper;
	
	/**
	 * 获取所有的事件服务
	 * 
	 * 
	 */
	@Override
	public List<EventServicesBo> getEventServicesAll() {
		return eventSvcMapper.getEventServicesAll();
	}
	
	/**
	 * 根据code获取事件服务
	 * 
	 * @param code
	 * 
	 */
	@Override
	public List<EventServicesBo> getEventServicesByCode(String code) {
		return eventSvcMapper.getEventServicesByCode(code);
	}
	
	/**
	 * 添加事件服务
	 * 
	 * @param code
	 * @param name
	 * @param type
	 * @param paramRemark
	 * @param remark
	 * 
	 */
	@Override
	public BaseBean insertEventServices(String code, String name, String type,
			String paramRemark, String remark) {
		BaseBean bb = new BaseBean();
		EventServicesDto event = new EventServicesDto();
		if(code == null || "".equals(code)){
			bb.setRes(CommonUtils.CODE_NULL);
			bb.setMsg("code is null");
			return bb;
		}
		if(name == null || "".equals(name)){
			bb.setRes(CommonUtils.NAME_NULL);
			bb.setMsg("name is null");
			return bb;
		}
		if(type == null || "".equals(type)){
			bb.setRes(CommonUtils.TYPE_NULL);
			bb.setMsg("type is null");
			return bb;
		}
		if(code.startsWith("EVENT_CODE_") == false){
			bb.setRes(CommonUtils.START_NOT_RULE);
			bb.setMsg("Must start EVENT_CODE_");
			return bb;
		}
		List<EventServicesBo> esbolist = eventSvcMapper.getEventServicesByCode(code);
		if(esbolist != null && esbolist.size() > 0){
			bb.setRes(CommonUtils.CODE_EXISTED);
			bb.setMsg("code existed");
			return bb;
		}
		event.setCode(code);
		event.setName(name);
		event.setType(type);
		event.setParamRemark(paramRemark);
		event.setRemark(remark);
		event.setCreatedBy(CommonUtils.ADMAIN);
		event.setCreatedOn(DateUtil.StringToDate(DateUtil.getUTCTimeStr()));
		int res = eventSvcMapper.insertEventServicesDto(event);
		if(res <= 0){
			String errMsg = "insert event services error [" + code + "]-[" + name + "]-[" + type  + 
					"]-[" + paramRemark  + "]-[" + remark  + "]";
			logger.error(errMsg);
			bb.setRes(CommonUtils.ERROR_RES);
			bb.setMsg(errMsg);
		}else{
			bb.setRes(CommonUtils.SUCCESS_RES);
		}
		
		return bb;
	}
	/**
	 * 根据code获取订阅事件服务
	 * 
	 * @param code
	 * 
	 */
	@Override
	public List<SubEventServicesBo> getSubEventServicesByCode(String code) {
		return subEventSvcMapper.getSubEventServicesByCode(code);
	}
	/**
	 * 添加订阅事件服务
	 * 
	 * @param code 标识
	 * @param name 名字
	 * @param type 类型
	 * @param url 地址
	 * @param method 请求方式
	 * @param param 请求参数
	 * 
	 */
	@Override
	public BaseBean insertSubEventServices(String code, String name, String type,
			String url,String method, String param) {
		BaseBean bb = new BaseBean();
		SubEventServicesDto sevent = new SubEventServicesDto();
		if(code == null || "".equals(code)){
			bb.setRes(CommonUtils.CODE_NULL);
			bb.setMsg("code is null");
			return bb;
		}
		if(name == null || "".equals(name)){
			bb.setRes(CommonUtils.NAME_NULL);
			bb.setMsg("name is null");
			return bb;
		}
		if(type == null || "".equals(type)){
			bb.setRes(CommonUtils.TYPE_NULL);
			bb.setMsg("type is null");
			return bb;
		}
		if(url == null || "".equals(url)){
			bb.setRes(CommonUtils.URL_NULL);
			bb.setMsg("url is null");
			return bb;
		}
		if(code.startsWith("EVENT_CODE_") == false){
			bb.setRes(CommonUtils.START_NOT_RULE);
			bb.setMsg("subscription event Must start EVENT_CODE_");
			return bb;
		}
		if(method == null || "".equals(method)){
			method = "GET";
		}
		if(!"GET".equals(method) && !"POST".equals(method)){
			bb.setRes(CommonUtils.METHOD_WRONG);
			bb.setMsg("method GET or POST");
			return bb;
		}
		sevent.setCode(code);
		sevent.setName(name);
		sevent.setType(type);
		sevent.setUrl(url);
		sevent.setMethod(method.toUpperCase());
		sevent.setParam(param);
		sevent.setCreatedBy(CommonUtils.ADMAIN);
		sevent.setCreatedOn(DateUtil.StringToDate(DateUtil.getUTCTimeStr()));
		int res = subEventSvcMapper.insertSubEventServicesDto(sevent);
		if(res <= 0){
			String errMsg = "insert event services error [" + code + "]-[" + name + "]-[" + type  + 
					"]-[" + url  + "]-[" + method + "]-[" + param + "]";
			logger.error(errMsg);
			bb.setRes(CommonUtils.ERROR_RES);
			bb.setMsg(errMsg);
		}else{
			bb.setRes(CommonUtils.SUCCESS_RES);
		}
		
		return bb;
	}

}
