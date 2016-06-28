package com.tomtop.base.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.base.mappers.CategoryShapeMapper;
import com.tomtop.base.models.dto.CategoryShapeDto;
import com.tomtop.base.service.ICategoryShapeService;

@Service
public class CategoryShapeServiceImpl implements ICategoryShapeService {

	@Autowired
	CategoryShapeMapper categoryShapeMapper;
	
	@Override
	public Map<String, Integer> getCategoryShapeType(Integer client) {
		List<CategoryShapeDto> cslist = categoryShapeMapper.getCategoryShape(client);
		HashMap<String,Integer> smap = new HashMap<String,Integer>();
		if(cslist != null && cslist.size() > 0){
			for (CategoryShapeDto categoryShape : cslist) {
				smap.put(categoryShape.getPath(), categoryShape.getType());
			}
		}
		return smap;
	}

}
