package com.tomtop.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.base.mappers.ParameterBaseMapper;
import com.tomtop.base.models.bo.ParameterBo;
import com.tomtop.base.models.dto.ParameterBase;
import com.tomtop.base.service.IParameterBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.BeanUtils;
@Service
public class ParameterBaseServiceImpl implements IParameterBaseService {

	@Autowired
	ParameterBaseMapper parameterBaseMapper;
	/**
	 * 获取所有参数信息
	 * 
	 * @return List<ParameterBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<ParameterBo> getParameterList() {
		List<ParameterBase> pbList = parameterBaseMapper.getAllParameterBase();
		List<ParameterBo> pboList = Lists.transform(pbList,  pb -> {
			ParameterBo pbo = new ParameterBo();
			BeanUtils.copyPropertys(pb, pbo);
				return pbo;
			});
		return pboList;
	}
	/**
	 * 获取参数信息列表
	 * 
	 * 	@param ParameterBase
	 * 			根据设定的对象的条件筛选
	 * 
	 * @return List<ParameterBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<ParameterBo> getParameterList(ParameterBase pb) {
		//return ParameterBaseMapper.getParameterBase(pb);
		List<ParameterBase> pbList = parameterBaseMapper.getParameterBase(pb);
		List<ParameterBo> pboList = Lists.transform(pbList,  pbl -> {
			ParameterBo pbo = new ParameterBo();
			BeanUtils.copyPropertys(pbl, pbo);
				return pbo;
			});
		return pboList;
	}

	/**
	 * 获取参数信息
	 * 
	 * 	@param id
	 * 		主键Id
	 * 
	 * @return ParameterBo
	 * @author renyy
	 *
	 */
	@Override
	public ParameterBo getParameterById(Integer id) {
		ParameterBase pb = new ParameterBase();
		pb.setId(id);
		List<ParameterBase> pbList = parameterBaseMapper.getParameterBase(pb);
		ParameterBo pbo = new ParameterBo();
		if (pbList != null && pbList.size() > 0) {
			pb = pbList.get(0);
			BeanUtils.copyPropertys(pb, pbo);
			pbo.setRes(CommonUtils.SUCCESS_RES);
		}else{
			pbo.setRes(CommonUtils.NOT_DATA_RES);
			pbo.setMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return pbo;
	}

}
