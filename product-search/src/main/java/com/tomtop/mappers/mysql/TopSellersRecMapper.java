package com.tomtop.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.TopSellersProduct;

public interface TopSellersRecMapper {

	@Select("select category_id categoryId,cname cname,cpath cpath,listing_id listingId,sku sku "
			+ "from top_sellers_rec where client_id=#{0} and is_enabled=1 and is_deleted=0")
	List<TopSellersProduct> getTopSellersProduct(Integer client);
}
