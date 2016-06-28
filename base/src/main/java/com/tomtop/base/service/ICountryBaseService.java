package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.CountryBo;
import com.tomtop.base.models.dto.CountryBase;

public interface ICountryBaseService {

	public List<CountryBo> getCountryList();
	
	public List<CountryBo> getCountryList(CountryBase cb);
	
	public CountryBo getCountryById(Integer id);
}
