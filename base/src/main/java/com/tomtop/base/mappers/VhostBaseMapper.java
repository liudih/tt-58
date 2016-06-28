package com.tomtop.base.mappers;

import java.util.List;

import com.tomtop.base.models.dto.VhostBase;

public interface VhostBaseMapper {
	
	List<VhostBase> getAllVhostBase();
	
	List<VhostBase> getVhostBase(VhostBase vb);
}
