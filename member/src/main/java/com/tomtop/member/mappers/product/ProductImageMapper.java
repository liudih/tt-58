package com.tomtop.member.mappers.product;



import org.apache.ibatis.annotations.Select;



public interface ProductImageMapper {

	@Select("select cimageurl from  t_product_image  where iorder = 1 and bsmallimage = true and clistingid =#{1}")
	String getProductSmallImageForMemberViewsByListingId(String listingId);

}