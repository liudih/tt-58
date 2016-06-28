package com.tomtop.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.base.mappers.CountryBaseMapper;
import com.tomtop.base.models.bo.CountryBo;
import com.tomtop.base.models.dto.CountryBase;
import com.tomtop.base.service.ICountryBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.BeanUtils;

@Service
public class CountryBaseServiceImpl implements ICountryBaseService {

	@Autowired
	CountryBaseMapper countryBaseMapper;
	
	/**
	 * 获取所有国家信息
	 * 
	 * @return List<CountryBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<CountryBo> getCountryList() {
		List<CountryBase> cbList = countryBaseMapper.getAllCountryBase();
		List<CountryBo> cboList = Lists.transform(cbList,  cb -> {
			CountryBo cbo = new CountryBo();
			BeanUtils.copyPropertys(cb, cbo);
				return cbo;
			});
		return cboList;
	}

	/**
	 * 获取国家信息列表
	 * 
	 * 	@param CountryBase
	 * 			根据设定的对象的条件筛选
	 * 
	 * @return List<CountryBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<CountryBo> getCountryList(CountryBase cb) {
		List<CountryBase> cbList = countryBaseMapper.getCountryBase(cb);
		List<CountryBo> cboList = Lists.transform(cbList,  cbl -> {
			CountryBo cbo = new CountryBo();
			BeanUtils.copyPropertys(cbl, cbo);
				return cbo;
			});
		return cboList;
	}

	/**
	 * 获取国家信息
	 * 
	 * 	@param id
	 * 		主键Id
	 * 
	 * @return CountryBo
	 * @author renyy
	 *
	 */
	@Override
	public CountryBo getCountryById(Integer id) {
		CountryBase cb = new CountryBase();
		cb.setId(id);
		List<CountryBase> cbList = countryBaseMapper.getCountryBase(cb);
		CountryBo cbo = new CountryBo();
		if (cbList != null && cbList.size() > 0) {
			cb = cbList.get(0);
			BeanUtils.copyPropertys(cb, cbo);
			cbo.setRes(CommonUtils.SUCCESS_RES);
		}else{
			cbo.setRes(CommonUtils.NOT_DATA_RES);
			cbo.setMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return cbo;
	}

}
