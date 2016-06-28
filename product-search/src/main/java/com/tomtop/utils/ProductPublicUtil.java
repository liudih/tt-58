package com.tomtop.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomtop.entity.Currency;
import com.tomtop.entity.Depot;
import com.tomtop.entity.OrderProduct;
import com.tomtop.entity.ProductBase;
import com.tomtop.entity.ProductBaseDepot;
import com.tomtop.entity.ProductCompute;
import com.tomtop.entity.ProductDepotDetails;
import com.tomtop.entity.ProductDetails;
import com.tomtop.entity.ProductImage;
import com.tomtop.entity.index.AttributeItem;
import com.tomtop.entity.index.DepotEntity;
import com.tomtop.entity.index.IndexEntity;
import com.tomtop.entity.index.MutilLanguage;
import com.tomtop.entity.index.ProductImageEntity;
/**
 * 对象属性赋值工具类
 * @author renyy
 *
 */
@Component
public class ProductPublicUtil {

	private static final Logger logger = LoggerFactory.getLogger(ProductPublicUtil.class);
	
	@Autowired
	ProductComputeUtil productComputeUtil;
	
	
	public void transformProductBase(ProductBase pb, IndexEntity ie,Currency currencyBean){
		pb.setImageUrl(ie.getDefaultImgUrl());
		pb.setListingId(ie.getListingId());
		pb.setSku(ie.getSku());
		if(ie.getMutil() != null){
			pb.setTitle(ie.getMutil().getTitle());
			if(ie.getMutil().getUrl() != null && ie.getMutil().getUrl().size() > 0){
				pb.setUrl(ie.getMutil().getUrl().get(0));
			}
		}
		ProductCompute pc = productComputeUtil.getPrice(ie.getCostPrice(), ie.getYjPrice(), ie.getPromotionPrice(), currencyBean);
		pb.setOrigprice(pc.getOriginalPrice());
		pb.setNowprice(pc.getPrice());
		pb.setSymbol(currencyBean.getSymbolCode());
	}
	
