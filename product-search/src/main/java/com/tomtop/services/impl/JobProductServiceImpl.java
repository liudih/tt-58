package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.entity.Category;
import com.tomtop.entity.DealsCategory;
import com.tomtop.entity.TopSellersProduct;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.OrderEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.services.ICategoryService;
import com.tomtop.services.IJobProductService;
import com.tomtop.services.ISearchIndex;
/**
 * job 更新获取数据方法
 * @author renyy
 *
 */
@Service
public class JobProductServiceImpl implements IJobProductService {

	@Autowired
	ISearchIndex searchIndex;
	@Autowired
	ICategoryService categoryService;
	
	/**
	 * 获取topSellers频道的商品 每个类目获取4个
	 * 
	 * @param website
	 * 
	 */
	@Override
	public List<TopSellersProduct> getTopSellersProduct(Integer website,Integer lang) {
		List<Category> list = categoryService.getCategoryList(1, null, lang, website);
		List<TopSellersProduct> tspList = new ArrayList<TopSellersProduct>();
		for (Category category : list) {
			PageBean bean = new PageBean();
			this.setTopSellersPageBean(bean, website, category.getIcategoryid());
			PageBean returnBean = searchIndex.queryByBeanV2(bean);
			if(returnBean == null){
				return null;
			}
			List<IndexEntity> ieList = returnBean.getIndexs();
			if(ieList != null && ieList.size() > 0){
				for (int i = 0; i < ieList.size(); i++) {
					IndexEntity ie = ieList.get(i);
					TopSellersProduct tsp = new TopSellersProduct();
					tsp.setCategoryId(category.getIcategoryid());
					tsp.setCname(category.getCname());
					tsp.setCpath(category.getCpath());
					tsp.setListingId(ie.getListingId());
					tsp.setSku(ie.getSku());
					tspList.add(tsp);
				}
			}
		}
		
		return tspList;
	}

	/**
	 * 设置topSellers pageBean
	 * @param bean
	 * @param website
	 * @param categoryId
	 */
	private void setTopSellersPageBean(PageBean bean,Integer website,Integer categoryId){
		bean.setLanguageName("en");//设置语言名字
		bean.setBeginNum(0);//开始记录数
		bean.setEndNum(4);//结束记录数
		Filter filter = new Filter("bmain",true,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("webSites",website,"&&");
		bean.getFilters().add(filter);	
		filter = new Filter("depots.status","1","==",false);
		bean.getFilters().add(filter);
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		bean.getFilters().add(filter);
		filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&");
		bean.getFilters().add(filter);
		filter = new Filter("tagsName.tagName","hot","&&");//聚合商品标签 不作为过滤条件
		bean.getFilters().add(filter);
		
		List<OrderEntity> orders = new ArrayList<OrderEntity>();
		OrderEntity oe = null;
		oe = new OrderEntity("salesTotalCount", 1, "desc");//根据最近销量降序
		orders.add(oe);
		oe = new OrderEntity("releaseTime", 2, "asc");//根据上架时间升序
		orders.add(oe);
		bean.setOrders(orders);
	}
	
	/**
	 * Deals频道 获取类目是否有折扣商品信息
	 * 
	 * @param website
	 * @param lang
	 */
	@Override
	public List<DealsCategory> getDealsCategory(Integer website,Integer lang) {
		List<Category> list = categoryService.getCategoryList(1, null, lang, website);
		List<DealsCategory> dcList = new ArrayList<DealsCategory>();
		for (Category category : list) {
			PageBean bean = new PageBean();
			this.setDealsCategoryPageBean(bean, website, category.getIcategoryid());
			PageBean returnBean = searchIndex.queryByBeanV2(bean);
			if(returnBean == null){
				return null;
			}
			List<IndexEntity> ieList = returnBean.getIndexs();
			if(ieList != null && ieList.size() > 0){
				DealsCategory dc = new DealsCategory();
				dc.setCategoryId(category.getIcategoryid());
				dc.setCname(category.getCname());
				dc.setCpath(category.getCpath());
				dc.setIsDiscount(true);
				dcList.add(dc);
			}
		}
		
		return dcList;
	}
	
	/**
	 * 设置deals category pageBean
	 * @param bean
	 * @param website
	 * @param categoryId
	 */
	private void setDealsCategoryPageBean(PageBean bean,Integer website,Integer categoryId){
		bean.setLanguageName("en");//设置语言名字
		bean.setBeginNum(0);//开始记录数
		bean.setEndNum(1);//结束记录数
		Filter filter = new Filter("bmain",true,"&&");//设置过滤条件为主商品
		bean.getFilters().add(filter);
		filter = new Filter("webSites",website,"&&");
		bean.getFilters().add(filter);	
		filter = new Filter("depots.status","1","==",false);
		bean.getFilters().add(filter);
		filter = new Filter("bactivity",false,"==",false);//设置过滤活动商品
		bean.getFilters().add(filter);
		filter = new Filter("tagsName.tagName","onSale","&&",false);//为促销商品
		bean.getFilters().add(filter);
		filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&");
		bean.getFilters().add(filter);
	}
}
