package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.Currency;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseRecommend;
import com.tomtop.entity.SearchDepotProduct;
import com.tomtop.entity.TopSellersProduct;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.OrderEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.entity.index.ProductTypeEntity;
import com.tomtop.entity.index.ReviewStartNum;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.mappers.mysql.TopSellersRecMapper;
import com.tomtop.services.ISearchIndex;
import com.tomtop.services.ISearchService;
import com.tomtop.services.ITopSellersRecService;
import com.tomtop.utils.ProductPublicUtil;
/**
 * top-sellers 专区
 * @author renyy
 *
 */
@Service
public class TopSellersRecServiceImpl extends BaseService implements ITopSellersRecService {
	
	@Autowired
	ISearchService searchService;
	@Autowired
	TopSellersRecMapper topSellersRecMapper;
	@Autowired
	ProductPublicUtil productPublicUtil;
	@Autowired
	ISearchIndex searchIndex;
	
	/**
	 * 获取top-sellers首页的数据
	 */
	@Cacheable(value = "top_sellers_product", key = "#root.caches[0].name + '_' + #client", cacheManager = "noCacheManager")
	@Override
	public List<TopSellersProduct> getTopSellersProductList(Integer client) {
		return topSellersRecMapper.getTopSellersProduct(client);
	}

	/**
	 * 用于更新获取top-sellers首页的数据缓存
	 *
	 */
	@CachePut(value = "top_sellers_product", key = "#root.caches[0].name + '_' + #client" , cacheManager = "noCacheManager")
	@Override
	public List<TopSellersProduct> putTopSellersProductList(Integer client) {
		return topSellersRecMapper.getTopSellersProduct(client);
	}
	
	/**
	 * 获取top-sellers专区首页展示数据
	 */
	@Override
	public LinkedHashMap<String, List<SearchDepotProduct>> getProductMap(
			Integer website, Integer client, Integer lang, String currency) {
		List<TopSellersProduct> tspList = getTopSellersProductList(client);
		if(tspList != null && tspList.size() > 0){
			List<String> listingIds = new ArrayList<String>();
			
			for (TopSellersProduct topSellers : tspList) {
				listingIds.add(topSellers.getListingId());
			}
			LinkedHashMap<String, List<SearchDepotProduct>> pmap = new LinkedHashMap<String, List<SearchDepotProduct>>();
			List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
			if(ieList != null && ieList.size() > 0){
				Currency cbo = this.getCurrencyBean(currency);
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					SearchDepotProduct vo = new SearchDepotProduct();
					ReviewStartNum rs = ie.getReview();
					if(rs != null){
						vo.setAvgScore(rs.getStart());
						vo.setReviewCount(rs.getCount());
					}
					vo.setCollectNum(ie.getColltes());
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBaseDepot(vo, ie, cbo);
					if(ie.getMutil() != null && ie.getMutil().getProductTypes() != null 
							&& ie.getMutil().getProductTypes().size() > 0){
						String cname = "";
						String cpath = "";
						for (ProductTypeEntity pte : ie.getMutil().getProductTypes()) {
							if(pte.getLevel() == 1){
								cname = pte.getProductTypeName();
								cpath = pte.getCpath();
							}
						}
						String pkey = cname + "|" + cpath;
						if(pmap.containsKey(pkey)){
							List<SearchDepotProduct> sdpList = pmap.get(pkey);
							sdpList.add(vo);
							pmap.put(pkey, sdpList);
						}else{
							List<SearchDepotProduct> sdpList = new ArrayList<SearchDepotProduct>();
							sdpList.add(vo);
							pmap.put(pkey, sdpList);
						}
					}
				}
			}
			return pmap;
		}
		
