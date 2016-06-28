package com.tomtop.services;


import java.util.List;
import java.util.Map;

import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.PageBean;

/**
 * 查询
 * @author ztiny
 * @Date 2015-12-19
 */
public interface ISearchIndex {
	
	/**
	 * 返回索引的ID
	 * @param indexName 索引名称
	 * @param indexType 索引类型
	 * @param hashMap 查询关键字
	 * @return
	 */
	public List<String> search(String indexName,String indexType,Map<String,Object> hashMap);
	

	/**
	 * 精准查询 
	 * @param bean
	 * @param paramMap  查询关键字<属性名称,属性值>
	 * @return
	 */
	public PageBean searchByKey(PageBean bean, Map<String, Object> hashMap);
	
	/**
	 * 索引条件查询
	 * @param pageBean 封装查询条件
	 * @return
	 */
	public PageBean queryBykey(PageBean bean);
	
	/**
	 * 推荐查询
	 * @param bean
	 * @param 属性名称
	 * @return
	 */
	public PageBean queryMoreLikeThis(PageBean bean,String proName);

	/**
	 * 属性匹配多个值的精准查询
	 * @param bean
	 * @param proName	属性名称
	 * @return
	 */
	public PageBean queryTermsAnyValue(PageBean bean,String proName);
	
	/**
	 * 根据多个listingid查询
	 * @param Bean
	 * @return
	 */
	public PageBean queryByIds(PageBean bean);
	
	/**
	 * 根据ID查询单个IndexEntity
	 * @param listingId 
	 * @param indexName
	 * @param indexType
	 */
	public IndexEntity queryById(String ListingId,String indexName,String indexType);
	
	/**
	 * 根据多个listingid查询 并且根据条件过滤
	 * @param Bean
	 * @return
	 */
	public PageBean queryByIdsAndFilter(PageBean bean,List<String> ids);
	
	/**
	 * 索引条件查询(queryBykey重写)
	 * @param pageBean 封装查询条件
	 * @return
	 */
	public PageBean queryByBean(PageBean bean);
	
	/**
	 * 索引条件查询(V2)
	 * @param pageBean 封装查询条件
	 * @return
	 */
	public PageBean queryByBeanV2(PageBean bean);
	/**
	 * 索引条件查询(V3)
	 * @param pageBean 封装查询条件
	 * @param prodtypeId 类目Id
	 * 
	 * @return
	 */
	public PageBean queryByBeanV3(PageBean bean,Integer prodtypeId,String depotName,String tagName);
	/**
	 * 获取聚合
	 * @param bean
	 * @param prodtypeId 类目Id
	 * @param keyList 属性Key列表
	 * @param mapList key对应value
	 * @return
	 */
	public PageBean getAggreagetionsBean(PageBean bean,Integer prodtypeId,List<String> keyList,Map<String,List<String>> mapList);
}
