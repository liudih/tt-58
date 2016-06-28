package com.tomtop.base.service;

import java.util.List;
import java.util.Map;

import com.tomtop.base.models.bo.ResourceBo;
import com.tomtop.base.models.dto.ResourceBase;

public interface IResourceBaseService {

	public List<ResourceBo> getResourceList();
	
	public List<ResourceBo> getResourceList(ResourceBase rb);
	
	public ResourceBo getResourceById(Integer id);
	
	public Map<String,String> getResourceMap(Integer lang,Integer client);
}

