package com.tomtop.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.base.mappers.LanguageBaseMapper;
import com.tomtop.base.models.bo.LanguageBo;
import com.tomtop.base.models.dto.LanguageBase;
import com.tomtop.base.service.ILanguageBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.BeanUtils;
@Service
public class LanguageBaseServiceImpl implements ILanguageBaseService {

	@Autowired
	LanguageBaseMapper languageBaseMapper;
	/**
	 * 获取所有语言信息
	 * 
	 * @return List<LanguageBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<LanguageBo> getLanguageList() {
		List<LanguageBase> lbList = languageBaseMapper.getAllLanguageBase();
		List<LanguageBo> cboList = Lists.transform(lbList,  lb -> {
			LanguageBo lbo = new LanguageBo();
			BeanUtils.copyPropertys(lb, lbo);
				return lbo;
			});
		
		return cboList;
	}
	/**
	 * 获取语言信息列表
	 * 
	 * 	@param LanguageBase
	 * 			根据设定的对象的条件筛选
	 * 
	 * @return List<LanguageBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<LanguageBo> getLanguageList(LanguageBase lb) {
		List<LanguageBase> lbList = languageBaseMapper.getLanguageBase(lb);
		List<LanguageBo> cboList = Lists.transform(lbList,  lbl -> {
			LanguageBo lbo = new LanguageBo();
			BeanUtils.copyPropertys(lbl, lbo);
				return lbo;
			});
		
		return cboList;
	}

	/**
	 * 获取语言信息
	 * 
	 * 	@param id
	 * 		主键Id
	 * 
	 * @return LanguageBo
	 * @author renyy
	 *
	 */
	@Override
	public LanguageBo getLanguageById(Integer id) {
		LanguageBase lb = new LanguageBase();
		lb.setId(id);
		List<LanguageBase> lbList = languageBaseMapper.getLanguageBase(lb);
		LanguageBo lbo = new LanguageBo();
		if (lbList != null && lbList.size() > 0) {
			lb = lbList.get(0);
			BeanUtils.copyPropertys(lb, lbo);
			lbo.setRes(CommonUtils.SUCCESS_RES);
		}else{
			lbo.setRes(CommonUtils.NOT_DATA_RES);
			lbo.setMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return lbo;
	}

}
