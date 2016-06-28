package com.tomtop.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.ListingIdNum;

public interface ProductUrlMapper {

	@Select("select cn.cname from t_product_category_mapper cm "
			+ "LEFT JOIN t_category_name cn on cm.icategory=cn.icategoryid and cn.ilanguageid=1 "
			+ "LEFT JOIN t_category_website cw on cw.icategoryid=cn.icategoryid "
			+ "where cm.clistingid=#{0} "
			+ "order by cw.ilevel desc LIMIT 1")
	String getCategoryName(String listingId);
	
	@Select("select iid as num,clistingid as listingId from t_product_base where iwebsiteid=#{0}")
	List<ListingIdNum> getProduct(Integer siteId);
	@Select("select iid as num,clistingid as listingId from t_product_base where iwebsiteid=#{0} and clistingid=#{1}")
	List<ListingIdNum> getProductByListingId(Integer siteId,String listingId);
	
	int updateProductUrl(@Param("curl") String curl, 
						@Param("clistingid") String listingId,
						@Param("iwebsiteid") int iwebsiteid);
	
}
