package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.Currency;
import com.tomtop.entity.NewArrivalsAgg;
import com.tomtop.entity.PriceRangePoly;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.ProductBaseSearchKeyword;
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
import com.tomtop.mappers.product.ProductNewMapper;
import com.tomtop.services.INewArrivalsService;
import com.tomtop.services.IPriceRangPolyService;
import com.tomtop.services.ISearchIndex;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.DateUtil;
import com.tomtop.utils.FormatDateUtils;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

/**
 * 搜索引擎查询新品业务类
 * 
 * @author renyy
 *
 */
@Service
public class NewArrivalsServiceImpl extends BaseService implements INewArrivalsService {

	private static final Logger logger = LoggerFactory
			.getLogger(EsProductSearchKeywordServiceImpl.class);
	@Autowired
	ISearchIndex searchIndex;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	IPriceRangPolyService priceRangPolyService;
	@Autowired
	ProductPublicUtil productPublicUtil;
	@Autowired
	ProductNewMapper newMapper;
	
	/**
	 * New Arrivals 商品
	 */
	@Override
	public ProductBaseSearchKeyword getProductNewArrivals(String keyword,
			Integer website, Integer lang, String currency, Integer categoryId,
			Integer page, Integer size, String sort, String tagName,
			String depotName, String brand, String yjprice, String type,String releaseTime) {
		ProductBaseSearchKeyword pbvo = new ProductBaseSearchKeyword();
		List<SearchProduct> pbpList = new ArrayList<SearchProduct>();
		//设置过滤条件
		PageBean bean = new PageBean();
		if(keyword != null && !"".equals(keyword)){
			bean.setKeyword(keyword);
		}
		String utcTime = FormatDateUtils.getCurrentUtcTime();
		String leekTime = DateUtil.addMonth(utcTime, -1);//设置时间获取最近一个月上架的
		this.setPageBean(bean, website, categoryId, lang, page, size, sort,true,leekTime,tagName,depotName,brand,yjprice,type,releaseTime);
		PageBean returnBean = searchIndex.queryByBeanV3(bean,categoryId,"","new");
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
			boolean isFilter = isFilter(tagName, brand, yjprice,type,releaseTime);
			if(isFilter){
				PageBean beanTwo = new PageBean();
				if(keyword != null && !"".equals(keyword)){
					beanTwo.setKeyword(keyword);//2
				}
				this.setPageBeanTwo(beanTwo, website,lang,page,size, categoryId, true,leekTime,releaseTime);
				//获取原条件聚合
				PageBean returnBeanTwo = searchIndex.getAggreagetionsBean(beanTwo,categoryId,null,null);
				aggsMap = returnBeanTwo.getAggsMap();
			}else{
				//获取聚合
				PageBean returnBeanTwo = searchIndex.getAggreagetionsBean(bean,categoryId,null,null);
				aggsMap = returnBeanTwo.getAggsMap();
			}
			this.aggsMapSort(aggsMap);
			pbvo.setAggsMap(aggsMap);
			pbvo.setPblist(pbpList);
			//等待搜索引擎返回结果集的页数
			Integer currentPage = page;
			Integer count = (int) returnBean.getTotalCount();
			Integer pageSize = size;
			Page pageObj = Page.getPage(currentPage, count, pageSize);
			pbvo.setPage(pageObj);
		}
		return pbvo;
	}

	/**
	 * 设置过滤条件和聚合
	 * 
	 */
	public void setPageBean(PageBean bean,Integer website,Integer categoryId,
			Integer lang,Integer page,Integer size,String sort,boolean bmain,String leekTime,
			String tagName,String depotName,String brand,String yjprice,String type,String releaseTime){
		bean.setLanguageName(getLangCode(lang));//设置语言名字
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",true,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("webSites",website,"&&");
		bean.getFilters().add(filter);	
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		bean.getFilters().add(filter);
		filter = new Filter("depots.status","1,3,40","&&",false);//设置status过滤条件不聚合
		bean.getFilters().add(filter);
		
		if("".equals(releaseTime)){
			filter = new Filter("releaseTime",leekTime,">=",false);//默认设置上架时间为最近一个月
			bean.getFilters().add(filter);
		}else{
			boolean b = false;
			//如果为大于等于时间的条件时
			if(releaseTime.indexOf("MOT") != -1){
				releaseTime = releaseTime.replace("MOT", "").trim();
				if(DateUtil.isDate(releaseTime) && releaseTime.length() == 10){
					releaseTime+=" 00:00:00";
					filter = new Filter("releaseTime",releaseTime,">=",false);
					bean.getFilters().add(filter);
					b = true;
				}
			//如果为等于时间的条件时
			}else if(releaseTime.indexOf("EQU") != -1){
				releaseTime = releaseTime.replace("EQU", "").trim();
				if(DateUtil.isDate(releaseTime) && releaseTime.length() == 10){
					String stime=releaseTime + " 00:00:00";
					String etime=releaseTime + " 23:59:59";
					filter = new Filter("releaseTime",stime,">=",false);
					bean.getFilters().add(filter);
					filter = new Filter("releaseTime",etime,"<=",false);
					bean.getFilters().add(filter);
					b = true;
				}
			}
			if(b == false){
				filter = new Filter("releaseTime",leekTime,">=",false);//默认设置上架时间为最近一个月
				bean.getFilters().add(filter);
			}
		}
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		//只过滤 不聚合
		filter = new Filter("tagsName.tagName","new","&&",false,true);//只过滤 不聚合
		bean.getFilters().add(filter);
		/*bean.getFilters().add(filter);
		if(tagName != null && !"".equals(tagName)){
			filter = new Filter("tagsName.tagName",tagName,"&&",false);//只作为过滤
			bean.getFilters().add(filter);
		}*/
		
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
		/*	
		if(type == null || "".equals(type)){
			filter = new Filter("mutil.items.value","","&&",true,false);//商品属性聚合 不作为过滤
		}else{
			filter = new Filter("mutil.items.value",type,"&&",false);//只作为过滤
		}
		//聚合属性标签 不作为过滤条件
		bean.getFilters().add(filter);*/
		
		this.setSort(bean, sort,website);//设置排序
	}
	/**
	 * 设置聚合(只聚合)
	 * 
	 */
	public void setPageBeanTwo(PageBean beanTwo,Integer website,Integer lang,Integer page,Integer size,Integer categoryId,boolean bmain,String leekTime,String releaseTime){
		beanTwo.setLanguageName(getLangCode(lang));//设置语言名字
		int endNum = page * size;
		int beginNum = endNum - size;
		beanTwo.setBeginNum(beginNum);//开始记录数
		beanTwo.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",true,"&&");//设置过滤条件为主商品
		beanTwo.getFilters().add(filter);
		filter = new Filter("webSites",website,"&&");
		beanTwo.getFilters().add(filter);	
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		beanTwo.getFilters().add(filter);
		filter = new Filter("depots.status","1,3,40","&&",false);//设置status过滤条件不聚合
		beanTwo.getFilters().add(filter);
		if("".equals(releaseTime)){
			filter = new Filter("releaseTime",leekTime,">=",false);//默认设置上架时间为最近一个月
			beanTwo.getFilters().add(filter);
		}else{
			boolean b = false;
			//如果为大于等于时间的条件时
			if(releaseTime.indexOf("MOT") != -1){
				releaseTime = releaseTime.replace("MOT", "").trim();
				if(DateUtil.isDate(releaseTime) && releaseTime.length() == 10){
					releaseTime+=" 00:00:00";
					filter = new Filter("releaseTime",releaseTime,">=",false);
					beanTwo.getFilters().add(filter);
					b = true;
				}
			//如果为等于时间的条件时
			}else if(releaseTime.indexOf("EQU") != -1){
				releaseTime = releaseTime.replace("EQU", "").trim();
				if(DateUtil.isDate(releaseTime) && releaseTime.length() == 10){
					String stime=releaseTime + " 00:00:00";
					String etime=releaseTime + " 23:59:59";
					filter = new Filter("releaseTime",stime,">=",false);
					beanTwo.getFilters().add(filter);
					filter = new Filter("releaseTime",etime,"<=",false);
					beanTwo.getFilters().add(filter);
					b = true;
				}
			}
			if(b == false){
				filter = new Filter("releaseTime",leekTime,">=",false);//默认设置上架时间为最近一个月
				beanTwo.getFilters().add(filter);
			}
		}
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		beanTwo.getFilters().add(filter);
		
		//处理tagName
		filter = new Filter("tagsName.tagName","new","&&",false,true);//只过滤 不聚合
		beanTwo.getFilters().add(filter);
		
		//处理brand
		filter = new Filter("brand","","&&",true,false);//聚合品牌标签 不作为过滤条件
		beanTwo.getFilters().add(filter);
		
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
				beanTwo.getRangeAgg().add(range);
			}
		}
			
		/*filter = new Filter("mutil.items.value","","&&",true,false);//聚合属性标签 不作为过滤条件
		bean.getFilters().add(filter);*/
	}
	/**
	 * 设置排序
	 * 
	 */
	public void setSort(PageBean bean,String sort,Integer website){
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		OrderEntity oe = null;
		if(sort != null && !"".equals(sort)){
			if("pirceAsc".equals(sort)){
				//价格从小到大升序 Price: High to Low - 价格从低到高
				oe = new OrderEntity("yjPrice", 2, "asc");
				orders.add(oe);
			}
			if("pirceDesc".equals(sort)){
				//Price: Low to High - 价格从高到低
				oe = new OrderEntity("yjPrice", 2, "desc");
				orders.add(oe);
			}
			if("reviewCount".equals(sort)){			
				//按评论最多的排序 Most Reviews
				oe = new OrderEntity("review.count", 2, "desc");
				orders.add(oe);
				oe = new OrderEntity("review.start", 3, "desc");
				orders.add(oe);
			}
			if("releaseTime".equals(sort)){
				//按发布时间的排序  Newest - 新上架商品
				oe = new OrderEntity("releaseTime", 2, "desc");
				orders.add(oe);
			}
			if("salesVolume".equals(sort)){
				//按销量的排序 Most Popular
				oe = new OrderEntity("salesTotalCount", 2, "desc");//销量降序
				orders.add(oe);
				oe = new OrderEntity("viewcount", 3, "desc");//浏览量降序
				orders.add(oe);
				oe = new OrderEntity("review.count", 4, "desc");//评论数量降序
				orders.add(oe);
			}
		}else{
			oe = new OrderEntity("tagOrder.sort", 2, "asc");//根据后台人工推荐置顶功能设置的显示顺序升序
			orders.add(oe);
			oe = new OrderEntity("salesTotalCount", 3, "desc");//根据最近30天内销量降序
			orders.add(oe);
			oe = new OrderEntity("viewcount", 4, "desc");//根据历史浏览量降序
			orders.add(oe);
		}
		if(website == CommonDefn.ONE){
			oe = new OrderEntity("depots.qty", 5, "desc");
			orders.add(oe);
			oe = new OrderEntity("depots.status", 6, "asc");
			orders.add(oe);
		}else{
			oe = new OrderEntity("storeNum", 5, "desc");
			orders.add(oe);
			oe = new OrderEntity("status", 6, "asc");
			orders.add(oe);
		}
		
		bean.setOrders(orders);
	}
	/**
	 * 判断是否有过滤条件
	 * @param tagName
	 * @param brand
	 * @param yjprice
	 * @param type
	 * @return
	 */
	private boolean isFilter(String tagName,String brand,String yjprice,String type,String releaseTime){
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
		if(releaseTime != null && !"".equals(releaseTime)){
			return true;
		}
		return false;
	}

	/**
	 * 新品日期聚合
	 */
	@Override
	public List<NewArrivalsAgg> getNewAgg(Integer website) {
		String utcTime = FormatDateUtils.getCurrentUtcTimeYYYYMMDD();
		utcTime += " 00:00:00";
		String leekTime = DateUtil.addDay(utcTime, -11);//获取10日内每日聚合
		Date startDate = FormatDateUtils.parseDate(leekTime, FormatDateUtils.YYYYMMDDHHMMSS);
		List<NewArrivalsAgg> naaList = newMapper.getNewArrivalsAgg(website,startDate);
		
		//获取最近7天聚合
		String oneLeekTime = DateUtil.addDay(utcTime, -7);
		Date oneLeek= FormatDateUtils.parseDate(oneLeekTime, FormatDateUtils.YYYYMMDDHHMMSS);
		oneLeekTime = "MOT" + oneLeekTime;
		NewArrivalsAgg naa1 = newMapper.getNewArrivalsAggWeek(website, oneLeek,oneLeekTime);
		if(naa1 != null){
			naa1.setDateName("Last week");
			naaList.add(naa1);
		}
		
		//获取最近14天聚合
		String twoLeekTime = DateUtil.addDay(utcTime, -14);
		Date twoLeek= FormatDateUtils.parseDate(twoLeekTime, FormatDateUtils.YYYYMMDDHHMMSS);
		twoLeekTime = "MOT" + twoLeekTime;
		NewArrivalsAgg naa2 = newMapper.getNewArrivalsAggWeek(website, twoLeek,twoLeekTime);
		if(naa2 != null){
			naa2.setDateName("Last 2 weeks");
			naaList.add(naa2);
		}
		//获取最近21天聚合
		String threeLeekTime = DateUtil.addDay(utcTime, -21);
		Date threeLeek= FormatDateUtils.parseDate(threeLeekTime, FormatDateUtils.YYYYMMDDHHMMSS);
		threeLeekTime = "MOT" + threeLeekTime;
		NewArrivalsAgg naa3 = newMapper.getNewArrivalsAggWeek(website, threeLeek,threeLeekTime);
		if(naa3 != null){
			naa3.setDateName("Last 3 weeks");
			naaList.add(naa3);
		}
		//获取最近一个月聚合Last month
		String lastMonthTime = DateUtil.addDay(utcTime, -30);
		Date lastMonth = FormatDateUtils.parseDate(lastMonthTime, FormatDateUtils.YYYYMMDDHHMMSS);
		lastMonthTime = "MOT" + lastMonthTime;
		NewArrivalsAgg naaLast = newMapper.getNewArrivalsAggWeek(website, lastMonth,lastMonthTime);
		if(naaLast != null){
			naaLast.setDateName("Last month");
			naaList.add(naaLast);
		}
		
		return naaList;
	}
	
	/**
	 * New Arrivals 商品 。v2
	 */
	@Override
	public ProductBaseDepotSearchKeyword getProductNewArrivalsDepot(String keyword,
			Integer website, Integer lang, String currency, Integer categoryId,
			Integer page, Integer size, String sort, String tagName,
			String depotName, String brand, String yjprice, String type,String releaseTime) {
		ProductBaseDepotSearchKeyword pbvo = new ProductBaseDepotSearchKeyword();
		List<SearchDepotProduct> sdpList = new ArrayList<SearchDepotProduct>();
		//设置过滤条件
		PageBean bean = new PageBean();
		if(keyword != null && !"".equals(keyword)){
			bean.setKeyword(keyword);
		}
		String utcTime = FormatDateUtils.getCurrentUtcTime();
		String leekTime = DateUtil.addMonth(utcTime, -1);//设置时间获取最近一个月上架的
		this.setPageBean(bean, website, categoryId, lang, page, size, sort,true,leekTime,tagName,depotName,brand,yjprice,type,releaseTime);
		PageBean returnBean = searchIndex.queryByBeanV3(bean,categoryId,"","new");
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
				sdpList.add(vo);
			}
			Map<String,List<AggregationEntity>> aggsMap = null;
			boolean isFilter = isFilter(tagName, brand, yjprice,type,releaseTime);
			if(isFilter){
				PageBean beanTwo = new PageBean();
				if(keyword != null && !"".equals(keyword)){
					beanTwo.setKeyword(keyword);//2
				}
				this.setPageBeanTwo(beanTwo, website,lang,page,size, categoryId, true,leekTime,releaseTime);
				PageBean returnBeanTwo = searchIndex.getAggreagetionsBean(beanTwo,categoryId,null,null);
				aggsMap = returnBeanTwo.getAggsMap();
			}else{
				//获取聚合
				PageBean returnBeanTwo = searchIndex.getAggreagetionsBean(bean,categoryId,null,null);
				aggsMap = returnBeanTwo.getAggsMap();
			}
			this.aggsMapSort(aggsMap);
			pbvo.setAggsMap(aggsMap);
			pbvo.setPblist(sdpList);
			//等待搜索引擎返回结果集的页数
			Integer currentPage = page;
			Integer count = (int) returnBean.getTotalCount();
			Integer pageSize = size;
			Page pageObj = Page.getPage(currentPage, count, pageSize);
			pbvo.setPage(pageObj);
		}
		return pbvo;
	}
	
	private void aggsMapSort(Map<String,List<AggregationEntity>> aggsMap){
		if(aggsMap != null && aggsMap.size() > 0){
			//类目聚合重新排序
			List<AggregationEntity> ae = aggsMap.get("mutil.productTypes.productTypeId");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("mutil.productTypes.productTypeId");
				aggsMap.put("mutil.productTypes.productTypeId", ae);
			}
			//品牌聚合重新排序
			ae = aggsMap.get("brand");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("brand");
				aggsMap.put("brand", ae);
			}
			//价格区间聚合重新排序
			ae = aggsMap.get("yjPrice");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("yjPrice");
				aggsMap.put("yjPrice", ae);
			}
		}
	}
	
}
