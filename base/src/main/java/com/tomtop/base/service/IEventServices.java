package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.base.BaseBean;
import com.tomtop.base.models.bo.EventServicesBo;
import com.tomtop.base.models.bo.SubEventServicesBo;

public interface IEventServices {

	public List<EventServicesBo> getEventServicesAll();
	
	public List<EventServicesBo> getEventServicesByCode(String code);
	public BaseBean insertEventServices(String code,String name,String type,String paramRemark,String remark);
	
	public List<SubEventServicesBo> getSubEventServicesByCode(String code);
	public BaseBean insertSubEventServices(String code,String name,String type,String url,String method,String param);
	
}
