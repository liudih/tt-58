package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.entity.Category;
import com.tomtop.entity.CategoryLable;
import com.tomtop.entity.CategoryPath;
import com.tomtop.mappers.product.CategoryMapper;
import com.tomtop.services.ICategoryService;

/**
 * 类目服务实现类
 * 
 * @author lijun
 *
 */
@Service
public class CategoryService implements ICategoryService {


	@Autowired
	CategoryMapper categoryMapper;
	
	/**
	 * 根据categoryID获取面包屑
	 * 
	 * @param categoryId
	 * @param languageId
	 * @return
	 */
	@Override
	public List<CategoryLable> getCategoryLableBreadCrumbs(
			Integer categoryId, Integer languageId, Integer siteId) {
		List<CategoryLable> clist = new ArrayList<CategoryLable>();
		CategoryLable cl = categoryMapper.getCategoryLable(categoryId,
				languageId,siteId);
		if (cl == null) {
			return clist;
		}
		clist.add(cl);
		Integer parentid = cl.getIparentid();
		if (parentid != null) {
			do {
				cl = categoryMapper.getCategoryLable(parentid, languageId,siteId);
				clist.add(cl);
				parentid = cl.getIparentid();
			} while (parentid != null);
		}
		return clist;
	}

	/**
	 * 根据listingID获取面包屑
	 * 
	 * @param listingId
	 * @param languageId
	 * @return
	 */
	@Override
	public List<CategoryLable> getCategoryLableBreadCrumbsByListingId(
			String listingId, Integer languageId, Integer siteId) {
		List<CategoryLable> clist = categoryMapper.getCategoryLableByListingId(
				listingId, languageId,siteId);
		if (clist == null || clist.size() == 0) {
			return clist;
		}
		return clist;
	}
	
	/**
	 * 根据cpath获取面包屑
	 * 
	 * @param cpath
	 * @param languageId
	 * @return
	 */
	@Override
	public List<CategoryLable> getCategoryLableBreadByPath(String path,
			Integer languageId, Integer siteId) {
		List<CategoryLable> clist = new ArrayList<CategoryLable>();
		CategoryLable cl = categoryMapper.getCategoryLableByPath(path,
				languageId);
		if (cl == null) {
			return clist;
		}
		clist.add(cl);
		Integer parentid = cl.getIparentid();
		if (parentid != null) {
			do {
				cl = categoryMapper.getCategoryLable(parentid, languageId,siteId);
				clist.add(cl);
				parentid = cl.getIparentid();
			} while (parentid != null);
		}
		return clist;
	}
	
	/**
	 * 获取品类集合
	 * 
	 * @param level
	 * @param parentId
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
	@Cacheable(value = "product_category", keyGenerator = "customKeyGenerator")
	@Override
	public List<Category> getCategoryList(Integer level,Integer parentId,Integer languageid,Integer websiteid){
		Map<String, Object> paras = Maps.newLinkedHashMap();
		paras.put("langId", languageid);
		paras.put("websiteId", websiteid);
		if (level != null) {
			paras.put("level", level);
		} else if (parentId != null) {
			paras.put("parentId", parentId);
		} 
		List<Category> list = categoryMapper.getCategories(paras);
		return list;
	}
	
	@Override
	public Category getCategoryByCategoryId(Integer categoryId,
			Integer languageid, Integer websiteid) {
		Map<String, Object> paras = Maps.newLinkedHashMap();
		paras.put("langId", languageid);
		paras.put("websiteId", websiteid);
		paras.put("categoryId", categoryId);
		List<Category> list = categoryMapper.getCategories(paras);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据cpath获取categoryId
	 * 
	 * @return
	 */
	@Cacheable(value = "category_path", keyGenerator = "customKeyGenerator",cacheManager = "dayCacheManager")
	@Override
	public HashMap<String, Integer> getCategoryPath(Integer siteId) {
		HashMap<String, Integer> cidMap = new HashMap<String, Integer>();
		List<CategoryPath> cpList = categoryMapper.getCategoryPathDto(siteId);
		
		if(cpList != null && cpList.size() > 0){
			for (int i = 0; i < cpList.size(); i++) {
				String key = cpList.get(i).getPath();
				Integer id = cpList.get(i).getId();
				cidMap.put(key, id);
			}
		}
		return cidMap;
	}
	
	/**
	 * 根据cpath获取CategoryPath
	 * 
	 * @return
	 */
	@Cacheable(value = "category_path", keyGenerator = "customKeyGenerator",cacheManager = "dayCacheManager")
	@Override
	public HashMap<String, CategoryPath> getCategoryPathV2(Integer website) {
		HashMap<String, CategoryPath> cidMap = new HashMap<String, CategoryPath>();
		List<CategoryPath> cpList = categoryMapper.getCategoryPathDto(website);
		if(cpList != null && cpList.size() > 0){
			for (int i = 0; i < cpList.size(); i++) {
				String key = cpList.get(i).getPath();
				cidMap.put(key, cpList.get(i));
			}
		}
		return cidMap;
	}
	
	/**
	 * 获取品类id 对应名称
	 * 
	 * @param websiteid
	 * @return
	 */
	@Cacheable(value = "product_category", keyGenerator = "customKeyGenerator")
	@Override
	public HashMap<Integer,String> getCategoryIdName(Integer lang,Integer siteId){
		HashMap<Integer,String> hmap = new HashMap<Integer,String>();
		List<Category> clist = this.getCategoryList(null, null, lang, siteId);
		for (int i = 0; i < clist.size(); i++) {
			Category cc = clist.get(i);
			hmap.put(cc.getIcategoryid(), cc.getCname());
		}
		return hmap;
	}
	

}
