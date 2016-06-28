package com.tomtop.services.impl;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.services.ISearchIndex;

/**
 * 索引查询
 * @author ztiny
 * @Date 2015-12-19
 */
@Component
public class SearchIndexImpl  extends BaseQueryBuildImpl implements ISearchIndex {

	Logger logger = Logger.getLogger(SearchIndexImpl.class);
	
	/**
	 * 返回索引的ID
	 * @param indexName 索引名称
	 * @param indexType 索引类型
	 * @param hashMap 查询关键字
	 * @return
	 */
	@Override
	public List<String> search(String indexName,String indexType,Map<String,Object> hashMap){
		if(hashMap==null || hashMap.size()<1){
			return null;
		}
		List<String> ids = new ArrayList<String>();
		try{
			SearchRequestBuilder requestBuilder = this.getRequestBuilder(indexName,indexType);
			requestBuilder.setQuery(accurateMustMatch(hashMap));
			SearchHits hits = requestBuilder.execute().actionGet().getHits();
			for (SearchHit searchHit : hits) {
				ids.add(searchHit.getId());
			} 
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ids;
	}

	/**
	 * 查询
	 * @param bean
	 * @return
	 */
	public PageBean queryBykey(PageBean bean){
		int prodtypeId = 0;
		if(bean.getFilters()!=null && bean.getFilters().size()>0){
			for (Filter filter : bean.getFilters()) {
				if(filter.getPropetyName().equals("mutil.productTypes.productTypeId")){
					Object obj = filter.getPropertyValue();
					prodtypeId = obj==null?0:(Integer)obj;
					break;
				}
			}
		}
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean);
		//添加过滤条件
		QueryBuilder queryBuilder = this.buildQueryBuilder(bean.getKeyword(),bean.getFilters());
		requestBuilder = requestBuilder.setQuery(queryBuilder);
		long count = this.getCount(requestBuilder);
		logger.info(requestBuilder.toString());
		//排序
		requestBuilder = this.autoOrder(requestBuilder,bean,prodtypeId);
		//聚合
		requestBuilder = this.addAggreationgBuilders(requestBuilder, bean.getFilters(),bean.getRangeAgg(),prodtypeId).setSize(1000);
		SearchHits hits = null;
		try{
			SearchResponse response = requestBuilder.setFrom(bean.getBeginNum()).setSize(bean.getEndNum()).execute().actionGet();
			bean = handler(bean,response.getAggregations(),prodtypeId);
			hits = response.getHits();
		}catch(Exception ex){
			ex.printStackTrace();
		}
 		
		return this.handerHit(hits, bean,count);
	}
	

	
	
	/**
	 * 返回索引的PageBean，该方法适用于 not_analyzed 类型的字段查询
	 * @param bean 
	 * @param hashMap 查询关键字<属性名称,属性值>
	 * @return
	 */
	@Override
	public PageBean searchByKey(PageBean bean, Map<String, Object> hashMap) {
		if(hashMap==null || hashMap.size()<1){
			return null;
		}
		try{
			SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean.getIndexName(),bean.getIndexType());
			requestBuilder.setQuery(accurateMustMatch(hashMap));
			SearchHits hits = requestBuilder.execute().actionGet().getHits();
			long count = hits.getTotalHits();
			bean = this.handerHit(hits, bean,count);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 推荐查询
	 * @param bean
	 * @param proName 属性名称
	 * @return
	 */
	public PageBean queryMoreLikeThis(PageBean bean,String proName){
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean);
		BoolQueryBuilder boolQuery = this.buildQueryBuilderByFilters(bean.getFilters());
		if(StringUtils.isBlank(proName)){
			proName = "mutil.title";
		}
		MoreLikeThisQueryBuilder moreLikeQuery = QueryBuilders.moreLikeThisQuery(proName).like(bean.getKeyword()).analyzer("standard").boost(0.8f).minTermFreq(1).maxQueryTerms(12).minDocFreq(1);
		requestBuilder = requestBuilder.setQuery(boolQuery.must(moreLikeQuery));
		requestBuilder = this.autoOrder(requestBuilder,bean);
		SearchHits hits = requestBuilder.setFrom(bean.getBeginNum()).setSize(bean.getEndNum()).execute().actionGet().getHits();
		logger.info(requestBuilder.toString());
		long count = hits.getTotalHits();
		return  this.handerHit(hits, bean,count);
	}
	
	
	
