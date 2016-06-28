package com.tomtop.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.entry.po.ProductBase;


public interface ProductBaseMapper {


	@Select("<script>"
			+ "SELECT b.clistingid,b.csku,b.fprice,b.fweight,b.fheight,b.flength,b.fwidth,b.fvolumeweight,M.ilogisticstemplateid templateid"
			+ " from   t_product_base b INNER JOIN t_product_storage_map M ON b.clistingid = M .clistingid "
			+ "where b.clistingid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach>"
			+ "and M.istorageid= #{storageId}"
			+" and M.ilogisticstemplateid  is not null"
			+" LIMIT 1 "
			+ "</script>")
	List<ProductBase> getProductsByListingIds(@Param("list")List<String> listingIds,@Param("storageId")int storageId,@Param("language")int language);
	
	@Select("SELECT b.clistingid,b.csku,b.fprice,b.fweight,b.fheight,b.flength,b.fwidth,b.fvolumeweight,M.ilogisticstemplateid templateid "
			+ " from   t_product_base b INNER JOIN t_product_storage_map M ON b.clistingid = M .clistingid  "
	+ "where b.clistingid =#{listingId} "
	+ "and M.istorageid= #{storageId} "
	+ " and M.ilogisticstemplateid  is not null "
	+ " LIMIT 1")
	ProductBase getProductsItem(@Param("listingId")String listingId,@Param("storageId")int storageId);
}
