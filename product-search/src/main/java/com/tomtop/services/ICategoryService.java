package com.tomtop.services;


import java.util.HashMap;
import java.util.List;

import com.tomtop.entity.Category;
import com.tomtop.entity.CategoryLable;
import com.tomtop.entity.CategoryPath;

public interface ICategoryService {

	/**
	 * 根据categoryID获取面包屑
	 * 
	 * @param categoryId
	 * @param languageId
	 * @return
	 */
	List<CategoryLable> getCategoryLableBreadCrumbs(Integer categoryId,
			Integer languageId, Integer siteId);

	/**
	 * 根据listingID获取面包屑
	 * 
	 * @param listingId
	 * @param languageId
	 * @return
	 */
	List<CategoryLable> getCategoryLableBreadCrumbsByListingId(
			String listingId, Integer languageId, Integer siteId);

	/**
	 * 根据cpath获取面包屑
	 * 
	 * @param cpath
	 * @param languageId
	 * @return
	 */
	List<CategoryLable> getCategoryLableBreadByPath(String path,
			Integer languageId,Integer siteId);

	/**
	 * 获取品类集合
	 * 
	 * @param level
	 * @param parentId
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
	public List<Category> getCategoryList(Integer level,Integer parentId,Integer languageid,Integer websiteid);
	
	/**
	 * 根据品类Id获取Category
	 * 
	 * @param categoryId
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
     Category getCategoryByCategoryId(Integer categoryId,Integer languageid,Integer websiteid);
	
	/**
	 * 根据cpath获取categoryId
	 * 
	 * @return
	 */
	public HashMap<String, Integer> getCategoryPath(Integer website);
	
	/**
	 * 根据cpath获取CategoryPath
	 * 
	 * @return
	 */
	public HashMap<String, CategoryPath> getCategoryPathV2(Integer website);
	/**
	 * 获取品类id 对应名称
	 * 
	 * @param websiteid
	 * @return
	 */
	public HashMap<Integer,String> getCategoryIdName(Integer lang,Integer siteId);

}