	/**
	 *  属性匹配多个值的精准查询
	 *  @param indexName 索引名称
	 *  @param indexType 索引类型
	 *  @param proName 属性名称
	 *  @param filters 过滤条件
	 *  @param objs 属性对应的多个值
	 */
	@SuppressWarnings("unchecked")
	public PageBean queryTermsAnyValue(PageBean bean,String proName){
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean.getIndexName(),bean.getIndexType());
		BoolQueryBuilder boolQuery = this.buildQueryBuilderByFilters(bean.getFilters());
		ArrayList<String> values  =  new ArrayList<String>() ;
		SearchHits hits = null;
		try{
			values = JSON.parseObject(bean.getKeyword(), ArrayList.class);
			hits = requestBuilder.setQuery(boolQuery.must(this.accurateMatch(proName,values))).execute().actionGet().getHits();
		}catch(Exception ex){
			hits = requestBuilder.setQuery(boolQuery.must(this.accurateMatch(proName, bean.getKeyword()))).execute().actionGet().getHits();
		}
		
		//System.out.println(requestBuilder.toString());
		long count = hits.getTotalHits();
		return this.handerHit(hits, bean,count);
	}
	
	
	/**
	 * 根据多个listingid查询
	 * @param indexName
	 * @param indexType
	 * @param ids listingid的集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageBean queryByIds(PageBean bean){
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean.getIndexName(), bean.getIndexType());
		ArrayList<String> ids = null;
		try {
			ids = new ObjectMapper().readValue(bean.getKeyword(), ArrayList.class);
		} catch (Exception ex) {
			logger.error("queryByIds ids is for json error");
			ex.printStackTrace();
		}
		if(ids != null){
			SearchHits hits = requestBuilder.setQuery(QueryBuilders.idsQuery().ids(ids)).setSize(ids.size()).execute().actionGet().getHits();
			long count = hits.getTotalHits();
			return this.handerHit(hits, bean, count);
		}else{
			return null;
		}
	}




	@Override
	public IndexEntity queryById(String ListingId,String indexName,String indexType) {
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(indexName,indexType);
		SearchHits hits = requestBuilder.setQuery(QueryBuilders.idsQuery().ids(ListingId)).execute().actionGet().getHits();
		return this.handerHit(hits);
	}

	/**
	 * 根据多个listingid加关键字条件过滤排序
	 * 
	 * @param bean
	 * @param ids listingid的集合
	 * @return
	 */
	@Override
	public PageBean queryByIdsAndFilter(PageBean bean,List<String> ids){
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean.getIndexName(), bean.getIndexType());
		//添加过滤条件
		QueryBuilder queryBuilder = buildQueryBuilderFilter(bean.getFilters(),bean.getKeyword(), ids);
		requestBuilder = requestBuilder.setQuery(queryBuilder);
		requestBuilder = this.autoOrder(requestBuilder,bean);//排序
		SearchResponse response = requestBuilder.setFrom(bean.getBeginNum()).setSize(bean.getEndNum()).execute().actionGet();
		SearchHits hits = response.getHits();
		long count = hits.getTotalHits();
		return this.handerHit(hits, bean, count);
	}
	
	/**
	 * 查询
	 * @param bean
	 * @return
	 */
	@Override
	public PageBean queryByBean(PageBean bean){
		int prodtypeId = 0;
		if(bean.getFilters()!=null && bean.getFilters().size()>0){
			for (Filter filter : bean.getFilters()) {
				if(filter.getPropetyName().equals("mutil.productTypes.productTypeId")){
					Object obj = filter.getPropertyValue();
					prodtypeId = obj==null?0:(Integer)obj;
					break;
				}
			}
		}
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean);
		//添加过滤条件
		QueryBuilder queryBuilder = this.buildQueryBuilder(bean.getKeyword(),bean.getFilters());
		requestBuilder = requestBuilder.setQuery(queryBuilder);
		long count = this.getCount(requestBuilder);
		logger.info(requestBuilder.toString());
		//排序
		requestBuilder = this.autoOrder(requestBuilder,bean,prodtypeId);
		//聚合
		requestBuilder = this.addAggreationgBuilders(requestBuilder, bean.getFilters(),bean.getRangeAgg(),prodtypeId).setSize(1000);
		SearchHits hits = null;
		try{
			SearchResponse response = requestBuilder.setFrom(bean.getBeginNum()).setSize(bean.getEndNum()).execute().actionGet();
			bean = handler(bean,response.getAggregations(),prodtypeId);
			hits = response.getHits();
		}catch(Exception ex){
			ex.printStackTrace();
		}
 		
		return this.handerHit(hits, bean,count);
	}
	
	/**
	 * 查询(V2)
	 * @param bean
	 * @return
	 */
	@Override
	public PageBean queryByBeanV2(PageBean bean){
		int prodtypeId = 0;
		if(bean.getFilters()!=null && bean.getFilters().size()>0){
			for (Filter filter : bean.getFilters()) {
				if(filter.getPropetyName().equals("mutil.productTypes.productTypeId")){
					Object obj = filter.getPropertyValue();
					prodtypeId = obj==null?0:(Integer)obj;
					break;
				}
			}
		}
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean);
		//添加过滤条件
		QueryBuilder queryBuilder = this.buildQueryBuilderV2(bean.getKeyword(),bean.getFilters());
		requestBuilder = requestBuilder.setQuery(queryBuilder);
		long count = this.getCount(requestBuilder);
		logger.info(requestBuilder.toString());
		//排序
		requestBuilder = this.autoOrder(requestBuilder,bean,prodtypeId);
		logger.info(requestBuilder.toString());
		//聚合
		requestBuilder = this.addAggreationgBuilders2(requestBuilder, bean.getFilters(),bean.getRangeAgg(),prodtypeId).setSize(1000);
		SearchHits hits = null;
		try{
			SearchResponse response = requestBuilder.setFrom(bean.getBeginNum()).setSize(bean.getEndNum()).execute().actionGet();
			bean = handler(bean,response.getAggregations(),prodtypeId);
			hits = response.getHits();
		}catch(Exception ex){
			ex.printStackTrace();
		}
 		
		return this.handerHit(hits, bean,count);
	}
	
	/**
	 * 查询(V3)
	 * 分片查询 去除聚合
	 * @param bean
	 * @return
	 */
	@Override
	public PageBean queryByBeanV3(PageBean bean,Integer prodtypeId,String depotName,String tagName){
		//获取查询客户端
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean);
		//添加过滤条件
		QueryBuilder queryBuilder = this.buildQueryBuilderV2(bean.getKeyword(),bean.getFilters());
		//设置过滤条件
		requestBuilder = requestBuilder.setQuery(queryBuilder);
		//获取总数
		long count = this.getCount(requestBuilder);
		//排序
		requestBuilder = this.autoOrder(requestBuilder,bean,prodtypeId,depotName,tagName);
		logger.info(requestBuilder.toString());
		SearchHits hits = null;
		try{
			//System.out.println("scroll 模式启动！");
			int begin = bean.getBeginNum();//从第几条记录开始
			int end = bean.getEndNum();//获取多少条记录
			//int size = end / 5;//记录数除以分片数
			SearchResponse response = requestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setSize(end)
									.setScroll(TimeValue.timeValueMinutes(6000)).get();
			count = response.getHits().getTotalHits();//第一次不返回数据
			if(begin > 0){
				begin = begin - end;
				for(int sum=0; sum<count;){
					if(sum > begin){
						break;
					}
					response = this.getSearchScrollRequestBuilder(response.getScrollId()).setScroll(TimeValue.timeValueMinutes(6000)).get();
				    sum += response.getHits().hits().length;
				  //  System.out.println("总量"+count+" 已经查到"+sum);
				}
			}
			hits = response.getHits();
		}catch(Exception ex){
			ex.printStackTrace();
		}
 		
		return this.handerHit(hits, bean,count);
	}
	/**
	 * 获取聚合结果集
	 * @param bean 聚合条件
	 * @param prodtypeId 类目Id
	 * @param 聚合的属性key list 可为null
	 * @param 聚合属性 key对应value list 可为null
	 * @return
	 */
	@Override
	public PageBean getAggreagetionsBean(PageBean bean,Integer prodtypeId,List<String> keyList,Map<String,List<String>> mapList){
		//获取查询客户端
		SearchRequestBuilder requestBuilder = this.getRequestBuilder(bean);
		//添加过滤条件
		QueryBuilder queryBuilder = this.buildQueryBuilderV2(bean.getKeyword(),bean.getFilters());
		//设置过滤条件
		requestBuilder = requestBuilder.setQuery(queryBuilder);
		//添加聚合条件
		requestBuilder = this.addAggreationgBuilders3(requestBuilder, bean.getFilters(),bean.getRangeAgg(),prodtypeId,keyList,mapList).setSize(1000);
		//查询聚合
		SearchResponse response = requestBuilder.setFrom(0).setSize(1).get();
		//获取聚合
		bean = handler(bean,response.getAggregations(),prodtypeId);
		
		return bean;
	}

	
	
}
