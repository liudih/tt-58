package com.tomtop.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.base.models.dto.AttributeDto;
import com.tomtop.base.product.mappers.ProductAttributeMapper;
import com.tomtop.base.service.IProductAttributeService;

@Service
public class ProductAttributeServiceImpl implements IProductAttributeService {

	@Autowired
	ProductAttributeMapper productAttributeMapper;
	
	@Override
	public HashMap<String, String> getProductAttributeMap(Integer lang) {
		HashMap<String, String> attMap = new HashMap<String, String>();
		List<AttributeDto> attList = productAttributeMapper.getAttributeAttributeDto(1);
		AttributeDto attdto = null;
		String key = "";
		String value = "";
		for (int i = 0; i < attList.size(); i++) {
			attdto = attList.get(i);
			key = attdto.getKeyName().trim();
			value = attdto.getValueName().trim();
			attMap.put(value, key);
			//System.out.println(key + " ====== " + value);
		}
		return attMap;
	}

	@Override
	public HashMap<String, List<String>> getProductAttributeKeyList(Integer lang) {
		HashMap<String, List<String>> attMap = new HashMap<String, List<String>>();
		List<AttributeDto> attList = productAttributeMapper.getAttributeAttributeDto(1);
		AttributeDto attdto = null;
		String key = "";
		String value = "";
		List<String> strList = null;
		for (int i = 0; i < attList.size(); i++) {
			attdto = attList.get(i);
			key = attdto.getKeyName().trim();
			value = attdto.getValueName().trim();
			if(attMap.containsKey(key)){
				strList = attMap.get(key);
			}else{
				strList = new ArrayList<String>();
			}
			strList.add(value);
			attMap.put(key, strList);
		}
		return attMap;
	}

}
