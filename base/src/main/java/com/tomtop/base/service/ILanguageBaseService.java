package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.LanguageBo;
import com.tomtop.base.models.dto.LanguageBase;

public interface ILanguageBaseService {

	public List<LanguageBo> getLanguageList();
	
	public List<LanguageBo> getLanguageList(LanguageBase lb);
	
	public LanguageBo getLanguageById(Integer id);
}