	/**
	 * 根据仓库获取对象与价格
	 * 
	 * @param pb
	 *       商品基础的对象
	 * @param ie
	 *      搜索引擎对象
	 * @param currencyBean
	 *        货币对象
	 * @param whouse
	 *         仓库名称
	 */
	public void transformProductBase(ProductBase pb, IndexEntity ie,Currency currencyBean,String whouse){
		pb.setImageUrl(ie.getDefaultImgUrl());
		pb.setListingId(ie.getListingId());
		pb.setSku(ie.getSku());
		if(ie.getMutil() != null){
			pb.setTitle(ie.getMutil().getTitle());
			if(ie.getMutil().getUrl() != null && ie.getMutil().getUrl().size() > 0){
				pb.setUrl(ie.getMutil().getUrl().get(0));
			}
		}
		if(ie.getDepots() != null && ie.getDepots().size() > 0){
			String original = "";
			String nowprice = "";
			for (DepotEntity de : ie.getDepots()) {
				if(whouse.equals(de.getDepotName())){
					ProductCompute pc = productComputeUtil.getDepotPrice(ie.getCostPrice(),de.getPrice(), de.getSalePrice(), currencyBean);
					pb.setOrigprice(pc.getOriginalPrice());
					pb.setNowprice(pc.getPrice());
					break;
				}else if("".equals(original) && "".equals(nowprice)){
						ProductCompute pc = productComputeUtil.getDepotPrice(ie.getCostPrice(),de.getPrice(), de.getSalePrice(), currencyBean);
						original = pc.getOriginalPrice();
						nowprice = pc.getPrice();
				}
			}
			if(pb.getOrigprice() == null || pb.getNowprice() == null ||
					"".equals(pb.getOrigprice()) || "".equals(pb.getNowprice())){
				pb.setOrigprice(original);
				pb.setNowprice(nowprice);
			}
			
			pb.setSymbol(currencyBean.getSymbolCode());
		}
		
	}
	/**
	 * 
	 * @param pb
	 *       商品基础的对象
	 * @param ie
	 *      搜索引擎对象
	 * @param currencyBean
	 *        货币对象
	 * @param Map<String,Integer>
	 *         listingId对应的仓库Id
	 */
	public void transformProductBase(ProductBase pb, IndexEntity ie,Currency currencyBean,Integer depotId){
		pb.setImageUrl(ie.getDefaultImgUrl());
		pb.setListingId(ie.getListingId());
		pb.setSku(ie.getSku());
		if(ie.getMutil() != null){
			pb.setTitle(ie.getMutil().getTitle());
			if(ie.getMutil().getUrl() != null && ie.getMutil().getUrl().size() > 0){
				pb.setUrl(ie.getMutil().getUrl().get(0));
			}
		}
		if(ie.getDepots() != null && ie.getDepots().size() > 0){
			String original = "";
			String nowprice = "";
			for (DepotEntity de : ie.getDepots()) {
				if(depotId == de.getDepotid()){
					if(de.getStatus() == CommonDefn.TWO ||
						de.getStatus() == CommonDefn.MINUSTEN){
						continue;
					}
					ProductCompute pc = productComputeUtil.getDepotPrice(ie.getCostPrice(),de.getPrice(), de.getSalePrice(), currencyBean);
					pb.setOrigprice(pc.getOriginalPrice());
					pb.setNowprice(pc.getPrice());
					break;
				}else{
					if(de.getStatus() == CommonDefn.TWO ||
							de.getStatus() == CommonDefn.MINUSTEN){
							continue;
					}
					ProductCompute pc = productComputeUtil.getDepotPrice(ie.getCostPrice(),de.getPrice(), de.getSalePrice(), currencyBean);
					original = pc.getOriginalPrice();
					nowprice = pc.getPrice();
				}
			}
			if(pb.getOrigprice() == null || pb.getNowprice() == null ||
					"".equals(pb.getOrigprice()) || "".equals(pb.getNowprice())){
				pb.setOrigprice(original);
				pb.setNowprice(nowprice);
			}
			
			pb.setSymbol(currencyBean.getSymbolCode());
		}
		
	}
	/**
	 * 
	 * @param pb
	 *       商品基础的对象
	 * @param ie
	 *      搜索引擎对象
	 * @param depotId
	 *        仓库Id
	 */
	public void transformOrderProduct(OrderProduct op, IndexEntity ie,Integer depotId){
		op.setImageUrl(ie.getDefaultImgUrl());
		op.setListingId(ie.getListingId());
		op.setSku(ie.getSku());
		if(ie.getMutil() != null){
			op.setTitle(ie.getMutil().getTitle());
			if(ie.getMutil().getUrl() != null && ie.getMutil().getUrl().size() > 0){
				op.setUrl(ie.getMutil().getUrl().get(0));
			}
			Map<String, String> attributeMap = new HashMap<String, String>();
			List<AttributeItem> aitList  = ie.getMutil().getItems();
			if(aitList != null && aitList.size() > 0){
				for (int j = 0; j < aitList.size(); j++) {
					AttributeItem ai = aitList.get(j);
					//属性为isShow=true才显示
					if(ai.getIsShow() && ai.getIsMult()){
						String keyName = ai.getKey();
						String valueName = ai.getValue();
						attributeMap.put(keyName, valueName);
					}
				}
			}
			op.setAttributeMap(attributeMap);
		}
		if(ie.getDepots() != null && ie.getDepots().size() > 0){
			for (DepotEntity de : ie.getDepots()) {
				if(depotId == de.getDepotid()){
					op.setStatus(de.getStatus());
				}
			}
		}
		
	}
	public void transformProductBaseDepot(ProductBaseDepot pbd, IndexEntity ie,Currency currencyBean){
		pbd.setImageUrl(ie.getDefaultImgUrl());
		pbd.setListingId(ie.getListingId());
		pbd.setSku(ie.getSku());
		if(ie.getMutil() != null){
			pbd.setTitle(ie.getMutil().getTitle());
			if(ie.getMutil().getUrl() != null && ie.getMutil().getUrl().size() > 0){
				pbd.setUrl(ie.getMutil().getUrl().get(0));
			}
		}
		List<Depot> delist = new ArrayList<Depot>();
		if(ie.getDepots() != null && ie.getDepots().size() > 0){
			for (DepotEntity de : ie.getDepots()) {
				ProductCompute pc = productComputeUtil.getDepotPrice(ie.getCostPrice(),de.getPrice(), de.getSalePrice(), currencyBean);
				//仓库为中国仓 并且为freeshipping
				/*if(de.getDepotid() == CommonDefn.ONE && ie.getIsFreeShipping()){
					d = new Depot(de.getDepotid(), de.getDepotName(), de.getLid(), 
							de.getStatus(), de.getQty(), currencyBean.getSymbolCode(), 
							pc.getPrice(), pc.getOriginalPrice(), pc.getEndDate(),ie.getIsFreeShipping());
				}else{
					d = new Depot(de.getDepotid(), de.getDepotName(), de.getLid(), 
							de.getStatus(), de.getQty(), currencyBean.getSymbolCode(), 
							pc.getPrice(), pc.getOriginalPrice(), pc.getEndDate(),false);
				}*/
				
				delist.add( new Depot(de.getDepotid(), de.getDepotName(), de.getLid(), 
						de.getStatus(), de.getQty(), currencyBean.getSymbolCode(), 
						pc.getPrice(), pc.getOriginalPrice(), pc.getEndDate(),de.isFreeShipping()));
			}
		}
		pbd.setDlist(delist);
	}
	
