package com.tomtop.services.impl.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dto.base.Language;
import com.tomtop.mappers.base.LanguageMapper;
import com.tomtop.services.base.ILanguageService;

@Service
public class LanguageService implements ILanguageService {

	@Autowired
	LanguageMapper mapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getLanguage(int)
	 */
	@Override
	public Language getLanguage(int id) {
		return mapper.selectByPrimaryKey(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getDefaultLanguage()
	 */
	@Override
	public Language getDefaultLanguage() {
		return mapper.selectDefaultLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getLanguageMaxId()
	 */
	@Override
	public int getLanguageMaxId() {
		return mapper.getLanguageMaxId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getAllLanguage()
	 */
	@Override
	public List<Language> getAllLanguage() {
		return mapper.getAllLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.lang.ILanguageService#getMaxLanguage(int)
	 */
	@Override
	public List<Language> getMaxLanguage(int mid) {
		return mapper.getMaxLanguage(mid);
	}

	/*
	 * (non-Javadoc) <p>Title: getLanguagesByIds</p> <p>Description:
	 * 通过语言ID列表查询语言列表</p>
	 * 
	 * @param languageIds
	 * 
	 * @return
	 * 
	 * @see services.ILanguageService#getLanguagesByIds(java.util.List)
	 */
	public List<Language> getLanguagesByIds(List<Integer> languageIds) {
		return mapper.getLanguagesByIds(languageIds);
	}

	public Language getLanguageByCode(String langCode) {
		return mapper.getLanguageByCode(langCode);
	}
}
