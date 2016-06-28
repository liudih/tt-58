package com.tomtop.services;


import java.util.List;

import com.tomtop.entity.HomeSearchKeyword;

/**
 * 首页搜索关键字
 * 
 * @author liulj
 *
 */
public interface IHomeSearchKeywordService {

	/**
	 * 获取关键字list
	 * 
	 * @param categoryId
	 *            类目id
	 * @param client
	 *            客户端id
	 * @param lang
	 *            语言id
	 * @return
	 */
	List<HomeSearchKeyword> getKeywordList(int categoryId, int client,
			int lang);
	
	/**
	 * 获取关键字补全list
	 * 
	 * @param keyword
	 *            要搜索的关键字
	 * @param client
	 *            客户端id
	 * @param lang
	 *            语言id
	 * @return
	 */
	List<String> getKeywordAutoList(String keyword, int client,
			int lang);

}