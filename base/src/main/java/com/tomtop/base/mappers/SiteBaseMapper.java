package com.tomtop.base.mappers;

import java.util.List;

import com.tomtop.base.models.dto.SiteBase;

public interface SiteBaseMapper {

	List<SiteBase> getAllSiteBase();
	
	List<SiteBase> getSiteBase(SiteBase sb);
}
