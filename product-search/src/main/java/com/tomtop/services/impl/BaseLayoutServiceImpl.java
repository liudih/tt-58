package com.tomtop.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.BaseLayout;
import com.tomtop.mappers.mysql.BaseLayoutMapper;
import com.tomtop.services.IBaseLayoutService;

/**
 * 布局
 * 
 * @author renyy
 *
 */
@Service
public class BaseLayoutServiceImpl implements IBaseLayoutService {

	@Autowired
	BaseLayoutMapper baseLayoutMapper;
	
	@Override
	public List<BaseLayout> getBaseLayoutByCode(String code, int client,
			int lang) {
		return baseLayoutMapper.getBaseLayoutByCode(code, client, lang);
	}

}
