package com.tomtop.services;

import java.util.List;
import java.util.Map;

import com.tomtop.entity.CategoryAttributes;

public interface IAttributesService {

	public List<CategoryAttributes> getCategoryAttributes(Integer categoryId,Integer lang);
	public Map<String,List<String>> getCategoryAttributesMap(Integer categoryId,Integer lang);
	public List<String> getCategoryAttributeAllKey(Integer categoryId,Integer lang);
	public Map<String,List<String>> putCategoryAttributesMap(Integer categoryId,Integer lang);
	public List<String> putCategoryAttributeAllKey(Integer categoryId,Integer lang);
}
