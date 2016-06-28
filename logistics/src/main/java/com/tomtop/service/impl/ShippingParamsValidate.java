package com.tomtop.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.tomtop.entry.po.ShippingCalculateBase;
import com.tomtop.entry.po.ShippingPriceCalculate;

@Service
public class ShippingParamsValidate{
	private static final Logger log=Logger.getLogger(ShippingParamsValidate.class.getName());
	
	public  void validateParams(ShippingPriceCalculate spc,
			Map<String, Object> map) {
		if (spc==null || CollectionUtils.isEmpty(spc.getShippingCalculateBaseList())) {
			log.info("ShippingController validateParams null  spc:"+spc);
			map.put("msg", "Collection EMPTY");
		}else{
			//校验币种
			validateItem(spc.getCurrency(),"Currency","Currency is null",map);
			validateItem(spc.getTotalPrice(),"totalPrice","totalPrice is null",map);	
			validateItem(spc.getStorageId(),"storageId","storageId is null",map);
			validateItem(spc.getCountry(),"country","country is null",map);
			//validateItem(spc.getTotalWeight(),"totalWeight","totalWeight is null",map);
			
			List<ShippingCalculateBase> list=spc.getShippingCalculateBaseList();
			for(ShippingCalculateBase sb : list){
				if(sb==null){
					continue;
				}
				validateItem(sb.getQty(),sb.getSku(),"sku qty < 1",map);
				validateItem(sb.getSku(),sb.getSku(),"sku is null",map);
				validateItem(sb.getTemplateId(),sb.getSku(),"templeteId is null",map);
				validateItem(sb.isSpecial(),sb.getSku(),"Special is null",map);
				//validateItem(sb.getHigh(),sb.getSku(),"High is null",map);		
				//validateItem(sb.getLength(),sb.getSku(),"Length is null",map);
				//validateItem(sb.getWidth(),sb.getSku(),"Width is null",map);
				//validateItem(sb.getVolumeWeight(),sb.getSku(),"VolumeWeight is null",map);
			}
			
		}
	}
	@SuppressWarnings("unused")
	private  void validateItem(Object value,String key,String tips,Map<String, Object> map) {
		if(MapUtils.isNotEmpty(map)){
			return;
		}
		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(tips)){
			map.put("msg", "key or tips null  key:"+key +"  tips:"+tips );
			return;
		}
		if(value instanceof String){
			if (StringUtils.isEmpty((String)value) ) {
				map.put("msg",(String)key+" "+tips);
			}
		}else if(value instanceof Float){
			if ((float)value<0.0) {
				map.put("msg",key+" "+tips);
			}
		}else if(value instanceof Double){
			if ((double)value<0.0) {
				map.put("msg",key+" "+tips);
			}
		}else if(value instanceof Integer){
			if((int)value<1){
				map.put("msg",key+" "+tips);
			}
		}else if (value instanceof Boolean){
			if (value==null){
				map.put("msg",key+" "+tips);
			}
		}else{
			map.put("msg",key+" unknow type value:"+value);
		}
	}
}
