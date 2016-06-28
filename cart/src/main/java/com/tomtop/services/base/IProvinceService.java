package com.tomtop.services.base;

import java.util.List;

import com.tomtop.dto.Province;

public interface IProvinceService {

	public abstract List<Province> getProvincesByCountryId(
			Integer countryId);

	public abstract Province getProvincesById(Integer id);

}