package com.tomtop.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.base.mappers.VhostBaseMapper;
import com.tomtop.base.models.bo.VhostBo;
import com.tomtop.base.models.dto.VhostBase;
import com.tomtop.base.service.IVhostBaseService;
import com.tomtop.framework.core.utils.BeanUtils;

/**
 * 来源业务逻辑类
 * @author renyy
 *
 */
@Service
public class VhostBaseServiceImpl implements IVhostBaseService {

	@Autowired
	VhostBaseMapper VhostMapper;
	
	/**
	 * 获取所有来源信息
	 * 
	 * @return List<VhostBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<VhostBo> getVhostList() {
		List<VhostBase> vbList = VhostMapper.getAllVhostBase();
		List<VhostBo> vboList = Lists.transform(vbList,  vb -> {
			VhostBo vbo = new VhostBo();
			BeanUtils.copyPropertys(vb, vbo);
				return vbo;
			});
		
		return vboList;
	}

	/**
	 * 获取客户端信息(根据条件)
	 * 
	 * @param VhostBase
	 * 			根据设定的对象的条件筛选
	 * 			
	 * @return List<VhostBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<VhostBo> getVhostList(VhostBase vb) {
		List<VhostBase> vbList = VhostMapper.getVhostBase(vb);
		List<VhostBo> vboList = Lists.transform(vbList,  vbl -> {
			VhostBo vbo = new VhostBo();
			BeanUtils.copyPropertys(vbl, vbo);
				return vbo;
			});
		
		return vboList;
	}
}
