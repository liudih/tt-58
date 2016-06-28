package com.tomtop.dao.impl.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.dao.product.IProductLabelDao;
import com.tomtop.dto.product.ProductLabel;
import com.tomtop.mappers.product.ProductLabelMapper;

@Service
public class ProductLabelDao implements IProductLabelDao {
	@Autowired
	ProductLabelMapper productLabelMapper;

	@Override
	public List<ProductLabel> getProductLabel(String clistingid) {
		return this.productLabelMapper.getProductLabel(clistingid);
	}

	@Override
	public List<ProductLabel> getBatchProductLabel(List<String> clistingids) {
		return this.productLabelMapper.getBatchProductLabel(clistingids);
	}

	@Override
	public List<ProductLabel> getBatchProductLabelByType(
			List<String> clistingids, String type) {
		return this.productLabelMapper.getBatchProductLabelByType(clistingids,
				type);
	}

	@Override
	public int getListingIdByTypeByPageTotalCount(String type, int iwebsiteid,
			List<String> listingIds) {
		return this.productLabelMapper.getListingIdByTypeByPageTotalCount(type,
				iwebsiteid, listingIds);
	}

	@Override
	public ProductLabel getProductLabelById(Integer id) {
		return this.productLabelMapper.getProductLabelById(id);
	}

}
