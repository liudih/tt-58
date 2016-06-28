package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.OrderEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.services.IBeforeSearchService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.CommonDefn;

/**
 * 获取搜索引擎数据类
 */
@Service
public class SearchServiceImpl extends BaseService implements ISearchService {
	
	Logger logger = Logger.getLogger(SearchServiceImpl.class);
	
	@Resource(name="beforeSearchServiceImpl")
	private IBeforeSearchService beforeSearchServiceImpl;
	
	private PageBean query(PageBean bean) {
		if(bean == null){
			bean = new PageBean();
			bean.setResutlCode("400");
			return bean;
		}
		try {
			List<Filter> filters = bean.getFilters();
			//添加默认的类目聚合
			boolean productTypeFilter = true;
			for (Filter filter : filters) {
				//判断类目聚合属性是否存在，如果存在,则不添加
				if(StringUtils.equals(filter.getPropetyName(), "mutil.productTypes.productTypeId")){
					productTypeFilter = false;
				}
			}
			if(productTypeFilter){
				Filter filter = new Filter("mutil.productTypes.productTypeId",null,"&&",true,false);
				bean.getFilters().add(filter);
			}
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			
			bean = this.setBeanDefaultParams(bean, bean.getLanguageName(), bean.getWebSites(), bean.getKeyword());
			
			bean = beforeSearchServiceImpl.query(bean);
			
		} catch (Exception e) {
			bean.setResutlCode("500");
			e.printStackTrace();
		}
		return bean;
	}
	

