package com.tomtop.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tomtop.entity.Currency;
import com.tomtop.entity.ListingIdNum;
import com.tomtop.entity.ProductBase;
import com.tomtop.entity.ShoppingCartProduct;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.services.IProductBaseService;
import com.tomtop.services.ISearchService;
import com.tomtop.utils.ProductComputeUtil;
import com.tomtop.utils.ProductPublicUtil;

@Service
public class ProductBaseServiceImpl extends BaseService implements IProductBaseService {
	
	//private static final Logger logger = LoggerFactory.getLogger(ProductBaseServiceImpl.class);
	
	@Autowired
	ISearchService searchService;
	@Autowired
	ProductComputeUtil productComputeUtil;
	@Autowired
	ProductPublicUtil productPublicUtil;
	
	@Override
	public List<ProductBase> getProductBaseByListIds(List<String> listingIds,
			int lang, int website,String currency) {
		List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
		Currency cbo = this.getCurrencyBean(currency);
		List<ProductBase> pbList = new ArrayList<ProductBase>();
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				ProductBase pb = new ProductBase();
				IndexEntity ie = ieList.get(i);
				if(ie != null){
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(pb, ie, cbo);
					pbList.add(pb);
				}
			}
		}
		return pbList;
	}

	@Override
	public ProductBase getProductBaseByListId(String listingId, int lang,
			int website, String currency) {
		if(listingId != null && !"".endsWith(listingId.trim())){
			List<String> list = new ArrayList<String>();
			list.add(listingId);
			List<ProductBase> pbList = this.getProductBaseByListIds(list, lang, website, currency);
			if(pbList != null && pbList.size() > 0){
				return pbList.get(0);
			}
		}
		return null;
	}

	@Override
	public List<ShoppingCartProduct> getProductShopping(String listingIds,
			int lang, int website, String currency) {
		List<ListingIdNum> list = JSON.parseArray(listingIds,ListingIdNum.class);
		List<String> lists = new ArrayList<String>();
		Map<String,Integer> lmap = new HashMap<String,Integer>();
		for (ListingIdNum ln : list) {
			lists.add(ln.getListingId());
			lmap.put(ln.getListingId(), ln.getNum());
		}
		List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(lists), lang, website);
		Currency cbo = this.getCurrencyBean(currency);
		List<ShoppingCartProduct> scpList = new ArrayList<ShoppingCartProduct>();
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				ShoppingCartProduct pb = new ShoppingCartProduct();
				IndexEntity ie = ieList.get(i);
				if(ie != null){
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(pb, ie, cbo);
					pb.setNum(lmap.get(ie.getListingId()));
					
					scpList.add(pb);
				}
			}
		}
		return scpList;
	}
	
	@Override
	public List<ProductBase> getProductBaseByListIds(List<String> listingIds,
			int lang, int website,String currency,String depotName) {
		List<IndexEntity> ieList = searchService.getSearchProductList(JSON.toJSONString(listingIds), lang, website);
		Currency cbo = this.getCurrencyBean(currency);
		List<ProductBase> pbList = new ArrayList<ProductBase>();
		if(ieList != null && ieList.size() > 0){
			for (int i = 0; i < ieList.size(); i++) {
				ProductBase pb = new ProductBase();
				IndexEntity ie = ieList.get(i);
				if(ie != null){
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(pb, ie, cbo,depotName);
					pbList.add(pb);
				}
			}
		}
		return pbList;
	}

	@Override
	public ProductBase getProductBaseByListId(String listingId, int lang,
			int website, String currency,String depotName) {
		if(listingId != null && !"".endsWith(listingId.trim())){
			List<String> list = new ArrayList<String>();
			list.add(listingId);
			List<ProductBase> pbList = this.getProductBaseByListIds(list, lang, website, currency,depotName);
			if(pbList != null && pbList.size() > 0){
				return pbList.get(0);
			}
		}
		return null;
	}
	/**
	 * 获取购物车的商品信息
	 */
	@Override
	public Map<Integer,List<ShoppingCartProduct>> getProductShopping(String listingIds,
			int lang, int website, String currency,String depotName) {
		List<ListingIdNum> list = JSON.parseArray(listingIds,ListingIdNum.class);
		List<String> lists = new ArrayList<String>();
		Map<String,Integer> lmap = new HashMap<String,Integer>();
		Map<String,Integer> depotMap = new HashMap<String,Integer>();
		for (ListingIdNum ln : list) {
			lists.add(ln.getListingId());
			lmap.put(ln.getListingId()+ln.getDepotId(), ln.getNum());
			depotMap.put(ln.getListingId()+ln.getDepotId(), ln.getDepotId());
		}
		Map<String,IndexEntity> ieMap = searchService.getSearchProductMap(JSON.toJSONString(lists), lang, website);
		Currency cbo = this.getCurrencyBean(currency);
		
		Map<Integer,List<ShoppingCartProduct>> scpMap = new LinkedHashMap<Integer,List<ShoppingCartProduct>>();
		if(ieMap != null && ieMap.size() > 0){
			for (ListingIdNum ln : list) {
				ShoppingCartProduct pb = new ShoppingCartProduct();
				IndexEntity ie = ieMap.get(ln.getListingId());
				if(ie != null){
					//通过公共的方法设置父类的属性
					productPublicUtil.transformProductBase(pb, ie, cbo,ln.getDepotId());
					pb.setNum(lmap.get(ln.getListingId()+ln.getDepotId()));
					if(scpMap.containsKey(ln.getDepotId())){
						List<ShoppingCartProduct> scpList = scpMap.get(ln.getDepotId());
						scpList.add(pb);
						scpMap.put(ln.getDepotId(), scpList);
					}else{
						List<ShoppingCartProduct> scpList = new ArrayList<ShoppingCartProduct>();
						scpList.add(pb);
						scpMap.put(ln.getDepotId(), scpList);
					}
				}
			}
		}
		return scpMap;
	}

}
