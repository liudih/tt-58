package com.tomtop.base.mappers;

import java.util.List;

import com.tomtop.base.models.dto.ParameterBase;

public interface ParameterBaseMapper {

	List<ParameterBase> getAllParameterBase();
	
	List<ParameterBase> getParameterBase(ParameterBase lb);
}
