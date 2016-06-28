package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.entity.CategoryAttributes;
import com.tomtop.mappers.product.CategoryAttributeMapper;
import com.tomtop.services.IAttributesService;
/**
 * 类目属性
 * @author renyy
 *
 */
@Service
public class AttributesServiceImpl implements IAttributesService {

	@Autowired
	CategoryAttributeMapper categoryAttributeMapper;
	
	/**
	 * 获取所有类目聚合属性
	 */
	@Override
	public List<CategoryAttributes> getCategoryAttributes(Integer categoryId,
			Integer lang) {
		return categoryAttributeMapper.getCategoryAttributesByCategory(categoryId, lang);
	}
	
	/**
	 * 获取key 对应的所有属性
	 */
	@Cacheable(value = "agg_att_map", key = "#root.caches[0].name + '_' + #categoryId  + '_' + #lang", cacheManager = "noCacheManager")
	@Override
	public Map<String,List<String>> getCategoryAttributesMap(Integer categoryId,
			Integer lang){
		Map<String,List<String>> map = new LinkedHashMap<String, List<String>>();
		List<CategoryAttributes> caList = this.getCategoryAttributes(categoryId, lang);
		for (CategoryAttributes ca : caList) {
			if(map.containsKey(ca.getKeyName())){
				List<String> lists = map.get(ca.getKeyName());
				lists.add(ca.getValueName());
				map.put(ca.getKeyName(), lists);
			}else{
				List<String> lists = new ArrayList<String>();
				lists.add(ca.getValueName());
				map.put(ca.getKeyName(), lists);
			}
		}
		
		return map;
	}
	
	/**
	 * 获取类目所有属性的Key值
	 */
	@Cacheable(value = "agg_att_key", key = "#root.caches[0].name + '_' + #categoryId + '_' + #lang", cacheManager = "noCacheManager")
	@Override
	public List<String> getCategoryAttributeAllKey(Integer categoryId,Integer lang){
		return categoryAttributeMapper.getAttributesKey(categoryId, lang);
	}
	
	/**
	 * 更新缓存使用
	 * 获取key 对应的所有属性
	 */
	@CachePut(value = "agg_att_map", key = "#root.caches[0].name + '_' + #categoryId + '_' + #lang", cacheManager = "noCacheManager")
	@Override
	public Map<String,List<String>> putCategoryAttributesMap(Integer categoryId,Integer lang){
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		List<CategoryAttributes> caList = this.getCategoryAttributes(categoryId, lang);
		for (CategoryAttributes ca : caList) {
			if(map.containsKey(ca.getKeyName())){
				List<String> lists = map.get(ca.getKeyName());
				lists.add(ca.getValueName());
				map.put(ca.getKeyName(), lists);
			}else{
				List<String> lists = new ArrayList<String>();
				lists.add(ca.getValueName());
				map.put(ca.getKeyName(), lists);
			}
		}
		
		return map;
	}
	
	/**
	 *  更新缓存使用
	 * 获取类目所有属性的Key值
	 */
	@CachePut(value = "agg_att_key", key = "#root.caches[0].name + '_' + #categoryId + '_' + #lang", cacheManager = "noCacheManager")
	@Override
	public List<String> putCategoryAttributeAllKey(Integer categoryId,Integer lang){
		return categoryAttributeMapper.getAttributesKey(categoryId, lang);
	}

}
