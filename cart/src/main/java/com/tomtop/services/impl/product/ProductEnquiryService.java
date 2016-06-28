package com.tomtop.services.impl.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.mappers.product.ProductBaseMapper;
import com.tomtop.services.product.IProductEnquiryService;
import com.tomtop.valueobjects.CartItem;
import com.tomtop.valueobjects.product.ProductLite;
import com.tomtop.valueobjects.product.Weight;

@Service
public class ProductEnquiryService implements IProductEnquiryService {

	@Autowired
	ProductBaseMapper productBaseMapper;

	public List<ProductLite> getProductLiteByListingIDs(
			List<String> listingIDs, int websiteID, int languageID) {
		if (listingIDs != null && listingIDs.size() > 0) {
			List<ProductLite> plist = Lists.newArrayList();
			try {
				plist = productBaseMapper.getProductByListingIDs(listingIDs,websiteID, languageID);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return plist;
		}
		return Lists.newArrayList();
	}

	public List<Weight> getWeightList(List<String> listingIDs) {
		// modify by lijun
		if (listingIDs == null || listingIDs.size() == 0) {
			throw new NullPointerException("listingIds is null");
		}
		return productBaseMapper.getWeightByListingIDs(listingIDs);
	}

	public List<ProductLite> getProductLiteForListingIDsAndStorageId(
			List<CartItem> items, int websiteID, int languageID,
			int storageId) {
		List<ProductLite> list = null;
		try{
			List<String> listingIDs = new ArrayList<String>();
			for (CartItem item : items) {
				listingIDs.add(item.getClistingid());
			}
			
			list = productBaseMapper.getProductForListingIdsAndStorageIds(listingIDs, websiteID, languageID, storageId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		 return list;
	}

}
