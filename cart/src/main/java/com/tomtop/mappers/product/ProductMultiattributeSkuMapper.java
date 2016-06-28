package com.tomtop.mappers.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tomtop.dto.product.ProductMultiattributeItem;
import com.tomtop.dto.product.ProductMultiattributeSku;

public interface ProductMultiattributeSkuMapper {

	List<ProductMultiattributeItem> getMultiattributeProductList(
			@Param("list") List<String> listingIDs);

	@Select("select * from t_product_multiattribute_sku where csku=#{0} ")
	ProductMultiattributeSku select(String sku);

	@Select("select csku from t_product_multiattribute_sku where cparentsku=#{0}")
	List<String> getChildSkus(String parentSku);

}