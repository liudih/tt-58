package com.tomtop.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.base.mappers.SiteBaseMapper;
import com.tomtop.base.models.bo.SiteBo;
import com.tomtop.base.models.dto.SiteBase;
import com.tomtop.base.service.ISiteBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.BeanUtils;
@Service
public class SiteBaseServiceImpl implements ISiteBaseService {

	@Autowired
	SiteBaseMapper siteBaseMapper;
	/**
	 * 获取所有站点信息
	 * 
	 * @return List<SiteBase>
	 * @author renyy
	 *
	 */
	@Override
	public List<SiteBo> getSiteList() {
		List<SiteBase> sbList = siteBaseMapper.getAllSiteBase();
		List<SiteBo> sboList = Lists.transform(sbList,  sb -> {
			SiteBo sbo = new SiteBo();
			BeanUtils.copyPropertys(sb, sbo);
				return sbo;
			});
		return sboList;
	}
	/**
	 * 获取站点信息列表
	 * 
	 * 	@param SiteBase
	 * 			根据设定的对象的条件筛选
	 * 
	 * @return List<SiteBase>
	 * @author renyy
	 *
	 */
	@Override
	public List<SiteBo> getSiteList(SiteBase sb) {
		List<SiteBase> sbList = siteBaseMapper.getSiteBase(sb);
		List<SiteBo> sboList = Lists.transform(sbList,  sbl -> {
			SiteBo sbo = new SiteBo();
			BeanUtils.copyPropertys(sbl, sbo);
				return sbo;
			});
		return sboList;
	}

	/**
	 * 获取站点信息
	 * 
	 * 	@param id
	 * 		主键Id
	 * 
	 * @return SiteBase
	 * @author renyy
	 *
	 */
	@Override
	public SiteBo getSiteById(Integer id) {
		SiteBase sb = new SiteBase();
		sb.setId(id);
		List<SiteBase> sbList = siteBaseMapper.getSiteBase(sb);
		SiteBo sbo = new SiteBo();
		if (sbList != null && sbList.size() > 0) {
			sb = sbList.get(0);
			BeanUtils.copyPropertys(sb, sbo);
			sbo.setRes(CommonUtils.SUCCESS_RES);
		}else{
			sbo.setRes(CommonUtils.NOT_DATA_RES);
			sbo.setMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return sbo;
	}

}
