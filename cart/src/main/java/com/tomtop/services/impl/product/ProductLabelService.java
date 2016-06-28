package com.tomtop.services.impl.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.dao.product.IProductLabelDao;
import com.tomtop.dto.product.ProductLabel;
import com.tomtop.services.product.IProductLabelService;
import com.tomtop.utils.FoundationService;

@Service
public class ProductLabelService implements IProductLabelService {

	@Autowired
	FoundationService foundationService;

	@Autowired
	IProductLabelDao labelDao;

	@Override
	public List<String> getListByListingIdsAndType(List<String> listingIds,
			String type) {
		List<ProductLabel> labels = this.labelDao.getBatchProductLabelByType(
				listingIds, type);
		return Lists.transform(labels, e -> e.getClistingid());
	}

	/**
	 * 通过clistingid来查询Label
	 * 
	 * @param clistingid
	 * @return
	 */
	@Override
	public List<ProductLabel> getProductLabel(String clistingid) {
		return this.labelDao.getProductLabel(clistingid);
	}

	/**
	 * @param type
	 * @param listingIds
	 * @param siteId
	 * @return int toalCount
	 */
	public int getListingIdByTypeByPageTotalCount(String type, int iwebsiteid,
			List<String> listingIds) {
		return this.labelDao.getListingIdByTypeByPageTotalCount(type,
				iwebsiteid, listingIds);
	}

	public ProductLabel getProductLabelById(Integer id) {
		return labelDao.getProductLabelById(id);
	}
}
