package com.tomtop.base.service.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.base.mappers.ResourceBaseMapper;
import com.tomtop.base.models.bo.ResourceBo;
import com.tomtop.base.models.dto.ResourceBase;
import com.tomtop.base.service.IResourceBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.BeanUtils;
@Service
public class ResourceBaseServiceImpl implements IResourceBaseService {

	@Autowired
	ResourceBaseMapper resourceBaseMapper;
	/**
	 * 获取所有资源信息
	 * 
	 * @return List<ResourceBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<ResourceBo> getResourceList() {
		List<ResourceBase> rbList = resourceBaseMapper.getAllResourceBase();
		List<ResourceBo> rboList = Lists.transform(rbList,  rb -> {
			ResourceBo rbo = new ResourceBo();
			BeanUtils.copyPropertys(rb, rbo);
				return rbo;
			});
		return rboList;
	}
	/**
	 * 获取资源信息列表
	 * 
	 * 	@param ResourceBase
	 * 			根据设定的对象的条件筛选
	 * 
	 * @return List<ResourceBo>
	 * @author renyy
	 *
	 */
	@Override
	public List<ResourceBo> getResourceList(ResourceBase rb) {
		List<ResourceBase> rbList = resourceBaseMapper.getResourceBase(rb);
		List<ResourceBo> rboList = Lists.transform(rbList,  rbl -> {
			ResourceBo rbo = new ResourceBo();
			BeanUtils.copyPropertys(rbl, rbo);
				return rbo;
			});
		return rboList;
	}

	/**
	 * 获取资源信息
	 * 
	 * 	@param id
	 * 		主键Id
	 * 
	 * @return ResourceBo
	 * @author renyy
	 *
	 */
	@Override
	public ResourceBo getResourceById(Integer id) {
		ResourceBase rb = new ResourceBase();
		rb.setId(id);
		List<ResourceBase> rbList = resourceBaseMapper.getResourceBase(rb);
		ResourceBo rbo = new ResourceBo();
		if (rbList != null && rbList.size() > 0) {
			rb = rbList.get(0);
			BeanUtils.copyPropertys(rb, rbo);
			rbo.setRes(CommonUtils.SUCCESS_RES);
		}else{
			rbo.setRes(CommonUtils.NOT_DATA_RES);
			rbo.setMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return rbo;
	}

	/**
	 * 获取资源信息 Map
	 * 
	 * 	@param lang
	 * 		语言Id
	 * 	@param client
	 * 		客户端Id
	 * 
	 * @return Map<String,String>
	 * @author renyy
	 *
	 */
	@Override
	public Map<String,String> getResourceMap(Integer lang,Integer client) {
		ResourceBase rbwhere = new ResourceBase();
		rbwhere.setLanguageId(lang);
		rbwhere.setClientId(client);
		List<ResourceBase> rbList = resourceBaseMapper.getResourceBase(rbwhere);
		ResourceBase rb = null;
		Map<String,String> strMap = new TreeMap<String,String>();
		for (int i = 0; i < rbList.size(); i++) {
			rb = rbList.get(i);
			strMap.put(rb.getKey(),rb.getValue());
		}
		return strMap;
	}
}
