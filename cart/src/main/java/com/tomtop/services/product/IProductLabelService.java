package com.tomtop.services.product;

import java.util.List;

import com.tomtop.dto.product.ProductLabel;

public interface IProductLabelService {

	List<String> getListByListingIdsAndType(List<String> listingIds, String type);

	List<ProductLabel> getProductLabel(String clistingid);

}