		return null;
	}

	/**
	 * 根据类目获取top-sellers商品
	 */
	@Override
	public ProductBaseDepotSearchKeyword getProductTopSellers(String keyword,
			Integer website, Integer lang, String currency, Integer categoryId,
			Integer page, Integer size, String sort) {
		ProductBaseDepotSearchKeyword pbvo = new ProductBaseDepotSearchKeyword();
		List<SearchDepotProduct> sdplist = new ArrayList<SearchDepotProduct>();
		PageBean bean = new PageBean();
		//设置关键字
		//this.setKeyword(keyword, bean);
		//设置过滤条件
		this.setDepotPageBean(bean, website, categoryId, lang, page, size);
		PageBean returnBean = searchIndex.queryByBeanV3(bean,categoryId,"","hot");
		if(returnBean == null){
			return null;
		}
		List<IndexEntity> ieList = returnBean.getIndexs();
		if(ieList != null && ieList.size() > 0){
			Currency cbo = this.getCurrencyBean(currency);
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				SearchDepotProduct vo = new SearchDepotProduct();
				ReviewStartNum rs = ie.getReview();
				if(rs != null){
					vo.setAvgScore(rs.getStart());
					vo.setReviewCount(rs.getCount());
				}
				vo.setCollectNum(ie.getColltes());
				//通过公共的方法设置父类的属性
				productPublicUtil.transformProductBaseDepot(vo, ie, cbo);
				sdplist.add(vo);
			}
			pbvo.setPblist(sdplist);
			//等待搜索引擎返回结果集的页数
			Integer count = (int) returnBean.getTotalCount();
			Page pageObj = Page.getPage(page, count, size);
			pbvo.setPage(pageObj);
		}
		return pbvo;
	}
	
	/**
	 * 设置过滤条件和聚合
	 * 
	 */
	public void setDepotPageBean(PageBean bean,Integer website,Integer categoryId,
			Integer lang,Integer page,Integer size){
		bean.setLanguageName(getLangCode(lang));//设置语言名字
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",true,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("webSites",website,"&&");
		bean.getFilters().add(filter);	
		filter = new Filter("depots.status","1","==",false);
		bean.getFilters().add(filter);
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		bean.getFilters().add(filter);
		
		filter = new Filter("tagsName.tagName","hot","&&");
		bean.getFilters().add(filter);
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&");
			bean.getFilters().add(filter);
		}
		
		this.setDepotSort(bean);//设置排序
	}
	
	/**
	 * 设置排序
	 * 
	 */
	public void setDepotSort(PageBean bean){
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		OrderEntity oe = null;
		oe = new OrderEntity("tagOrder.sort", 1, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
		orders.add(oe);
		oe = new OrderEntity("salesTotalCount", 2, "desc");//根据最近销量降序
		orders.add(oe);
		oe = new OrderEntity("releaseTime", 3, "asc");//根据上架时间升序
		orders.add(oe);
		bean.setOrders(orders);
	}

	/**
	 * 获取hot-sellers商品
	 */
	@Override
	public List<ProductBaseRecommend> getHotSellersProduct(Integer website,
			Integer lang, String currency,Integer categoryId,Integer page, Integer size,String depotName) {
		List<ProductBaseRecommend> pbrList = new ArrayList<ProductBaseRecommend>();
		PageBean bean = new PageBean();
		this.setDepotPageBean(bean, website, categoryId, lang, page, size);
		bean = searchIndex.queryByBeanV3(bean, 0, "","hot");
		if(bean == null){
			return pbrList;
		}
		List<IndexEntity> ieList = bean.getIndexs();
		if(ieList != null && ieList.size() > 0){
			Currency cbo = this.getCurrencyBean(currency);
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				if(ie != null){
					ProductBaseRecommend pbr = new ProductBaseRecommend();
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(pbr, ie, cbo,depotName);
					pbr.setCollectNum(ie.getColltes());
					ReviewStartNum recount = ie.getReview();
					if(recount != null ){
						pbr.setReviewCount(recount.getCount());
						pbr.setAvgScore(recount.getStart());
					}
					if(pbr.getNowprice() == null){
						continue;
					}
					pbrList.add(pbr);
				}
			}
		}
	
		return pbrList;
	}
	
}
