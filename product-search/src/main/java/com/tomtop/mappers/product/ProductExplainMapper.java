package com.tomtop.mappers.product;


import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.ProductExplain;

public interface ProductExplainMapper {

	@Select("select iid,iwebsiteid,ilanguageid,ctype,ccontent from t_all_product_explain "
			+ "where ctype = #{0} and iwebsiteid = #{1} and ilanguageid = #{2}")
	public ProductExplain getProductExplainByType(String type,Integer siteId,Integer langId);
}
