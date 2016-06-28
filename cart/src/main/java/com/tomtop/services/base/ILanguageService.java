package com.tomtop.services.base;

import java.util.List;

import com.tomtop.dto.base.Language;

public interface ILanguageService {

	public Language getLanguage(int id);

	public Language getDefaultLanguage();

	public int getLanguageMaxId();

	public List<Language> getAllLanguage();

	public List<Language> getMaxLanguage(int mid);

	/**
	 * 
	 * @Title: getLanguagesByIds
	 * @Description: TODO(通过语言ID列表查询语言列表)
	 * @param @param languageIds
	 * @param @return
	 * @return List<Language>
	 * @throws
	 * @author yinfei
	 */
	public List<Language> getLanguagesByIds(List<Integer> languageIds);

	/**
	 * 获取语言
	 * 
	 * @param langCode
	 * @return
	 */
	public Language getLanguageByCode(String langCode);

}