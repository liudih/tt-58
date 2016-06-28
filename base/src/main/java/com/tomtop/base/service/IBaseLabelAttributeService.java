package com.tomtop.base.service;

import java.util.List;

import com.tomtop.base.models.bo.BaseLabelAttributeBo;

/**
 * 标签的属性
 * 
 * @author liulj
 *
 */
public interface IBaseLabelAttributeService {

	/**
	 * 获取标签的属性
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<BaseLabelAttributeBo> getListByClientLang(Integer client, Integer lang);

	/**
	 * 类目获取值
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<BaseLabelAttributeBo> getListByCategoryId(Integer categoryId,Integer client,
			Integer lang);

	/**
	 * 标签key获取值
	 * 
	 * @param client
	 * @param lang
	 * @return
	 */
	List<BaseLabelAttributeBo> getListByKey(String key,Integer client,
			Integer lang);

}