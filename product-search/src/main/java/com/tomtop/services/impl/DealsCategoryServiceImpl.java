package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.Currency;
import com.tomtop.entity.DealsCategory;
import com.tomtop.entity.ProductBaseDepotSearchKeyword;
import com.tomtop.entity.SearchDepotProduct;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.OrderEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.entity.index.ReviewStartNum;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.mappers.mysql.DealsCategoryDiscountMapper;
import com.tomtop.services.IDealsCategoryService;
import com.tomtop.services.ISearchIndex;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.ProductPublicUtil;

/**
 * Deals 频道专区服务
 * @author Administrator
 *
 */
@Service
public class DealsCategoryServiceImpl extends BaseService implements IDealsCategoryService {
	
	@Autowired
	ISearchService searchService;
	@Autowired
	DealsCategoryDiscountMapper dealsCategoryDiscountMapper;
	@Autowired
	ProductPublicUtil productPublicUtil;
	@Autowired
	ISearchIndex searchIndex;
	
	/**
	 * 获取有促销商品的类目
	 */
	@Override
	public List<DealsCategory> getDealsCategoryList(Integer client) {
		return dealsCategoryDiscountMapper.getDealsCategory(client);
	}
	
	/**
	 * 获取Deals频道专区商品
	 * 
	 * @param keyword
	 * @param website
	 * @param lang
	 * @param currency
	 * @param categoryId
	 * @param page
	 * @param size
	 * @param sort
	 * @param depotName (保留参数)
	 * @return
	 */
	@Override
	public ProductBaseDepotSearchKeyword getDealsProducts(String keyword,
			Integer website, Integer lang, String currency, Integer categoryId,
			Integer page, Integer size, String sort, String depotName) {
		ProductBaseDepotSearchKeyword pbvo = new ProductBaseDepotSearchKeyword();
		List<SearchDepotProduct> sdplist = new ArrayList<SearchDepotProduct>();
		PageBean bean = new PageBean();
		//设置过滤条件
		this.setDepotPageBean(bean, website, categoryId, lang, page, size,sort);
		PageBean returnBean = searchIndex.queryByBeanV3(bean,categoryId,"","");
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
			Integer lang,Integer page,Integer size,String sort){
		bean.setLanguageName(getLangCode(lang));//设置语言名字
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		Filter filter = new Filter("bmain",true,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("webSites",website,"&&");
		bean.getFilters().add(filter);	
		filter = new Filter("depots.status",1,"==",false);
		bean.getFilters().add(filter);
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		bean.getFilters().add(filter);
		
		filter = new Filter("tagsName.tagName","onSale","&&");//为促销商品
		bean.getFilters().add(filter);
		if(categoryId > 0){
			filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&");//聚合品类,不作为过滤条件
			bean.getFilters().add(filter);
		}
		
		this.setDepotSort(bean,sort);//设置排序
	}
	
	/**
	 * 设置排序
	 * 
	 */
	public void setDepotSort(PageBean bean,String sort){
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		OrderEntity oe = null;
		if(sort != null && !"".equals(sort)){
			if("newest".equals(sort)){
				//根据创建折扣价格的时间降序
				oe = new OrderEntity("depots.salePrice.createStamp", 1, "desc");
				orders.add(oe);
			}
			if("hottest".equals(sort)){
				//根据销量降序，上架时间升序
				oe = new OrderEntity("salesTotalCount", 1, "desc");//根据最近销量降序
				orders.add(oe);
				oe = new OrderEntity("releaseTime", 2, "asc");//根据上架时间升序
				orders.add(oe);
			}
			if("price".equals(sort)){			
				//价格从低到高
				oe = new OrderEntity("yjPrice", 1, "asc");
				orders.add(oe);
			}
			if("discount".equals(sort)){			
				//折扣从高到低
				oe = new OrderEntity("depots.salePrice.discount", 1, "asc");
				orders.add(oe);
			}
		}
		
		bean.setOrders(orders);
	}
	
}
