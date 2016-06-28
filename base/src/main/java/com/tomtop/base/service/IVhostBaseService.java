package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.VhostBo;
import com.tomtop.base.models.dto.VhostBase;

public interface IVhostBaseService {

	public List<VhostBo> getVhostList();
	
	public List<VhostBo> getVhostList(VhostBase vb);
}
