package com.tomtop.base.mappers;

import java.util.List;

import com.tomtop.base.models.dto.ResourceBase;

public interface ResourceBaseMapper {

	List<ResourceBase> getAllResourceBase();
	
	List<ResourceBase> getResourceBase(ResourceBase rb);
}
