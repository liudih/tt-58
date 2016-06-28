package com.tomtop.base.mappers;

import java.util.List;

import com.tomtop.base.models.dto.CountryBase;

public interface CountryBaseMapper {

	List<CountryBase> getAllCountryBase();
	
	List<CountryBase> getCountryBase(CountryBase cb);
}
