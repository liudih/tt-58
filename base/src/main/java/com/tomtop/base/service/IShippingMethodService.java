package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.ShippingMethodBo;

public interface IShippingMethodService {
	public List<ShippingMethodBo> getListByStorageId(Integer storageId,
			Integer lang);

	public List<ShippingMethodBo> getListByLang(Integer lang);
}
