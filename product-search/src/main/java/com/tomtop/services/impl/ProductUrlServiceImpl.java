package com.tomtop.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.ListingIdNum;
import com.tomtop.mappers.product.ProductUrlMapper;
import com.tomtop.services.IProductUrlService;
import com.tomtop.utils.CommonsUtil;

@Service
public class ProductUrlServiceImpl implements IProductUrlService {

	@Autowired
	ProductUrlMapper productUrlMapper;
	
	@Override
	public Map<String, String> updateProductUrl(Integer website) {
		Map<String, String> map = new HashMap<String, String>();
		List<ListingIdNum> list = productUrlMapper.getProduct(website);
		if(list != null && list.size() > 0 ){
			for (ListingIdNum listingIdNum : list) {
				String listingId = listingIdNum.getListingId();
				Integer id = listingIdNum.getNum();
				String cname = productUrlMapper.getCategoryName(listingId);
				if(cname == null || "".equals(cname)){
					continue;
				}
				cname = CommonsUtil.replaceNoEnStr(cname.toLowerCase());
				String newUrl = cname + "/p_" + id.toString();
				
				int res = productUrlMapper.updateProductUrl(newUrl, listingId, website);
				if(res <= 0){
					map.put("error", listingId + " || " + newUrl);
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, String> updateProductUrlByListingId(String listingId,Integer website) {
		Map<String, String> map = new HashMap<String, String>();
		List<ListingIdNum> list = productUrlMapper.getProductByListingId(website, listingId);
		if(list != null && list.size() > 0 ){
			for (ListingIdNum listingIdNum : list) {
				Integer id = listingIdNum.getNum();
				String cname = productUrlMapper.getCategoryName(listingId);
				if(cname == null || "".equals(cname)){
					continue;
				}
				cname = CommonsUtil.replaceNoEnStr(cname.toLowerCase());
				String newUrl = cname + "/p_" + id.toString();
				
				int res = productUrlMapper.updateProductUrl(newUrl, listingId, website);
				if(res <= 0){
					map.put("error", listingId + " || " + newUrl);
				}
			}
		}
		return map;
	}
}