	/**
	 * 全局底部推荐位 Your Recently Viewed Items and Featured Recommendations
	 * @param web
	 * @param language
	 * @param size
	 * @param key
	 * @param request
	 * @return
	 */
	private PageBean queryMoreLikeByRecently(String web, String language,
			Integer size, String key,Boolean isDepot) {
		PageBean bean =  new PageBean();
		List<IndexEntity> indexs = bean.getIndexs();
		logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+key);
		logger.info("==========================qRecentlylike method be called=============================");
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(key)){
				return null;
			}
			
			ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
			//30天销量降序
			OrderEntity orderTotal = new OrderEntity();
			orderTotal.setPropetyName("salesTotalCount");
			orderTotal.setType("desc");
			orders.add(orderTotal);
			//上架时间
			OrderEntity orderReleateTime = new OrderEntity();
			orderReleateTime.setPropetyName("releaseTime");
			orderReleateTime.setType("asc");
			orders.add(orderReleateTime);
			
			List<Filter> filters = new ArrayList<Filter>();
			if(isDepot){
				//当为多仓库的时候
				//已下架商品过滤
				Filter filter = new Filter("depots.status","1","&&");
				filters.add(filter);
				//过滤库存为0的商品
				Filter storeNumFilter = new Filter("depots.qty",0,"!=");
				filters.add(storeNumFilter);
			}else{
				//已下架商品过滤
				Filter filter = new Filter("status","1","&&");
				filters.add(filter);
				//过滤库存为0的商品
				Filter storeNumFilter = new Filter("storeNum",0,"!=");
				filters.add(storeNumFilter);
			}
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			filters.add(activeFilter);
			
			
			setBeanDefaultParams(bean,language,web,key);
			bean.setEndNum(size);
			bean.setOrders(orders);
			bean.setFilters(filters);
			indexs = beforeSearchServiceImpl.queryYouRecentlyLike(bean);
			bean.setIndexs(indexs);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}

	/**
	 * 详情页 Customers Who Bought This Item Also Bought
	 * @param web
	 * @param language
	 * @param size
	 * @param key
	 * @param isDepot
	 *          是否为多仓库
	 * @return
	 */
	private PageBean queryMoreLikeForCustomersItem(String web, String language,
			Integer size, String key,Boolean isDepot) {
		PageBean bean =  new PageBean();
		List<IndexEntity> indexs = bean.getIndexs();
		logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+key);
		logger.info("==========================qMorelikeForCustomersItem method be called=============================");
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(key)){
				return null;
			}
			
			ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
			//30天销量降序
			OrderEntity orderTotal = new OrderEntity();
			orderTotal.setPropetyName("salesTotalCount");
			orderTotal.setType("desc");
			orders.add(orderTotal);
			//上架时间
			OrderEntity orderReleateTime = new OrderEntity();
			orderReleateTime.setPropetyName("releaseTime");
			orderReleateTime.setType("asc");
			orders.add(orderReleateTime);
			
			List<Filter> filters = new ArrayList<Filter>();
			if(isDepot){
				//当为多仓库的时候
				//已下架商品过滤
				Filter filter = new Filter("depots.status","1","&&");
				filters.add(filter);
				//过滤库存为0的商品
				Filter storeNumFilter = new Filter("depots.qty",0,"!=");
				filters.add(storeNumFilter);
			}else{
				//已下架商品过滤
				Filter filter = new Filter("status","1","&&");
				filters.add(filter);
				//过滤库存为0的商品
				Filter storeNumFilter = new Filter("storeNum",0,"!=");
				filters.add(storeNumFilter);
			}
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			filters.add(activeFilter);
			

			
			setBeanDefaultParams(bean,language,web,key);
			bean.setEndNum(size);
			bean.setOrders(orders);
			bean.setFilters(filters);
			indexs = beforeSearchServiceImpl.queryMoreLikeForCustomersItem(bean);
			bean.setIndexs(indexs);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}


	/**
	 * 详情页 Customers Who Viewed This Item Also Viewed
	 * @param web
	 * @param language
	 * @param size
	 * @param key
	 * @param request
	 * @return
	 */
	private PageBean queryMoreLikeForCustomersViewed(String web,
			String language, Integer size, String key,Boolean isDepot) {
	logger.info("==========================qMorelikeForCustomersViewed method be called=============================");
		
		PageBean bean =  new PageBean();
		List<IndexEntity> indexs = bean.getIndexs();
		logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+key);
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(key)){
				return null;
			}
			
			ArrayList<OrderEntity> orders = new ArrayList<OrderEntity>();
			
			//上架时间
			OrderEntity orderReleateTime = new OrderEntity();
			orderReleateTime.setPropetyName("releaseTime");
			orderReleateTime.setType("asc");
			orders.add(orderReleateTime);
			
			//30天销量降序
			OrderEntity orderTotal = new OrderEntity();
			orderTotal.setPropetyName("salesTotalCount");
			orderTotal.setType("desc");
			orders.add(orderTotal);
			
			List<Filter> filters = new ArrayList<Filter>();
			if(isDepot){
				//当为多仓库的时候
				//已下架商品过滤
				Filter filter = new Filter("depots.status","1","&&");
				filters.add(filter);
				//过滤库存为0的商品
				Filter storeNumFilter = new Filter("depots.qty",0,"!=");
				filters.add(storeNumFilter);
			}else{
				//已下架商品过滤
				Filter filter = new Filter("status","1","&&");
				filters.add(filter);
				//过滤库存为0的商品
				Filter storeNumFilter = new Filter("storeNum",0,"!=");
				filters.add(storeNumFilter);
			}
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			filters.add(activeFilter);
			
			setBeanDefaultParams(bean,language,web,key);
			bean.setEndNum(size);
			bean.setOrders(orders);
			bean.setFilters(filters);
			indexs = beforeSearchServiceImpl.queryMoreLikeForCustomersViewed(bean);
			bean.setIndexs(indexs);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}


	/**
	 * 根据ListingId查询
	 * @param key
	 * @return
	 */
	private PageBean queryByListingId(String language, String web,
			String listingid) {
		logger.info("==========================qlistingid method be called=============================");
		PageBean bean =  new PageBean();
		try{
			if(StringUtils.isBlank(language)|| StringUtils.isBlank(listingid)){
				bean.setResutlCode("400");
			}
			logger.info("language value is :"+language + "==web value is :"+web+"==key value is :"+listingid);
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			setBeanDefaultParams(bean,language,web,listingid);
			bean = beforeSearchServiceImpl.queryByListingId(bean);
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}

	/**
	 * 首页根据listingids查询
	 * @param listingids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PageBean queryHome(String language, String web, String listingids) {
		logger.info("================qhome method be called================");
		PageBean bean = new PageBean();
		if(StringUtils.isBlank(language)|| StringUtils.isBlank(web)||StringUtils.isBlank(listingids)){
			bean.setResutlCode("400");
			return bean;
		}
		logger.info("language value is :"+language + "    ,==web value is :"+web+"   ,==listingids value is :"+listingids);
		try{
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			bean = setBeanDefaultParams(bean,language,web,listingids);
			bean = beforeSearchServiceImpl.queryHomePage(bean);
			if(bean != null){
				List<IndexEntity> indexs  = bean.getIndexs();
				if(!indexs.isEmpty()){
					//排序
					ArrayList<String> ids  =  JSON.parseObject(bean.getKeyword(), ArrayList.class);
					for (int i=0;i<ids.size();i++) {
						String id =  ids.get(i);
						for (int j=i;j<indexs.size();j++) {
							IndexEntity nid = indexs.get(j);
							IndexEntity old = indexs.get(i);
							if(nid.getListingId().equals(id)){
								indexs.set(j, old);
								indexs.set(i, nid);
								break;
							}
						}
					}
				}
			}
		}catch(Exception ex){
			bean.setResutlCode("500");
			ex.printStackTrace();
		}
		return bean;
	}


	/**
	 * 热门查询
	 * @param language 语言
	 * @param websiteId 站点
	 * @return
	 */
	private PageBean queryHot(String language, String websiteId,Integer size) {
		logger.info("==========================qhot method be called=============================");
		PageBean bean =  new PageBean();
		if(StringUtils.isBlank(language)|| StringUtils.isBlank(websiteId)){
			bean.setResutlCode("400");
			return bean;
		}
		try {
			bean.setWebSites(websiteId);
			bean.setLanguageName(language);
			bean.setEndNum(size);
			Filter filter = new Filter("tagsName.tagName","hot","&&");
			bean.getFilters().add(filter);
			
			OrderEntity orderModel = new OrderEntity("tagsName.releaseTime",1,"desc");
			List<OrderEntity> orders = bean.getOrders();
			orders.add(orderModel);
			
			//过滤活动商品
			Filter activeFilter = new Filter();
			activeFilter.setExpress("==");
			activeFilter.setPropetyName("bactivity");
			activeFilter.setPropertyValue(false);
			bean.getFilters().add(activeFilter);
			//已下架商品过滤
			Filter statusfilter = new Filter("status","1","&&");
			bean.getFilters().add(statusfilter);
			//过滤库存为0的商品
			Filter storeNumFilter = new Filter("storeNum",0,"!=");
			bean.getFilters().add(storeNumFilter);
			
			bean = beforeSearchServiceImpl.query(bean);
			
		} catch (Exception e) {
			bean.setResutlCode("500");
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 设置PageBean的默认参数
	 * @param bean
	 * @param language 	语言
	 * @param web		站点
	 * @param keyword 	关键字
	 * @return
	 */
	private PageBean setBeanDefaultParams(PageBean bean,String language,String web,String keyword){
		if(bean==null){
			bean = new PageBean();
		}
		if(StringUtils.isNotBlank(language)){
			bean.setLanguageName(language);
		}
		if(StringUtils.isNotBlank(web)){
			String webs[] = web.split(",");
			int size = webs.length;
			for (String siteId : webs) {
				String express = "&&";
				if(size>1){
					express = "||";
				}
				Filter webFilter = new Filter("webSites",siteId,express);
				bean.getFilters().add(webFilter);	
			}
		}
		
		if(StringUtils.isNotBlank(keyword)){
			bean.setKeyword(keyword);
		}
		return bean;
	}
	
	/**
	 * 获取搜索引擎对象(单个)
	 * 
	 * @param listingId
	 * @param langId
	 * @param siteId
	 * 
	 */
	@Override
	public IndexEntity getSearchProduct(String listingId, Integer langId,
			Integer siteId) {
		String lang = getLangCode(langId);
		PageBean bean = this.queryByListingId(lang, siteId.toString(), listingId);
		IndexEntity index = bean.getIndexs().size() > 0 ? bean.getIndexs().get(0) : null;
		return index;
	}

	/**
	 * 根据过滤条件 获取搜索引擎对象集合 (多个)
	 * 
	 * @param PageBean
	 * @param langId
	 * 
	 * return List<IndexEntity>
	 */
	@Override
	public List<IndexEntity> getSearchProductList(PageBean bean, Integer langId) {
		String lang = getLangCode(langId);
		bean.setLanguageName(lang);
		PageBean resBean = this.query(bean);
		return resBean.getIndexs();
	}
	
	/**
	 * 根据过滤条件 获取搜索引擎分页对象集合
	 * 
	 * @param PageBean
	 * @param langId
	 * 
	 * return PageBean
	 * 
	 */
	@Override
	public PageBean getSearchPageBean(PageBean bean, Integer langId) {
		String lang = getLangCode(langId);
		bean.setLanguageName(lang);
		return this.query(bean);
	}
	
	/**
	 * 根据listingIds 获取搜索引擎对象集合
	 * 
	 * @param listingIds
	 * @param langId
	 * @param siteId
	 */
	@Override
	public List<IndexEntity> getSearchProductList(String listingIds,
			Integer langId, Integer siteId) {
		String lang = getLangCode(langId);
		PageBean pageBean = this.queryHome(lang, siteId.toString(), listingIds);
		List<IndexEntity> indexList = null;
		if(pageBean != null){
			indexList = pageBean.getIndexs();
		}
		return indexList;
	}

	/**
	 * 根据listingIds 获取Map搜索引擎对象集合
	 * 
	 * @param listingIds
	 * @param langId
	 * @param siteId
	 */
	@Override
	public Map<String,IndexEntity> getSearchProductMap(String listingIds,
			Integer langId, Integer siteId) {
		String lang = getLangCode(langId);
		PageBean pageBean = this.queryHome(lang, siteId.toString(), listingIds);
		Map<String,IndexEntity> indexMap = new HashMap<String, IndexEntity>();
		if(pageBean != null && pageBean.getIndexs() != null){
			List<IndexEntity> indexList = pageBean.getIndexs();
			for (IndexEntity ie : indexList) {
				indexMap.put(ie.getListingId(), ie);
			}
		}
		return indexMap;
	}
	/**
	 * 搜索引擎获取热门商品
	 * 
	 * @param langId
	 * @param siteId
	 * @param size
	 */
	@Override
	public List<IndexEntity> getSearchProductHotList(Integer langId,
			Integer siteId,Integer size) {
		String lang = getLangCode(langId);
		PageBean pageBean = this.queryHot(lang, siteId.toString(),size);
		List<IndexEntity> indexList = pageBean.getIndexs();
		return indexList;
	}

	
	/**
	 * 根据获取推荐位的数据对象
	 * 
	 * @param listingId
	 * @param langId
	 * @param siteId
	 * @param type
	 * 
	 */
	@Override
	public PageBean getSearchPageBeanRecommendByType(String listingId,
			Integer langId, Integer siteId, String type,Integer size) {
		PageBean pageBean = null;
		String lang = getLangCode(langId);
		boolean isDepot = false;
		if(siteId == CommonDefn.ONE){
			isDepot = true;
		}
		if(CommonDefn.BOUGHT.equals(type)){
			pageBean = this.queryMoreLikeForCustomersItem(siteId.toString(), lang, size, listingId,isDepot);
		}
		if(CommonDefn.VIEWED.equals(type)){
			pageBean = this.queryMoreLikeForCustomersViewed(siteId.toString(), lang, size, listingId,isDepot);
		}
		if(CommonDefn.RECOMMEND.equals(type)){
			pageBean = this.queryMoreLikeByRecently(siteId.toString(), lang, size, listingId,isDepot);
		}
		return pageBean;
	}


}
