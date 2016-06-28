package com.tomtop.services.base;

import java.util.List;

import com.tomtop.dto.base.SystemParameter;

public interface ISystemParameterService {

	public String getSystemParameter(Integer iwebsiteid, Integer ilanguageid,
			String cparameterkey);

	public String getSystemParameter(Integer iwebsiteid, Integer ilanguageid,
			String cparameterkey, String defaultValue);

	public int getSystemParameterAsInt(Integer iwebsiteid, Integer ilanguageid,
			String cparameterkey, int defaultValue);

	public double getSystemParameterAsDouble(Integer iwebsiteid,
			Integer ilanguageid, String cparameterkey, double defaultValue);

	public List<SystemParameter> getAllSysParameter();

	public boolean alterSysParameter(SystemParameter systemParameter);

	public boolean validateKey(String key, Integer websiteId);

	public SystemParameter getSysParameterByKeyAndSiteIdAndLanugageId(
			Integer siteId, Integer languageId, String cparameterkey);

	SystemParameter getSystemParameterNoCacheByKey(String key, Integer websiteId);

}