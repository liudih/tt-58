package com.tomtop.base.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.base.models.bo.EventServicesBo;
import com.tomtop.base.models.dto.EventServicesDto;

public interface EventServicesMapper {

	public List<EventServicesDto> getEventServicesDto(EventServicesDto event);
	
	public int insertEventServicesDto(EventServicesDto event);
	
	@Select({"select id, code, name, type, param_remark paramRemark, remark from event_services ",
			"where code=#{0} and is_enabled=1 and is_deleted=0"})
	public List<EventServicesBo> getEventServicesByCode(String code);
	
	@Select({"select id, code, name, type, param_remark paramRemark, remark from event_services ",
			"where is_enabled=1 and is_deleted=0"})
	public List<EventServicesBo> getEventServicesAll();
}
