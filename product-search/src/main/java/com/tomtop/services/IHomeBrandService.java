package com.tomtop.services;

import java.util.List;

import com.tomtop.entity.HomeBrand;


/**
 * 首页品牌管理
 * 
 * @author liulj
 *
 */
public interface IHomeBrandService {
	
	List<HomeBrand> getHomeBrand(Integer client, Integer lang);
}