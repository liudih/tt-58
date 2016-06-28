package com.tomtop.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.entity.Category;
import com.tomtop.entity.CategoryLable;
import com.tomtop.entity.StorageParent;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.services.ICategoryService;
import com.tomtop.services.IStorageService;

/**
 * 产品分类
 * 
 * @author lijun
 *
 */
@RestController
public class CategoryController {

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IStorageService storageService;

	private final static String CATEGORY_TYPE = "category";
	private final static String DETAIL_TYPE = "detail";

	/**
	 * 获取分类
	 * 
	 * @param level
	 *            要获取分类的层级,如果没有传该参数,那么默认去的第一级分类(1,2...)
	 * @param parentId
	 *            要获取parentId下面的直接子分类
	 * @param languageid
	 *            语言ID
	 * @param websiteid
	 *            站点ID
	 */
	@RequestMapping(value = "/ic/v1/categories", method = RequestMethod.GET)
	public Result get(
			@RequestParam(value = "level", required = false) Integer level,
			@RequestParam(value = "parentId", required = false) Integer parentId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {
		
		List<Category> list = categoryService.getCategoryList(level, parentId, languageid, website);
		if(list == null || list.size() == 0){
			return new Result(Result.FAIL, null);
		}
		return new Result(Result.SUCCESS, list.toArray());
	}

	/**
	 * 根具类目id获取类目信息
	 * 
	 * @param categoryId
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
	@Cacheable(value = "product_category", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/categories/{categoryId}", method = RequestMethod.GET)
	public Result getByCategoryId(
			@PathVariable("categoryId") Integer categoryId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website) {

		Category category = categoryService.getCategoryByCategoryId(categoryId, languageid, website);
		if (category != null) {
			return new Result(Result.SUCCESS, category);
		}
		return new Result(Result.FAIL, null);
	}

	/**
	 * 获取面包屑
	 * 
	 * @param categoryId
	 * @param lang
	 * @param type
	 * 
	 * @author renyy
	 * @return
	 */
	@Cacheable(value = "product_category", keyGenerator = "customKeyGenerator", cacheManager="dayCacheManager")
	@RequestMapping(method = RequestMethod.GET, value = "/ic/v1/bread/{id}")
	public Result getCategoryBreadCrumbs(
			@PathVariable("id") String id,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "type", required = false, defaultValue = "category") String type) {

		List<CategoryLable> clbList = null;
		if (CATEGORY_TYPE.equals(type)) {
			Integer categoryId = Integer.parseInt(id);
			clbList = categoryService.getCategoryLableBreadCrumbs(categoryId,
					languageid,website);
		}
		if (DETAIL_TYPE.equals(type)) {
			clbList = categoryService.getCategoryLableBreadCrumbsByListingId(id,
					languageid,website);
		}
		if (clbList != null && clbList.size() > 0) {
			return new Result(Result.SUCCESS, clbList);
		} else {
			return new Result(Result.FAIL, null);
		}
	}

	/**
	 * 获取Local Warehouse
	 * 
	 * @param lang
	 * @param client
	 * @param country
	 * 
	 * @author renyy
	 * @return
	 */
	@Cacheable(value = "product_category", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET, value = "/ic/v1/categories/storage")
	public Result getCategoryStorage(
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "country", required = false, defaultValue = "US") String country) {

		List<StorageParent> csboList = storageService.getAllStorageParent();

		if (csboList != null && csboList.size() > 0) {
			return new Result(Result.SUCCESS, csboList);
		} else {
			return new Result(Result.FAIL, null);
		}
	}
}
