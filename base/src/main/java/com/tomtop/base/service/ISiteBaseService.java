package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.SiteBo;
import com.tomtop.base.models.dto.SiteBase;

public interface ISiteBaseService {

	public List<SiteBo> getSiteList();
	
	public List<SiteBo> getSiteList(SiteBase sb);
	
	public SiteBo getSiteById(Integer id);
}
