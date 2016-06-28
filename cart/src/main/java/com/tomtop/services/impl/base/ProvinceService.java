package com.tomtop.services.impl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.Province;
import com.tomtop.mappers.base.ProvinceMapper;
import com.tomtop.services.base.IProvinceService;

@Service
public class ProvinceService implements IProvinceService {

	@Autowired
	ProvinceMapper provinceMapper;

	@Override
	public List<Province> getProvincesByCountryId(Integer countryId) {
		return provinceMapper.getAllProvincesByCountryId(countryId);
	}

	@Override
	public Province getProvincesById(Integer id) {
		return provinceMapper.getById(id);
	}
}
