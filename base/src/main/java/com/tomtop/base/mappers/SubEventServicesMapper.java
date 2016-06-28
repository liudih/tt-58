package com.tomtop.base.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.base.models.bo.SubEventServicesBo;
import com.tomtop.base.models.dto.SubEventServicesDto;

public interface SubEventServicesMapper {

	public List<SubEventServicesDto> getSubEventServicesDto(SubEventServicesDto sevent);
	
	public int insertSubEventServicesDto(SubEventServicesDto sevent);
	
	@Select({"select id, code, name, type, url,method,param from sub_event_services ",
			 "where code=#{0} and is_enabled=1 and is_deleted=0"})
	public List<SubEventServicesBo> getSubEventServicesByCode(String code);
}
