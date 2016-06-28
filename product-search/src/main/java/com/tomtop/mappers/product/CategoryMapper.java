package com.tomtop.mappers.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.Category;
import com.tomtop.entity.CategoryLable;
import com.tomtop.entity.CategoryPath;

public interface CategoryMapper {
	
	/**
	 * 品类Id
	 * 
	 * @author renyy
	 * @param categoryId
	 * @param langId
	 * @param siteId
	 * @return
	 */
	@Select("select cn.icategoryid categoryId,cn.cname as name,cba.ilevel as level,cba.iparentid,cba.cpath from t_category_name cn "
			+ "INNER JOIN t_category_base cba on cn.icategoryid=cba.iid "
			+ "INNER JOIN t_category_website cw on cw.icategoryid=cba.iid "
			+ "where cn.ilanguageid=#{1} and cn.icategoryid=#{0} and cw.iwebsiteid=#{2}")
	public CategoryLable getCategoryLable(Integer categoryId,Integer langId,Integer siteId);
	
	/**
	 * 品类路径查询
	 * 
	 * @author renyy
	 * @param path
	 * @return
	 */
	@Select("select cn.icategoryid categoryId,cn.cname as name,cba.ilevel as level,cba.iparentid, cba.cpath "
			+ "from t_category_name cn INNER JOIN t_category_base cba "
			+ "on cn.icategoryid=cba.iid where cn.ilanguageid=#{1} and "
			+ "cba.cpath=#{0}")
	public CategoryLable getCategoryLableByPath(String path,Integer langId);
	
	/**
	 * listingId查询面包屑
	 * 
	 * @author renyy
	 * @param listingId
	 * @return
	 */
	@Select("select cn.icategoryid categoryId,cn.cname as name,cba.ilevel as level,cba.iparentid,cba.cpath from t_product_category_mapper pcm "
			+ "LEFT JOIN t_category_name cn on pcm.icategory=cn.icategoryid "
			+ "LEFT JOIN t_category_base cba on cn.icategoryid=cba.iid "
			+ "INNER JOIN t_category_website cw on cw.icategoryid=cba.iid "
			+ "where cn.ilanguageid=#{1} and pcm.clistingid=#{0} and cw.iwebsiteid=#{2} "
			+ "order by cn.icategoryid LIMIT 3")
	public List<CategoryLable> getCategoryLableByListingId(String listingId,Integer langId,Integer siteId);
	

	/**
	 * 查询品类路径
	 * 
	 * @author renyy
	 * @param siteId
	 * @return
	 */
	@Select("select icategoryid as id,cpath as path,ilevel as level from t_category_website where iwebsiteid=#{0} order by icategoryid")
	public List<CategoryPath> getCategoryPathDto(Integer siteId);
	
	/**
	 * 查询类目
	 * 
	 * @author lijun
	 * @param paras
	 * @return
	 */
	List<Category> getCategories(Map<String, Object> paras);

}
