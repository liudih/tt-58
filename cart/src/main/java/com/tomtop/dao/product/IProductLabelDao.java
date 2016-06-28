package com.tomtop.dao.product;

import java.util.List;

import com.tomtop.dto.product.ProductLabel;

public interface IProductLabelDao {

	List<ProductLabel> getProductLabel(String clistingid);

	List<ProductLabel> getBatchProductLabel(List<String> clistingids);

	List<ProductLabel> getBatchProductLabelByType(List<String> clistingids,
			String type);

	int getListingIdByTypeByPageTotalCount(String type, int iwebsiteid,
			List<String> listingIds);

	ProductLabel getProductLabelById(Integer id);

}
