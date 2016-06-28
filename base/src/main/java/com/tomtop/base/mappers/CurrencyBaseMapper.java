package com.tomtop.base.mappers;

import java.util.List;

import com.tomtop.base.models.dto.CurrencyBase;

public interface CurrencyBaseMapper {

	List<CurrencyBase> getAllCurrencyBase();
	
	List<CurrencyBase> getCurrencyBase(CurrencyBase cb);
	
	CurrencyBase getCurrencyBaseByCode(String code);
		
}
