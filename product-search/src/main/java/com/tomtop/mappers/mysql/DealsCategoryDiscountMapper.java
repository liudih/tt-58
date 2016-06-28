package com.tomtop.mappers.mysql;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.tomtop.entity.DealsCategory;

public interface DealsCategoryDiscountMapper {

	@Select("select category_id categoryId,cname cname,cpath cpath,is_discount "
			+ "from deals_category_discount where client_id=#{0} and is_discount=1 and is_enabled=1 and is_deleted=0")
	List<DealsCategory> getDealsCategory(Integer client);
}
