package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.Currency;
import com.tomtop.entity.PriceRangePoly;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseSearchKeyword;
import com.tomtop.entity.ProductDepotSearchKeywordAggSort;
import com.tomtop.entity.SearchDepotProduct;
import com.tomtop.entity.SearchProduct;
import com.tomtop.entity.index.AggregationEntity;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.OrderEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.entity.index.RangeAggregation;
import com.tomtop.entity.index.ReviewStartNum;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.services.IAttributesService;
import com.tomtop.services.IBaseQueryBuild;
import com.tomtop.services.IEsProductSearchKeywordService;
import com.tomtop.services.IPriceRangPolyService;
import com.tomtop.services.ISearchIndex;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

/**
 * 搜索引擎查询业务类
 * 
 * @author renyy
 *
 */
@Service
public class EsProductSearchKeywordServiceImpl extends BaseService implements
		IEsProductSearchKeywordService {

	private static final Logger logger = LoggerFactory
			.getLogger(EsProductSearchKeywordServiceImpl.class);
	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	IPriceRangPolyService priceRangPolyService;
	@Autowired
	ProductPublicUtil productPublicUtil;
	@Autowired
	ISearchIndex searchIndex;
	@Autowired
	IBaseQueryBuild baseQueryBuild;
	@Autowired
	IAttributesService attributesService;
	
	/**
	 * 通过搜索引擎 根据keyword获取商品列表聚合
	 * 
	 * @param keyword (如果keyword等于空,则根据分类path获取类目数据列表)
	 * 
	 * @param website  站点
	 * 
 	 * @param lang 语言Id
	 * 
	 * @param categoryId 品类Id
	 * 
	 * @param page 页数
	 *  
	 * @param size 大小
	 *   
	 * @param sort 排序
	 * 
	 * @param bmain 是否为主商品
	 * 
	 * @param tagName 标签名
	 *  
	 * @param depotName 仓库名
	 *   
	 * @param brand 品牌
	 *    
	 * @param priceRange 价格区间
	 * 
	 * @return ProductBaseSearchKeywordVo
	 * @author renyy
	 */
	@Override
	public ProductBaseSearchKeyword getProductBaseSearch(String keyword, Integer website, 
			Integer lang,String currency,Integer categoryId,Integer level,Integer page,Integer size,
			String sort,boolean bmain,String tagName,String depotName,String brand,String yjprice,String type) {
		ProductBaseSearchKeyword pbvo = new ProductBaseSearchKeyword();
		List<SearchProduct> pbpList = new ArrayList<SearchProduct>();
		PageBean bean = new PageBean();
		//设置关键字
		this.setKeyword(keyword, bean);
		//设置过滤条件
		this.setPageBean(bean, website, categoryId, level, lang, page, size, sort,bmain,tagName,depotName,brand,yjprice,type);
		PageBean returnBean = searchService.getSearchPageBean(bean, lang);
		if(returnBean == null){
			return null;
		}
		List<IndexEntity> ieList = returnBean.getIndexs();
		if(ieList != null && ieList.size() > 0){
			Currency cbo = this.getCurrencyBean(currency);
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				SearchProduct vo = new SearchProduct();
				ReviewStartNum rs = ie.getReview();
				if(rs != null){
					vo.setAvgScore(rs.getStart());
					vo.setReviewCount(rs.getCount());
				}
				vo.setCollectNum(ie.getColltes());
				vo.setFreeShipping(ie.getIsFreeShipping());
				//通过公共的方法设置父类的属性
				productPublicUtil.transformProductBase(vo, ie, cbo);
				pbpList.add(vo);
			}
			Map<String,List<AggregationEntity>> aggsMap = null;
			boolean isFilter = isFilter(tagName, brand, yjprice,type,depotName);
			//判断是否有过滤条件,如果有则聚合不增加过滤条件
			if(isFilter){
				PageBean beanTwo = new PageBean();
				//设置关键字
				this.setKeyword(keyword, bean);
				this.setPageBeanTwo(beanTwo, website,page,size, categoryId, bmain);
				PageBean returnBeanTwo = searchService.getSearchPageBean(beanTwo, lang);
				aggsMap = returnBeanTwo.getAggsMap();
			}else{
				aggsMap = returnBean.getAggsMap();
			}
			pbvo.setAggsMap(aggsMap);
			pbvo.setPblist(pbpList);
			//等待搜索引擎返回结果集的页数
			Integer count = (int) returnBean.getTotalCount();
			Page pageObj = this.getPageObj(page, count, size);
			pbvo.setPage(pageObj);
		}
		return pbvo;
	}
	
	/**
	 * 设置过滤条件和聚合
	 * 
	 */
	public void setPageBean(PageBean bean,Integer client,Integer categoryId,Integer level,
			Integer lang,Integer page,Integer size,String sort,boolean bmain,
			String tagName,String depotName,String brand,String yjprice,String type){
		bean.setWebSites(client.toString());//站点
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",bmain,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("status",2,"!=",false);//设置status不等于2的过滤条件不聚合
		bean.getFilters().add(filter);
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		//处理tagName
		if(tagName == null || "".equals(tagName)){
			filter = new Filter("tagsName.tagName","","&&",true,false);//聚合商品标签 不作为过滤条件
		}else{
			filter = new Filter("tagsName.tagName",tagName,"&&",false);//只作为过滤
		}
		bean.getFilters().add(filter);
		
		//filter = new Filter("depots.depotName","CN","&&",false);
		//bean.getFilters().add(filter);
		
		//处理depotName
//		if(depotName == null || "".equals(depotName)){
//			filter = new Filter("depots.depotName","","&&",true,false);//聚合仓库标签 不作为过滤条件
//		}else{
//			filter = new Filter("depots.depotName",depotName,"&&",true);//聚合仓库标签 过滤
//		}
//		bean.getFilters().add(filter);
		
		//处理brand
		if(brand == null || "".equals(brand)){
			filter = new Filter("brand","","&&",true,false);//聚合品牌标签 不作为过滤条件
		}else{
			filter = new Filter("brand",brand,"&&",false);//只作为过滤
		}
		bean.getFilters().add(filter);
		
		//价格过滤和聚合区间
		Map<Integer, List<PriceRangePoly>> prmap = priceRangPolyService.getPriceRangeMap();
		if(prmap == null || prmap.size() == 0){
			logger.error("price range poly Map not find");
		}
		List<PriceRangePoly> rrlist = prmap.get(client);
		if(rrlist != null && rrlist.size() > 0){
			for (PriceRangePoly prp : rrlist) {
				if(yjprice != null && !"".equals(yjprice)){
					if(prp.getAliases().equals(yjprice)){
						if(prp.getGreater() != null && !"".equals(prp.getGreater())){
							filter = new Filter(prp.getFilterName(),prp.getGreater(),">=",false,true);//作为过滤条件不作为聚合
							bean.getFilters().add(filter);
						}
						if(prp.getLess() != null && !"".equals(prp.getLess())){
							filter = new Filter(prp.getFilterName(),prp.getLess(),"<=",false,true);//作为过滤条件不作为聚合
							bean.getFilters().add(filter);
						}
					}
				}
				//价格范围
				RangeAggregation range = new RangeAggregation();
				range.setName(prp.getFilterName());
				range.setAliasName(prp.getAliases());
				if(prp.getGreaterAgg() != null){
					range.setFrom(prp.getGreaterAgg());
				}
				if(prp.getLessAgg() != null){
					range.setTo(prp.getLessAgg());
				}
				bean.getRangeAgg().add(range);
			}
		
		}
//		filter = new Filter("mutil.items.key","","&&",true,false);//聚合属性标签 不作为过滤条件
//		bean.getFilters().add(filter);
		if(type == null || "".equals(type)){
			filter = new Filter("mutil.items.value","","&&",true,false);//商品属性聚合 不作为过滤
		}else{
			filter = new Filter("mutil.items.value",type,"&&",false);//只作为过滤
		}
		//聚合属性标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		this.setSort(bean, sort,categoryId,level);//设置排序
	}
	
	private boolean isFilter(String tagName,String brand,String yjprice,String type,String depotName){
		if(tagName != null && !"".equals(tagName)){
			return true;
		}
		if(brand != null && !"".equals(brand)){
			return true;
		}
		if(yjprice != null && !"".equals(yjprice)){
			return true;
		}
		if(type != null && !"".equals(type)){
			return true;
		}
		if(depotName != null && !"".equals(depotName)){
			return true;
		}
		return false;
	}
	/**
	 * 设置聚合(只聚合)
	 * 
	 */
	public void setPageBeanTwo(PageBean bean,Integer client,Integer page,Integer size,Integer categoryId,boolean bmain){
		bean.setWebSites(client.toString());//站点
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",bmain,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		
		filter = new Filter("status",2,"!=",false);//设置status不等于2的过滤条件不聚合
		bean.getFilters().add(filter);
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		//处理tagName
		filter = new Filter("tagsName.tagName","","&&",true,false);//聚合商品标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		//处理仓库
		//filter = new Filter("depots.depotName","CN","&&",false);
		//bean.getFilters().add(filter);
		
		//处理brand
		filter = new Filter("brand","","&&",true,false);//聚合品牌标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		Map<Integer, List<PriceRangePoly>> prmap = priceRangPolyService.getPriceRangeMap();
		if(prmap == null || prmap.size() == 0){
			logger.error("price range poly Map not find");
		}
		List<PriceRangePoly> rrlist = prmap.get(client);
		if(rrlist != null && rrlist.size() > 0){
			for (PriceRangePoly prp : rrlist) {
				//价格范围
				RangeAggregation range = new RangeAggregation();
				range.setName(prp.getFilterName());
				range.setAliasName(prp.getAliases());
				if(prp.getGreaterAgg() != null){
					range.setFrom(prp.getGreaterAgg());
				}
				if(prp.getLessAgg() != null){
					range.setTo(prp.getLessAgg());
				}
				bean.getRangeAgg().add(range);
			}
		}
			
		filter = new Filter("mutil.items.value","","&&",true,false);//聚合属性标签 不作为过滤条件
		bean.getFilters().add(filter);
	}
	/**
	 * 设置排序
	 * 
	 */
	public void setSort(PageBean bean,String sort,Integer categoryId,Integer level){
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		OrderEntity oe = null;
		if(sort != null && !"".equals(sort)){
			if("pirceAsc".equals(sort)){
				//价格从小到大升序 Price: High to Low - 价格从低到高
				oe = new OrderEntity("yjPrice", 1, "asc");
				orders.add(oe);
			}
			if("pirceDesc".equals(sort)){
				//Price: Low to High - 价格从高到低
				oe = new OrderEntity("yjPrice", 1, "desc");
				orders.add(oe);
			}
			if("reviewCount".equals(sort)){			
				//按评论最多的排序 Most Reviews
				oe = new OrderEntity("review.count", 1, "desc");
				orders.add(oe);
				oe = new OrderEntity("review.start", 2, "desc");
				orders.add(oe);
			}
			if("releaseTime".equals(sort)){
				//按发布时间的排序  Newest - 新上架商品
				oe = new OrderEntity("releaseTime", 1, "desc");
				orders.add(oe);
			}
			if("salesVolume".equals(sort)){
				//按销量的排序 Most Popular
				oe = new OrderEntity("salesTotalCount", 1, "desc");//销量降序
				orders.add(oe);
				oe = new OrderEntity("viewcount", 2, "desc");//浏览量降序
				orders.add(oe);
				oe = new OrderEntity("review.count", 3, "desc");//评论数量降序
				orders.add(oe);
			}
		}else{
			oe = new OrderEntity("categoryOrder.sort", 2, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
			orders.add(oe);
		/*	if(level == 1){
				oe = new OrderEntity("mutil.productLevel1.sort", 1, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
				orders.add(oe);
			}else if(level == 2){
				oe = new OrderEntity("mutil.productLevel2.sort", 1, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
				orders.add(oe);
			}else{
				oe = new OrderEntity("mutil.productLevel3.sort", 1, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
				orders.add(oe);
			}*/
			oe = new OrderEntity("salesTotalCount", 2, "desc");//根据最近30天内销量降序
			orders.add(oe);
			oe = new OrderEntity("viewcount", 3, "desc");//根据历史浏览量降序
			orders.add(oe);
		}
		oe = new OrderEntity("storeNum", 5, "desc");
		orders.add(oe);
		oe = new OrderEntity("status", 6, "asc");
		orders.add(oe);
		bean.setOrders(orders);
	}
	
	/**
	 * 设置关键字到pagebean
	 * @param keyword
	 * @param pageBean
	 */
	public void setKeyword(String keyword,PageBean pageBean){
		if(keyword != null && !"".equals(keyword)){
			pageBean.setKeyword(keyword);
			pageBean.getFilters().add(new Filter("spu",keyword,"||"));
		}
	}
	/**
	 * 获取到Page对象
	 * @param currentPage
	 * @param count
	 * @param pageSize
	 * @return
	 */
	public Page getPageObj(Integer currentPage,Integer count,Integer pageSize){
		return Page.getPage(currentPage, count, pageSize);
	}
	
	/**
	 * 通过搜索引擎 根据keyword获取商品列表聚合
	 * 
	 * @param keyword (如果keyword等于空,则根据分类path获取类目数据列表)
	 * 
	 * @param website  站点
	 * 
 	 * @param lang 语言Id
	 * 
	 * @param categoryId 品类Id
	 * 
	 * @param level 类目等级
	 * 
	 * @param page 页数
	 *  
	 * @param size 大小
	 *   
	 * @param sort 排序
	 * 
	 * @param tagName 标签名
	 *  
	 * @param depotName 仓库名
	 *   
	 * @param brand 品牌
	 *    
	 * @param yjprice 价格区间
	 * 
	 * @param type 属性条件
	 * 
	 * @return ProductBaseDepotSearchKeyword
	 * @author renyy 
	 */
	public ProductBaseDepotSearchKeyword getProductBaseDepotSearch(String keyword, Integer website, 
			Integer lang,String currency,Integer categoryId,Integer level,Integer page,Integer size,
			String sort,String tagName,String depotName,String brand,String yjprice,String type,Double startPrice,Double endPrice) {
		ProductBaseDepotSearchKeyword pbvo = new ProductBaseDepotSearchKeyword();
		List<SearchDepotProduct> sdplist = new ArrayList<SearchDepotProduct>();
		PageBean bean = new PageBean();
		//设置关键字
		this.setKeyword(keyword, bean);
		//设置过滤条件
		this.setDepotPageBean(bean, website, categoryId,level, lang, page, size, sort,tagName,depotName,brand,yjprice,type,startPrice,endPrice);
		PageBean returnBean = searchIndex.queryByBeanV2(bean);
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
			Map<String,List<AggregationEntity>> aggsMap = null;
			boolean isFilter = isFilter(tagName, brand, yjprice,type,depotName);
			//判断是否有过滤条件,如果有则聚合不增加过滤条件
			if(isFilter){
				PageBean beanTwo = new PageBean();
				//设置关键字
				this.setKeyword(keyword, bean);
				this.setDepotPageBeanTwo(beanTwo, website,lang,page,size, categoryId);
				PageBean returnBeanTwo = searchIndex.queryByBeanV2(beanTwo);
				aggsMap = returnBeanTwo.getAggsMap();
			}else{
				aggsMap = returnBean.getAggsMap();
			}
			pbvo.setAggsMap(aggsMap);
			pbvo.setPblist(sdplist);
			//等待搜索引擎返回结果集的页数
			Integer count = (int) returnBean.getTotalCount();
			Page pageObj = this.getPageObj(page, count, size);
			pbvo.setPage(pageObj);
		}
		return pbvo;
	}
	
	
	/**
	 * 设置过滤条件和聚合
	 * 
	 */
	public void setDepotPageBean(PageBean bean,Integer website,Integer categoryId,Integer level,
			Integer lang,Integer page,Integer size,String sort,String tagName,
			String depotName,String brand,String yjprice,String type,Double startPrice,Double endPrice){
		bean.setLanguageName(getLangCode(lang));//设置语言名字
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",true,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("webSites",website,"&&");
		bean.getFilters().add(filter);	
		filter = new Filter("depots.status","1,3,40","&&",false);//设置status过滤条件不聚合
		bean.getFilters().add(filter);
		//过滤活动商品
		filter = new Filter("bactivity",false,"==",false);
		bean.getFilters().add(filter);
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		//处理tagName
		if(tagName == null || "".equals(tagName)){
			filter = new Filter("tagsName.tagName","","&&",true,false);//聚合商品标签 不作为过滤条件
			bean.getFilters().add(filter);
		}else{
			String[] tags = tagName.split(",");
			for (int i = 0; i < tags.length; i++) {
				filter = new Filter("tagsName.tagName",tags[i],"&&",false);//只作为过滤
				bean.getFilters().add(filter);
			}
		}
		
		//处理depotName
		if(depotName == null || "".equals(depotName)){
			filter = new Filter("depots.depotName","","&&",true,false);//聚合仓库标签 不作为过滤条件
		}else{
			filter = new Filter("depots.depotName",depotName,"&&",true);//聚合仓库标签 过滤
		}
		bean.getFilters().add(filter);
		
		//处理brand 
		/*if(brand == null || "".equals(brand)){
			filter = new Filter("brand","","&&",true,false);//聚合品牌标签 不作为过滤条件
		}else{
			filter = new Filter("brand",brand,"&&",false);//只作为过滤
		}
		bean.getFilters().add(filter);*/
		
		//上新多属性版本
		if(brand != null && !"".equals(brand)){
			filter = new Filter("brand",brand,"&&",false);//只作为过滤
			bean.getFilters().add(filter);
		}
		
		if(startPrice >= 0.0 && endPrice > 0.0){
			filter = new Filter("yjPrice",startPrice,">=",false,true);//作为过滤条件不作为聚合
			bean.getFilters().add(filter);
			filter = new Filter("yjPrice",endPrice,"<=",false,true);//作为过滤条件不作为聚合
			bean.getFilters().add(filter);
		}else{
			//价格过滤和聚合区间
			Map<Integer, List<PriceRangePoly>> prmap = priceRangPolyService.getPriceRangeMap();
			if(prmap == null || prmap.size() == 0){
				logger.error("price range poly Map not find");
			}
			List<PriceRangePoly> rrlist = prmap.get(website);
			if(rrlist != null && rrlist.size() > 0){
				for (PriceRangePoly prp : rrlist) {
					if(yjprice != null && !"".equals(yjprice)){
						if(prp.getAliases().equals(yjprice)){
							if(prp.getGreater() != null && !"".equals(prp.getGreater())){
								filter = new Filter(prp.getFilterName(),prp.getGreater(),">=",false,true);//作为过滤条件不作为聚合
								bean.getFilters().add(filter);
							}
							if(prp.getLess() != null && !"".equals(prp.getLess())){
								filter = new Filter(prp.getFilterName(),prp.getLess(),"<=",false,true);//作为过滤条件不作为聚合
								bean.getFilters().add(filter);
							}
						}
					}
					//价格范围
					RangeAggregation range = new RangeAggregation();
					range.setName(prp.getFilterName());
					range.setAliasName(prp.getAliases());
					if(prp.getGreaterAgg() != null){
						range.setFrom(prp.getGreaterAgg());
					}
					if(prp.getLessAgg() != null){
						range.setTo(prp.getLessAgg());
					}
					bean.getRangeAgg().add(range);
				}
			
			}
		}
		//属性聚合过滤
		if(type == null || "".equals(type)){
			filter = new Filter("mutil.items.value","","&&",true,false);//商品属性聚合 不作为过滤
		}else{
			filter = new Filter("mutil.items.value",type.toLowerCase(),"&&",false);//只作为过滤
		}
		bean.getFilters().add(filter);
		//属性聚合过滤
		/*if(type == null || "".equals(type)){
			filter = new Filter("mutil.items.value","","&&",true,false);//商品属性聚合 不作为过滤
			bean.getFilters().add(filter);
		}else{
			if(type.indexOf(",") >= 0){
				String[] ts = type.split(",");
				for (String t : ts) {
					filter = new Filter("mutil.items.value",t,"==",false);//只作为过滤
					bean.getFilters().add(filter);
				}
			}else{
				filter = new Filter("mutil.items.value",type,"&&",false);//只作为过滤
				bean.getFilters().add(filter);
			}
		}*/
		
		this.setDepotSort(bean, sort,depotName);//设置排序
	}
	/**
	 * 设置聚合(只聚合)
	 * 
	 */
	public void setDepotPageBeanTwo(PageBean bean,Integer website,Integer lang,Integer page,Integer size,Integer categoryId){
		bean.setLanguageName(getLangCode(lang));//设置语言名字
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",true,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("webSites",website,"&&");//设置站点
		bean.getFilters().add(filter);	
		filter = new Filter("depots.status","1,3,40","&&",false);//设置status过滤条件不聚合
		bean.getFilters().add(filter);
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		bean.getFilters().add(filter);
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		//处理tagName
		filter = new Filter("tagsName.tagName","","&&",true,false);//聚合商品标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		//聚合仓库
		filter = new Filter("depots.depotName","","&&",true,false);//聚合仓库标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		//处理brand
		//filter = new Filter("brand","","&&",true,false);//聚合品牌标签 不作为过滤条件
		//bean.getFilters().add(filter);
		
		Map<Integer, List<PriceRangePoly>> prmap = priceRangPolyService.getPriceRangeMap();
		if(prmap == null || prmap.size() == 0){
			logger.error("price range poly Map not find");
		}
		List<PriceRangePoly> rrlist = prmap.get(website);
		if(rrlist != null && rrlist.size() > 0){
			for (PriceRangePoly prp : rrlist) {
				//价格范围
				RangeAggregation range = new RangeAggregation();
				range.setName(prp.getFilterName());
				range.setAliasName(prp.getAliases());
				if(prp.getGreaterAgg() != null){
					range.setFrom(prp.getGreaterAgg());
				}
				if(prp.getLessAgg() != null){
					range.setTo(prp.getLessAgg());
				}
				bean.getRangeAgg().add(range);
			}
		}
			
		filter = new Filter("mutil.items.value","","&&",true,false);//聚合属性标签 不作为过滤条件
		bean.getFilters().add(filter);
	}
	/**
	 * 设置排序
	 * 
	 */
	public void setDepotSort(PageBean bean,String sort,String depotName){
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		OrderEntity oe = null;
		if(sort != null && !"".equals(sort)){
			if("pirceAsc".equals(sort)){
				//价格从小到大升序 Price: High to Low - 价格从低到高
				oe = new OrderEntity("yjPrice", 1, "asc");
				orders.add(oe);
			}
			if("pirceDesc".equals(sort)){
				//Price: Low to High - 价格从高到低
				oe = new OrderEntity("yjPrice", 1, "desc");
				orders.add(oe);
			}
			if("reviewCount".equals(sort)){			
				//按评论最多的排序 Most Reviews
				oe = new OrderEntity("review.count", 1, "desc");
				orders.add(oe);
				oe = new OrderEntity("review.start", 2, "desc");
				orders.add(oe);
			}
			if("releaseTime".equals(sort)){
				//按发布时间的排序  Newest - 新上架商品
				oe = new OrderEntity("releaseTime", 1, "desc");
				orders.add(oe);
			}
			if("salesVolume".equals(sort)){
				//按销量的排序 Most Popular
				oe = new OrderEntity("salesTotalCount", 1, "desc");//销量降序
				orders.add(oe);
				oe = new OrderEntity("viewcount", 2, "desc");//浏览量降序
				orders.add(oe);
				oe = new OrderEntity("review.count", 3, "desc");//评论数量降序
				orders.add(oe);
			}
		}else{
			if(depotName == null || "".equals(depotName)){
				oe = new OrderEntity("categoryOrder.sort", 2, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
				orders.add(oe);
			}else{
				oe = new OrderEntity("depotOrder.sort", 2, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
				orders.add(oe);
			}
			/*if(level == 1){
				oe = new OrderEntity("mutil.productLevel1.sort", 1, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
				orders.add(oe);
			}else if(level == 2){
				oe = new OrderEntity("mutil.productLevel2.sort", 1, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
				orders.add(oe);
			}else{
				oe = new OrderEntity("mutil.productLevel3.sort", 1, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
				orders.add(oe);
			}*/
			oe = new OrderEntity("salesTotalCount", 2, "desc");//根据最近30天内销量降序
			orders.add(oe);
			oe = new OrderEntity("viewcount", 3, "desc");//根据历史浏览量降序
			orders.add(oe);
		}
		oe = new OrderEntity("depots.qty", 5, "desc");
		orders.add(oe);
		oe = new OrderEntity("depots.status", 6, "asc");
		orders.add(oe);
		bean.setOrders(orders);
	}
	
	
	/**
	 * 类目或关键字查询第三版(使用深度分页、查询和聚合分离)
	 * @param keyword (如果keyword等于空,则根据分类path获取类目数据列表)
	 * 
	 * @param website  站点
	 * 
 	 * @param lang 语言Id
	 * 
	 * @param currency 货币
	 * 
	 * @param categoryId 品类Id
	 * 
	 * @param level 类目等级
	 * 
	 * @param page 页数
	 *  
	 * @param size 大小
	 *   
	 * @param sort 排序
	 * 
	 * @param tagName 标签名
	 *  
	 * @param depotName 仓库名
	 *   
	 * @param brand 品牌
	 *    
	 * @param yjprice 价格区间
	 * 
	 * @param type 属性条件
	 * 
	 * @param startPrice 价格条件 区间 开始价 (目前用于m端)
	 * 
	 * @param endPrice 价格条件 区间 结束价  (目前用于m端)
	 * 
	 * @author renyy add by 20160531
	 */
	@Override
	public ProductDepotSearchKeywordAggSort getProductBaseDepotSearchScroll(
			String keyword, Integer website, Integer lang, String currency,
			Integer categoryId, Integer level, Integer page, Integer size,
			String sort, String tagName, String depotName, String brand,
			String yjprice, String type, Double startPrice, Double endPrice) {
		ProductDepotSearchKeywordAggSort pbvo = new ProductDepotSearchKeywordAggSort();
		List<SearchDepotProduct> sdplist = new ArrayList<SearchDepotProduct>();
		PageBean bean = new PageBean();
		//设置关键字
		this.setKeyword(keyword, bean);
		//设置过滤条件
		this.setDepotPageBean(bean, website, categoryId,level, lang, page, size, sort,tagName,depotName,brand,yjprice,type,startPrice,endPrice);
		//查询获取结果集
		PageBean returnBean = searchIndex.queryByBeanV3(bean,categoryId,depotName,"");
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
			Map<String,List<AggregationEntity>> aggsMap = null;
			//根据类目获取所有要聚合属性key名
			List<String> keyList = attributesService.getCategoryAttributeAllKey(categoryId, lang);
			//根据类目获取所有要聚合属性key名对应的所有value
			Map<String,List<String>> mapList = attributesService.getCategoryAttributesMap(categoryId, lang);
			
			boolean isFilter = isFilter(tagName, brand, yjprice,type,depotName);
			//判断是否有过滤条件,如果有则聚合不增加过滤条件
			if(isFilter){
				PageBean beanTwo = new PageBean();
				//设置关键字
				this.setKeyword(keyword, bean);
				this.setDepotPageBeanTwo(beanTwo, website,lang,page,size, categoryId);
				PageBean returnBeanTwo = searchIndex.getAggreagetionsBean(beanTwo,categoryId,keyList,mapList);
				aggsMap = returnBeanTwo.getAggsMap();
				
			}else{
				PageBean returnBeanTwo = searchIndex.getAggreagetionsBean(bean,categoryId,keyList,mapList);
				aggsMap = returnBeanTwo.getAggsMap();
			}
			this.aggsMapSort(aggsMap, keyList, mapList);
			pbvo.setAggsMap(aggsMap);
			pbvo.setPblist(sdplist);
			//等待搜索引擎返回结果集的页数
			Integer count = (int) returnBean.getTotalCount();
			Page pageObj = this.getPageObj(page, count, size);
			pbvo.setPage(pageObj);
		}
		return pbvo;
	}
	
	private void aggsMapSort(Map<String,List<AggregationEntity>> aggsMap,List<String> keyList,Map<String,List<String>> mapList){
		if(aggsMap != null && aggsMap.size() > 0 ){
			//类目聚合重新排序
			List<AggregationEntity> ae = aggsMap.get("mutil.productTypes.productTypeId");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("mutil.productTypes.productTypeId");
				aggsMap.put("mutil.productTypes.productTypeId", ae);
			}
			//仓库
			ae = aggsMap.get("depots.depotName");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("depots.depotName");
				aggsMap.put("depots.depotName", ae);
			}
			
			//Featured Options
			ae = aggsMap.get("tagsName.tagName");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("tagsName.tagName");
				aggsMap.put("tagsName.tagName", ae);
			}
			//价格区间聚合重新排序
			ae = aggsMap.get("yjPrice");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("yjPrice");
				aggsMap.put("yjPrice", ae);
			}
			//品牌聚合重新排序
			/*ae = aggsMap.get("brand");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("brand");
				aggsMap.put("brand", ae);
			}*/
			//属性的聚合排最后
			if(keyList != null && keyList.size() > 0){
				for (String k : keyList) {
					String lowerKey = k.toLowerCase();
					List<AggregationEntity> aelist = aggsMap.get(lowerKey);
					aggsMap.remove(lowerKey);
					List<String> slist = mapList.get(k);
					if(aelist != null && aelist.size() > 0 && slist != null && slist.size() > 0){
						List<AggregationEntity> nl = new ArrayList<AggregationEntity>();
						for (String v : slist) {
							String lowerValue = v.toLowerCase();
							for (AggregationEntity aentity : aelist) {
								if(lowerValue.equals(aentity.getName())){
									aentity.setName(v);
									nl.add(aentity);
								}
							}
						}
						aggsMap.put(k, nl);
					}
					
				}
			}
		}
	}

}
