package com.tomtop.base.mappers;

import java.util.List;

import com.tomtop.base.models.dto.LanguageBase;

public interface LanguageBaseMapper {

	List<LanguageBase> getAllLanguageBase();
	
	List<LanguageBase> getLanguageBase(LanguageBase lb);
}