	public void transformProductDtl(ProductDetails pdbo,IndexEntity ie,String key,Integer langId,Currency cbo){
		MutilLanguage mutil = ie.getMutil();
		if(mutil == null){
			logger.error("transformProductDtl mutil is null [" + ie.getSku() + "]");
		}
		pdbo.setTitle(mutil.getTitle());
		pdbo.setSku(ie.getSku());
		pdbo.setStatus(ie.getStatus());
		Integer qty = ie.getStoreNum();
		if(qty == null){
			pdbo.setQty(0);
		}else{
			pdbo.setQty(qty);
		}
		pdbo.setListingId(ie.getListingId());
		pdbo.setIsFreeShipping(ie.getIsFreeShipping());
		ProductCompute pprbo = productComputeUtil.getPriceEndDate(ie.getCostPrice(), ie.getYjPrice(), ie.getPromotionPrice(), cbo);
		pdbo.setOrigprice(pprbo.getOriginalPrice());//原价
		pdbo.setNowprice(pprbo.getPrice());//折扣价
		pdbo.setSymbol(cbo.getSymbolCode());
		if(!"".equals(pprbo.getEndDate())){
			pdbo.setIsOnSale(true);
			pdbo.setSaleEndDate(pprbo.getEndDate());
		}else{
			pdbo.setIsOnSale(false);
		}
		List<ProductImage> piboList = new ArrayList<ProductImage>();
		List<ProductImageEntity> pimgEnt = ie.getImgs();
		for (int j = 0; j < pimgEnt.size(); j++) {
			ProductImageEntity pie = pimgEnt.get(j);
			ProductImage pimgbo = new ProductImage();
			pimgbo.setImgUrl(pie.getUrl());
			pimgbo.setIsMain(pie.getIsBase());
			pimgbo.setIsSmall(pie.getIsSmall());
			pimgbo.setIsThumb(pie.getIsThumb());
			pimgbo.setIsDetails(pie.getIsDetails());
			piboList.add(pimgbo);
		}
		pdbo.setImgList(piboList);
		
		Map<String, String> attributeMap = new HashMap<String, String>();
		List<AttributeItem> aitList  = mutil.getItems();
		if(aitList != null && aitList.size() > 0){
			for (int j = 0; j < aitList.size(); j++) {
				AttributeItem ai = aitList.get(j);
				//属性为isShow=true才显示
				if(ai.getIsShow() && ai.getIsMult()){
					String keyName = ai.getKey().toLowerCase().trim().replaceAll(" ", "_");
					String valueName = ai.getValue();
					attributeMap.put(CommonsUtil.replceKeyName(keyName), valueName);
				}
			}
		}
		pdbo.setAttributeMap(attributeMap);
		if(mutil.getUrl() != null && mutil.getUrl().size() > 0){
			boolean isUrl = false;
				for (int j = 0; j < mutil.getUrl().size(); j++) {
					if(mutil.getUrl().get(j).equals(key.trim())){
						pdbo.setUrl(mutil.getUrl().get(j));
						isUrl = true;
					}
				}
				if(isUrl == false){
					pdbo.setUrl(mutil.getUrl().get(0));
				}
		}
	}
	
