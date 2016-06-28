package com.tomtop.services.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.CallProductLableSaleSku;
import com.tomtop.entity.Currency;
import com.tomtop.entity.OrderProduct;
import com.tomtop.entity.ProductCompute;
import com.tomtop.entity.ReportProductData;
import com.tomtop.entity.ReportProductDataBo;
import com.tomtop.entity.index.DepotEntity;
import com.tomtop.entity.index.Filter;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.PageBean;
import com.tomtop.entity.index.ProductTypeEntity;
import com.tomtop.entity.index.TagEntity;
import com.tomtop.framework.core.utils.Page;
import com.tomtop.services.ICallProductService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.CommonDefn;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

@Service
public class CallProductServiceImpl extends BaseService implements ICallProductService {

	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	/**
	 * 获取 sku、商品标签、是否为促销商品
	 * @param listingId 
	 * @param lang 语言Id
	 * @param client 客户端Id
	 * @author renyy
	 *
	 */
	@Override
	public  Map<String,CallProductLableSaleSku> getCallProductLableSaleSkuBo(List<String> listingIds, Integer lang, Integer website) {
		Map<String,CallProductLableSaleSku> callMap = new HashMap<String,CallProductLableSaleSku>();
		List<IndexEntity> ieList =searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				if(ie != null){
					CallProductLableSaleSku cpbo = new CallProductLableSaleSku();
					cpbo.setSku(ie.getSku());
					boolean b = productComputeUtil.getIsSales(ie.getPromotionPrice());
					List<String> lables = new ArrayList<String>();
					if(b){
						lables.add("OffPrice");
					}
					if(ie.getTagsName() != null && ie.getTagsName().size() > 0){
						for (TagEntity te : ie.getTagsName()) {
							if(te.getTagName() != null && !"".equals(te.getTagName())){
								lables.add(te.getTagName());
							}
						}
					}
					cpbo.setLables(lables);
					List<Integer> categoryIds = new ArrayList<Integer>();
					if(ie.getMutil() != null && ie.getMutil().getProductTypes() != null && ie.getMutil().getProductTypes().size() > 0){
						for (int j = 0; j < ie.getMutil().getProductTypes().size(); j++) {
							ProductTypeEntity pt = ie.getMutil().getProductTypes().get(j);
							categoryIds.add(pt.getProductTypeId());
						}
					}
					cpbo.setCategoryIds(categoryIds);
					callMap.put(ie.getListingId(), cpbo);
				}
				
			}
		}
		
		return callMap;
	}

	/**
	 * 获取 sku、商品标签、是否为促销商品 .V2
	 * @param listingId 
	 * @param lang 语言Id
	 * @param client 客户端Id
	 * @param depotId 仓库Id
	 * @author renyy
	 *
	 */
	@Override
	public  Map<String,CallProductLableSaleSku> getCallProductLableSaleSkuBoV2(List<String> listingIds, Integer lang, Integer website,Integer depotId) {
		Map<String,CallProductLableSaleSku> callMap = new HashMap<String,CallProductLableSaleSku>();
		List<IndexEntity> ieList =searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				if(ie != null){
					CallProductLableSaleSku cpbo = new CallProductLableSaleSku();
					cpbo.setSku(ie.getSku());
					List<String> lables = new ArrayList<String>();
					//判断商品对应的仓库是否有折扣价
					if(ie.getDepots() != null && ie.getDepots().size() > 0){
						for (DepotEntity de : ie.getDepots()) {
							if(de != null && de.getSalePrice() != null && de.getSalePrice().size() > 0 &&  de.getDepotid() == depotId){
								boolean b = productComputeUtil.getIsDepotSales(de.getSalePrice());
								if(b){
									lables.add("OffPrice");
								}
							}
						}
					}
					//商品标签
					if(ie.getTagsName() != null && ie.getTagsName().size() > 0){
						for (TagEntity te : ie.getTagsName()) {
							if(te.getTagName() != null && !"".equals(te.getTagName())){
								lables.add(te.getTagName());
							}
						}
					}
					cpbo.setLables(lables);
					List<Integer> categoryIds = new ArrayList<Integer>();
					//商品对应的品类Ids
					if(ie.getMutil() != null && ie.getMutil().getProductTypes() != null && ie.getMutil().getProductTypes().size() > 0){
						for (int j = 0; j < ie.getMutil().getProductTypes().size(); j++) {
							ProductTypeEntity pt = ie.getMutil().getProductTypes().get(j);
							categoryIds.add(pt.getProductTypeId());
						}
					}
					cpbo.setCategoryIds(categoryIds);
					callMap.put(ie.getListingId(), cpbo);
				}
				
			}
		}
		
		return callMap;
	}
	/**
	 * 后台导出数据方法
	 * 
	 * 
	 */
	@Override
	public ReportProductData getReportProductDataVo(Integer categoryId,
			Integer lang, Integer siteId, String currency,Boolean bmain, Integer page,
			Integer size, String status,Integer storage) {
		ReportProductData pbvo = new ReportProductData();
		List<ReportProductDataBo> pbpList = new ArrayList<ReportProductDataBo>();
		//设置过滤条件
		PageBean bean = new PageBean();
		this.setPageBean(bean, categoryId, lang, siteId, currency,bmain, page, size, status);
		PageBean returnBean = searchService.getSearchPageBean(bean, lang);
		if(returnBean == null){
			return null;
		}
		List<IndexEntity> ieList = returnBean.getIndexs();
		Currency cbo = this.getCurrencyBean(currency);
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				ReportProductDataBo rpd = new ReportProductDataBo();
				IndexEntity ie = ieList.get(i);
				rpd.setListingId(ie.getListingId());
				rpd.setSku(ie.getSku());
				rpd.setStatus(ie.getStatus());
				if(ie.getMutil() != null){
					rpd.setDesc(ie.getMutil().getDesc());
					if(ie.getMutil().getProductTypes() != null  && ie.getMutil().getProductTypes().size() > 0){
						List<Integer> categoryIds = new ArrayList<Integer>();
						for (int j = 0; j < ie.getMutil().getProductTypes().size(); j++) {
							categoryIds.add(ie.getMutil().getProductTypes().
									get(j).getProductTypeId());
						}
						rpd.setCategoryIds(categoryIds);
					}
					rpd.setShortDesc(ie.getMutil().getShortDescription());
					rpd.setBrand(ie.getBrand());
					rpd.setSearchTerms(ie.getMutil().getKeywords());
					rpd.setTitle(ie.getMutil().getTitle());
					if(ie.getMutil().getUrl() != null && ie.getMutil().getUrl().size() > 0){
						rpd.setProductUrl(ie.getMutil().getUrl().get(0));
					}
				}
				rpd.setCurrencyCode(cbo.getCode());
				List<DepotEntity> dlist = ie.getDepots();
				if(dlist != null && dlist.size() > 0){
					String price = "";
					String originalPrice = "";
					Integer depotId = 0;
					for (DepotEntity depotEntity : dlist) {
						if(storage == 0){
							ProductCompute pprbo = productComputeUtil.getDepotPrice(ie.getCostPrice(),depotEntity.getPrice(), depotEntity.getSalePrice(), cbo);
							if("".equals(price)){
								price = pprbo.getPrice();
								originalPrice = pprbo.getOriginalPrice();
								depotId = depotEntity.getDepotid();
							}else if(Double.parseDouble(price) > Double.parseDouble(pprbo.getPrice())){
								price = pprbo.getPrice();
								originalPrice = pprbo.getOriginalPrice();
								depotId = depotEntity.getDepotid();
							}
						}else if(storage == depotEntity.getDepotid()){
								ProductCompute pprbo = productComputeUtil.getDepotPrice(ie.getCostPrice(),depotEntity.getPrice(), depotEntity.getSalePrice(), cbo);
								price = pprbo.getPrice();
								originalPrice = pprbo.getOriginalPrice();
								depotId = storage;
						}
					}
					if("".equals(price) && "".equals(originalPrice)){
						continue;
					}
					rpd.setPrice(price);
					rpd.setOldprice(originalPrice);
					rpd.setStorageId(depotId);
				}
				rpd.setImgs(ie.getImgs());
				boolean isHot = false;
				if(ie.getTagsName() != null && ie.getTagsName().size() > 0){
					for (int j = 0; j < ie.getTagsName().size(); j++) {
						if("hot".equals(ie.getTagsName().get(j).getTagName().trim())){
							isHot = true;
							break;
						}
					}
				}
					rpd.setTopseller(isHot);
					pbpList.add(rpd);
			}
			
			Integer currentPage = page;
			Integer count = (int) returnBean.getTotalCount();
			Integer pageSize = size;
			Page pageObj = Page.getPage(currentPage, count, pageSize);
			pbvo.setPage(pageObj);
			pbvo.setRpdbos(pbpList);
		}
		
		return pbvo;
	}
	/**
	 * 设置过滤条件和聚合
	 * 
	 */
	public void setPageBean(PageBean bean,Integer categoryId,
			Integer lang, Integer siteId, String currency,Boolean bmain, Integer page,
			Integer size, String status){
		bean.setWebSites(siteId.toString());//站点
		int endNum = page * size;
		int beginNum = endNum - size;
		bean.setBeginNum(beginNum);//开始记录数
		bean.setEndNum(size);//结束记录数
		if(bmain){
			bean.getFilters().add(new Filter("bmain",true,"&&"));//设置过滤条件为主商品
		}
		if(siteId == CommonDefn.ONE){
			bean.getFilters().add(new Filter("depots.status",status,"==",false));//设置status
		}else{
			bean.getFilters().add(new Filter("status",status,"==",false));//设置status
		}
		
		
		Filter filter = new Filter("mutil.productTypes.productTypeId",categoryId,"&&",false);//聚合品类 作为过滤条件
		bean.getFilters().add(filter);
	}
	
	/**
	 * 获取订单商品详情
	 * 
	 */
	@Override
	public List<OrderProduct> getOrderProducts(List<String> listingIds,
			Integer lang, Integer website,Integer storage) {
		List<OrderProduct> opList = new ArrayList<OrderProduct>();
		List<IndexEntity> ieList =searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				IndexEntity ie = ieList.get(i);
				if(ie != null){
					OrderProduct op = new OrderProduct();
					productPublicUtil.transformOrderProduct(op, ie, storage);
					opList.add(op);
				}
			}
		}
		return opList;
	}
}
