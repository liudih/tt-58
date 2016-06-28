package com.tomtop.service.impl;


import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.common.utils.VolumeweightCalculateUtils;
import com.tomtop.entry.bo.ShippingCalculateLessParam;
import com.tomtop.entry.bo.ShippingCalculateLessParamBase;
import com.tomtop.entry.po.ProductBase;
import com.tomtop.entry.po.ShippingCalculateBase;
import com.tomtop.entry.po.ShippingPriceCalculate;
import com.tomtop.service.ICacheManagerService;
import com.tomtop.service.IProductBaseInfoService;

@Service
public class ProductBaseInfoService implements IProductBaseInfoService{
	
	private static final Logger log=Logger.getLogger(ProductBaseInfoService.class.getName());
	
	@Autowired
	ICacheManagerService cacheManagerService;
	@Override
	public ShippingPriceCalculate getShippingCalculateBase(
			ShippingCalculateLessParam s) {
		if(s==null || StringUtils.isEmpty(s.getCurrency())
				|| CollectionUtils.isEmpty(s.getShippingCalculateLessParamBase())){
			log.info("shippingCalculateLessParam params null ! shippingCalculateLessParam:"+s);
			return null;
		}
		List<ShippingCalculateLessParamBase> pbList = s.getShippingCalculateLessParamBase();
		List<String> listingIds = Lists.transform(pbList, i->i.getListingId());
		log.error("@@@@getShippingCalculateBase listingIds:"+listingIds+"  @@@@@");
		int storageId=s.getStorageId();
		int language=s.getLanguage();
		double totalWeight=0;
		if(CollectionUtils.isNotEmpty(listingIds)){
			List<ProductBase> producdtBaseList =getProductList(listingIds,storageId);
			//List<ProductBase> producdtBaseList = productBaseMapper.getProductsByListingIds(listingIds,storageId,language);
			if(CollectionUtils.isNotEmpty(producdtBaseList)){
				ShippingPriceCalculate shippingPriceCalculate=new ShippingPriceCalculate();
				shippingPriceCalculate.setCurrency(s.getCurrency());
				shippingPriceCalculate.setTotalPrice(s.getTotalPrice());
				shippingPriceCalculate.setCountry(s.getCountry());
				shippingPriceCalculate.setStorageId(s.getStorageId());
				shippingPriceCalculate.setLanguage(s.getLanguage());
				//shippingPriceCalculate.setTemplateId(shippingPriceCalculate.getTemplateId());
				//shippingPriceCalculate.setTotalWeight(s.getTotalWeight());
				Map<String,ProductBase> map=Maps.uniqueIndex(producdtBaseList.iterator(), new Function<ProductBase,String>(){  
				    @Override
					public  String apply(ProductBase input) { 
				        return input.getClistingid();
				    }  
				});
				
				List<ShippingCalculateBase>  transform=Lists.newArrayList();
				for(ShippingCalculateLessParamBase i : pbList){
					ShippingCalculateBase shippingCalculateBase=new ShippingCalculateBase();
					if(map.containsKey(i.getListingId())){
						ProductBase pb = map.get(i.getListingId());
						
						shippingCalculateBase.setQty(i.getQty());
						/*shippingCalculateBase.setTemplateId(i.getTemplateId());*/
						shippingCalculateBase.setSku(pb.getCsku());
						shippingCalculateBase.setSpecial(pb.getBspecial());
						shippingCalculateBase.setTemplateId(pb.getTemplateid());
						//shippingCalculateBase.setPrice(sclp.getPrice());
						totalWeight=totalWeight+pb.getFweight()*i.getQty();//添加SKU本身重量
						String[] chrd = i.getChrd();
						Map<String, Double> childSku = getChildSku(chrd,storageId,language);
						if(MapUtils.isEmpty(childSku)){
							shippingCalculateBase.setHigh(pb.getFheight());
							shippingCalculateBase.setLength(pb.getFlength());
							shippingCalculateBase.setWidth(pb.getFwidth());
							shippingCalculateBase.setWeight(pb.getFweight());
							shippingCalculateBase.setVolumeWeight(pb.getFvolumeweight());
						}else{
							
							Map<String,Double> mutilProduct=Maps.newHashMap();
							mutilProduct.put("length", pb.getFlength());
							mutilProduct.put("width", pb.getFwidth());
							mutilProduct.put("high", pb.getFheight());
							mutilProduct.put("weight", pb.getFweight());
							mutilProduct.put("volumeweight", pb.getFvolumeweight());
							VolumeweightCalculateUtils.getMutilIqtyVolumeweight(mutilProduct, childSku.get("high"),
									childSku.get("width"), childSku.get("length"),childSku.get("weight"), childSku.get("volumeweight"),1 );
							shippingCalculateBase.setHigh(mutilProduct.get("high"));
							shippingCalculateBase.setLength(mutilProduct.get("length"));
							shippingCalculateBase.setWidth(mutilProduct.get("width"));
							shippingCalculateBase.setWeight(mutilProduct.get("weight"));// 单个产品重量
							shippingCalculateBase.setVolumeWeight(mutilProduct.get("volumeweight"));
							totalWeight=totalWeight+childSku.get("weight")*i.getQty();//添加SKU捆绑商品重量
						}
						
						transform.add(shippingCalculateBase);
					}else{
						throw new RuntimeException("ShippingPriceCalculate listingId not contain! listingId:"+i.getListingId());
					}
				}
				shippingPriceCalculate.setTotalWeight(totalWeight);
				shippingPriceCalculate.setShippingCalculateBaseList(transform);//设置好参数
				return shippingPriceCalculate;
			}else{
				log.info("shippingCalculateLessParam listingIds not exist !");
				return null;
			}
		}else{
			log.info("shippingCalculateLessParam listingIds empty !");
			return null;
		}
	}

	private Map<String,Double> getChildSku(String[] chrdList,int storageId,int language){
		 if(chrdList==null || chrdList.length<1){
			 return null;
		 }
		 List<String> list=Lists.newArrayList(chrdList);
		// List<ProductBase> productsByListingIds = productBaseMapper.getProductsByListingIds(list,storageId,language);
		 
		 List<ProductBase> productsByListingIds =getProductList(list,storageId);
		 if(CollectionUtils.isEmpty(productsByListingIds) || list.size()!=productsByListingIds.size()){
			 log.info("getChildSku getProduct fail ! list:"+list+"  productsByListingIds:"+productsByListingIds);
			 throw new RuntimeException("getChildSku getProduct fail ! list:"+list);
		 }else{
			 
			 Map<String,Double> map=Maps.newHashMap();
			 for(ProductBase productBase : productsByListingIds){
				 VolumeweightCalculateUtils.getMutilIqtyVolumeweight(map, productBase.getFheight(),
						 productBase.getFwidth(),  productBase.getFlength(),productBase.getFweight(), productBase.getFvolumeweight(),1 );
			 }
			 return map;
		 }
	}
	/**
	 * 产品缓存信息
	 * @param list
	 * @param storageId
	 * @param language
	 * @return
	 */
	private List<ProductBase> getProductList(List<String> list,final int storageId){
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		List<ProductBase> result=Lists.newArrayList();
		for(String item : list){
			ProductBase productItem = getProductItem(item,storageId);
			if(productItem!=null){
				result.add(productItem);
			}
		}
		return result;
		
	}
	private ProductBase getProductItem(String listingId,Integer storageId){
		return cacheManagerService.getProductItem(listingId, storageId);
		
	}
}
