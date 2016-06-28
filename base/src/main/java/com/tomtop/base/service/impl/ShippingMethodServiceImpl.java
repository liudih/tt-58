package com.tomtop.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.base.models.bo.ShippingMethodBo;
import com.tomtop.base.models.dto.ShippingMethodDto;
import com.tomtop.base.order.mappers.ShippingMethodMapper;
import com.tomtop.base.service.IShippingMethodService;
import com.tomtop.framework.core.utils.BeanUtils;

@Service
public class ShippingMethodServiceImpl implements IShippingMethodService {

	@Autowired
	ShippingMethodMapper mapper;

	@Override
	public List<ShippingMethodBo> getListByStorageId(Integer storageId,
			Integer lang) {
		List<ShippingMethodDto> dto = mapper
				.getListByStorageId(storageId, lang);
		if (dto == null || dto.size() <= 0) {
			dto = mapper.getListByStorageId(storageId, 1);
		}
		return Lists.transform(dto,
				p -> BeanUtils.mapFromClass(p, ShippingMethodBo.class));
	}

	@Override
	public List<ShippingMethodBo> getListByLang(Integer lang) {
		List<ShippingMethodDto> dto = mapper.getListByLang(lang);
		if (dto == null || dto.size() <= 0) {
			dto = mapper.getListByLang(1);
		}
		return Lists.transform(dto,
				p -> BeanUtils.mapFromClass(p, ShippingMethodBo.class));
	}
}