	public void transformProductDepotDetails(ProductDepotDetails pdd,IndexEntity ie,String key,Integer langId,Currency cbo){
		MutilLanguage mutil = ie.getMutil();
		if(mutil == null){
			logger.error("transformProductDepotDetails mutil is null [" + ie.getSku() + "]");
		}
		pdd.setTitle(mutil.getTitle());
		pdd.setSku(ie.getSku());
		pdd.setListingId(ie.getListingId());
		List<ProductImage> piboList = new ArrayList<ProductImage>();
		List<ProductImageEntity> pimgEnt = ie.getImgs();
		for (int j = 0; j < pimgEnt.size(); j++) {
			ProductImageEntity pie = pimgEnt.get(j);
			ProductImage pimgbo = new ProductImage();
			pimgbo.setImgUrl(pie.getUrl());
			pimgbo.setIsMain(pie.getIsBase());
			pimgbo.setIsSmall(pie.getIsSmall());
			pimgbo.setIsThumb(pie.getIsThumb());
			pimgbo.setIsDetails(pie.getIsDetails());
			piboList.add(pimgbo);
		}
		pdd.setImgList(piboList);
		
		Map<String, String> attributeMap = new HashMap<String, String>();
		List<AttributeItem> aitList  = mutil.getItems();
		if(aitList != null && aitList.size() > 0){
			for (int j = 0; j < aitList.size(); j++) {
				AttributeItem ai = aitList.get(j);
				//属性为isShow=true才显示
				if(ai.getIsShow() && ai.getIsMult()){
					String keyName = ai.getKey().toLowerCase().trim().replaceAll(" ", "_");
					String valueName = ai.getValue();
					attributeMap.put(CommonsUtil.replceKeyName(keyName), valueName);
				}
			}
		}
		
		pdd.setAttributeMap(attributeMap);
		if(mutil.getUrl() != null && mutil.getUrl().size() > 0){
			boolean isUrl = false;
				for (int j = 0; j < mutil.getUrl().size(); j++) {
					if(mutil.getUrl().get(j).equals(key.trim())){
						pdd.setUrl(mutil.getUrl().get(j));
						isUrl = true;
					}
				}
				if(isUrl == false){
					pdd.setUrl(mutil.getUrl().get(0));
				}
		}
		String jumpUrl = "";
		if(mutil.getJumpUrl() == null || "".equals(mutil.getJumpUrl())){
			jumpUrl = key;
		}else{
			jumpUrl = mutil.getJumpUrl();
		}
		pdd.setJumpUrl(jumpUrl);
		
		Map<String,Depot> whouse = new LinkedHashMap<String,Depot>();
		if(ie.getDepots() != null && ie.getDepots().size() > 0){
			for (DepotEntity de : ie.getDepots()) {
				if(de.getStatus() == CommonDefn.MINUSTEN){
						continue;
				}
				ProductCompute pc = productComputeUtil.getDepotPrice(ie.getCostPrice(),de.getPrice(), de.getSalePrice(), cbo);
				//仓库为中国仓 并且为freeshipping
				Depot d = new Depot(de.getDepotid(), de.getDepotName(), de.getLid(), 
							de.getStatus(), de.getQty(), cbo.getSymbolCode(), 
							pc.getPrice(), pc.getOriginalPrice(), pc.getEndDate(),de.isFreeShipping());
				whouse.put(de.getDepotName(), d);
			}
			pdd.setWhouse(whouse);
		}
	}
	
}