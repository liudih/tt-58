package com.tomtop.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.base.models.bo.StorageBo;
import com.tomtop.base.models.dto.StorageDto;
import com.tomtop.base.productbase.mappers.StorageMapper;
import com.tomtop.base.service.IStoragService;
import com.tomtop.framework.core.utils.BeanUtils;

@Service
public class StoragServiceImpl implements IStoragService {

	@Autowired
	StorageMapper mapper;

	@Override
	public StorageBo getById(Integer id) {
		// TODO Auto-generated method stub
		StorageDto dto = mapper.getById(id);
		return dto == null ? null : BeanUtils.mapFromClass(mapper.getById(id),
				StorageBo.class);
	}

	@Override
	public StorageBo getByName(String name) {
		// TODO Auto-generated method stub
		StorageDto dto = mapper.getByName(name);
		return dto == null ? null : BeanUtils
				.mapFromClass(dto, StorageBo.class);
	}

	@Override
	public List<StorageBo> getAll() {
		// TODO Auto-generated method stub
		return Lists.transform(mapper.getAll(),
				p -> BeanUtils.mapFromClass(p, StorageBo.class));
	}

}
