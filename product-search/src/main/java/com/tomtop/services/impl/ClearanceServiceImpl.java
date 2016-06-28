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
import com.tomtop.entity.ProductBaseSearchKeyword;
import com.tomtop.entity.SearchProduct;
import com.tomtop.entity.index.AggregationEntity;
import com.tomtop.entity.index.DepotEntity;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.OrderEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.entity.index.RangeAggregation;
import com.tomtop.entity.index.ReviewStartNum;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.services.IClearanceService;
import com.tomtop.services.IPriceRangPolyService;
import com.tomtop.services.ISearchIndex;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.ProductPublicUtil;

/**
 * 清仓专区搜索
 * @author renyy
 *
 */
@Service
public class ClearanceServiceImpl extends BaseService implements IClearanceService {

	private static final Logger logger = LoggerFactory
			.getLogger(ClearanceServiceImpl.class);
	
	@Autowired
	ISearchIndex searchIndex;
	
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	@Autowired
	IPriceRangPolyService priceRangPolyService;
	
	/**
	 * 清仓商品查询
	 * 
	 */
	@Override
	public ProductBaseSearchKeyword getProductClearance(String keyword,
			Integer website, Integer lang, String currency, Integer categoryId,
			Integer page, Integer size, String sort, String depotName,
			String brand, String yjprice) {
		ProductBaseSearchKeyword pbvo = new ProductBaseSearchKeyword();
		List<SearchProduct> pbpList = new ArrayList<SearchProduct>();
		//设置过滤条件
		PageBean bean = new PageBean();
		if(keyword != null && !"".equals(keyword)){
			bean.setKeyword(keyword);
		}
		this.setPageBean(bean, website, categoryId, lang, page, size, sort,true,depotName,brand,yjprice);
		PageBean returnBean = searchIndex.queryByBeanV3(bean,categoryId,"","clearstocks");
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
				if(ie.getDepots() != null && ie.getDepots().size() > 0){
					boolean isFreeShipping = false;
					for (DepotEntity  de : ie.getDepots()) {
						if(depotName.equals(de.getDepotName())){
							isFreeShipping = de.isFreeShipping();
						}
					}
					vo.setFreeShipping(isFreeShipping);
				}
				
				//通过公共的方法设置父类的属性
				productPublicUtil.transformProductBase(vo, ie, cbo,depotName);
				pbpList.add(vo);
			}
			Map<String,List<AggregationEntity>> aggsMap = null;
			boolean isFilter = isFilter(brand, yjprice,depotName);
			if(isFilter){
				PageBean beanTwo = new PageBean();
				if(keyword != null && !"".equals(keyword)){
					beanTwo.setKeyword(keyword);
				}
				this.setPageBeanTwo(beanTwo, website,lang,page,size, categoryId, true,depotName);
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

	private void setPageBean(PageBean bean, Integer website,
			Integer categoryId, Integer lang, Integer page, Integer size,
			String sort, boolean bmain, String depotName,
			String brand, String yjprice) {
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
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		bean.getFilters().add(filter);
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		filter = new Filter("depots.clearance",depotName,"&&",false,true);
		bean.getFilters().add(filter);
		
				
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
		
		this.setSort(bean, sort,website);//设置排序
		
	}
	
	/**
	 * 设置聚合(只聚合)
	 * 
	 */
	public void setPageBeanTwo(PageBean bean,Integer website,Integer lang,Integer page,Integer size,Integer categoryId,boolean bmain,String depotName){
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
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		bean.getFilters().add(filter);
		
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true);//聚合品类 作为过滤条件
		}else{
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",true,false);//聚合品类,不作为过滤条件
		}
		bean.getFilters().add(filter);
		
		filter = new Filter("depots.clearStocks",true,"&&",false,true);
		bean.getFilters().add(filter);
		
		filter = new Filter("depots.clearance",depotName,"&&",true,false);
		bean.getFilters().add(filter);
		
		//处理brand
		filter = new Filter("brand","","&&",true,false);//聚合品牌标签 不作为过滤条件
		bean.getFilters().add(filter);
		
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
			
	}

	/**
	 * 判断是否有过滤条件
	 * @param brand
	 * @param yjprice
	 * @param depotName
	 * @return
	 */
	private boolean isFilter(String brand,String yjprice,String depotName){
		if(brand != null && !"".equals(brand)){
			return true;
		}
		if(yjprice != null && !"".equals(yjprice)){
			return true;
		}
		if(depotName != null && !"".equals(depotName)){
			return true;
		}
		return false;
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
			//后续默认增加标签人工置顶
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

	
	private void aggsMapSort(Map<String,List<AggregationEntity>> aggsMap){
		if(aggsMap != null && aggsMap.size() > 0){
			//仓库
			List<AggregationEntity> ae = aggsMap.get("depots.clearance");
			if(ae != null && ae.size() > 0){
				for (int i = 0; i < ae.size(); i++) {
					if("NO".equals(ae.get(i).getName())){
						ae.remove(i);
					}
				}
				aggsMap.remove("depots.clearance");
				aggsMap.put("depots.depotName", ae);
			}
			//类目聚合重新排序
			ae = aggsMap.get("mutil.productTypes.productTypeId");
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
			//品牌聚合重新排序
			ae = aggsMap.get("yjPrice");
			if(ae != null && ae.size() > 0){
				aggsMap.remove("yjPrice");
				aggsMap.put("yjPrice", ae);
			}
		}
	}
}
