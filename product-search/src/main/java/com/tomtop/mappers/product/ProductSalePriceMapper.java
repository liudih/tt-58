package com.tomtop.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.ProductSalePrice;

public interface ProductSalePriceMapper {


	@Select("SELECT p.fsaleprice,p.csku,p.dbegindate,p.denddate,p.dcreatedate,p.ccreateuser FROM t_product_saleprice p "
			+ "WHERE clistingid = #{listingId} and p.dbegindate is not null and p.denddate is not null "
			+ "ORDER BY p.denddate desc")
	List<ProductSalePrice> getAllProductSalePriceByListingId(String listingId);

	/**
	 * 该listingId商品是否是折扣商品
	 * 
	 * @param listingId
	 * @return 1：折扣商品 0：非折扣商品
	 */
	@Select("select count(clistingid) from t_product_saleprice where clistingid = #{0}")
	int isOffPrice(String listingId);
}