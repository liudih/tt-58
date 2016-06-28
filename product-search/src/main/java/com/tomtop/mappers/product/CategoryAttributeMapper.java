package com.tomtop.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.CategoryAttributes;

public interface CategoryAttributeMapper {

	/**
	 * 查询类目所有显示属性
	 * @param categoryId
	 * @param lang
	 * 
	 * @author renyy
	 */
	@Select("select ckeyname keyName,cvaluename valueName from t_category_filter_attribute a1 "
			+ "LEFT JOIN  t_attribute_value_namev2 v2 on a1.iattributevalueid=v2.ivalueid and v2.ilanguageid=#{1} "
			+ "LEFT JOIN  t_attribute_key_namev2 k2 on a1.iattributekeyid=k2.ikeyid and v2.ilanguageid=k2.ilanguageid "
			+ "where a1.icategoryid=#{0} and a1.bisshow=true and ckeyname is not null and cvaluename is not null "
			+ "order by isort")
	List<CategoryAttributes> getCategoryAttributesByCategory(Integer categoryId, Integer lang);
	
	/**
	 * 查询类目所有显示属性的key值
	 * @param categoryId
	 * @param lang
	 * 
	 * @author renyy
	 */
	@Select("select ckeyname keyName from t_category_attributekey_relation r1,t_attribute_key_namev2 k2 "
			+ "where r1.iattributekeyid=k2.ikeyid and k2.ilanguageid=#{1} and bisshow=true and "
			+ "icategoryid=#{0} order by isort")
	List<String> getAttributesKey(Integer categoryId,Integer lang);
	
}
