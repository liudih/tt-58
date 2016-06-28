package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.ParameterBo;
import com.tomtop.base.models.dto.ParameterBase;

public interface IParameterBaseService {

	public List<ParameterBo> getParameterList();
	
	public List<ParameterBo> getParameterList(ParameterBase pb);
	
	public ParameterBo getParameterById(Integer id);
}
