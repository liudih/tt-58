package com.tomtop.services.impl.product;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.dto.product.ProductEntityMap;
import com.tomtop.mappers.product.ProductBaseMapper;
import com.tomtop.mappers.product.ProductEntityMapMapper;
import com.tomtop.services.product.IEntityMapService;
import com.tomtop.utils.FoundationService;

@Service
public class EntityMapService implements IEntityMapService {
	@Autowired
	ProductEntityMapMapper mapMapper;

	@Autowired
	ProductBaseMapper baseMapper;

	@Autowired
	FoundationService foundationService;

	@Override
	public Map<String, String> getAttributeMap(String listingID,
			Integer languageID) {
		List<ProductEntityMap> attributes = getProductEntityMapByListingId(
				listingID, languageID);
		Map<String, String> attributeMap = Maps.newHashMap();
		if (null != attributes && attributes.size() > 0) {
			for (ProductEntityMap productEntityMap : attributes) {
				String ckeyName = productEntityMap.getCkeyname();
				String cvalueName = productEntityMap.getCvaluename();
				if (null != ckeyName && null != cvalueName) {
					attributeMap.put(ckeyName, cvalueName);
				}
			}
		}
		return attributeMap;
	}
	
	@Override
	public List<ProductEntityMap> getProductEntityMapByListingId(
			String listingId, Integer lang) {
		return mapMapper.getProductEntityMapByListingId(listingId, lang);
	}

}
