package com.tomtop.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.TreeMultimap;
import com.tomtop.common.constants.Cons;
import com.tomtop.common.enums.CountryTypeEnum;
import com.tomtop.common.enums.FilterTypeEnum;
import com.tomtop.common.enums.ShippingType;
import com.tomtop.common.utils.CacheUtils;
import com.tomtop.common.utils.VolumeweightCalculateUtils;
import com.tomtop.entry.bo.ShippingPriceBo;
import com.tomtop.entry.bo.VolumeWeightBo;
import com.tomtop.entry.form.ShippingPriceCalculateDto;
import com.tomtop.entry.form.ShippingPriceParamsDto;
import com.tomtop.entry.po.ShippingCalculateBase;
import com.tomtop.entry.po.ShippingDisplayNamePo;
import com.tomtop.entry.po.ShippingMethod;
import com.tomtop.entry.po.ShippingPriceCalculate;
import com.tomtop.entry.po.ShippingTemplate;
import com.tomtop.entry.po.ShippingTypePo;
import com.tomtop.entry.vo.CacheParamsVo;
import com.tomtop.entry.vo.ShippingTitleDescribeParamsVo;
import com.tomtop.mapper.shipping.NewShippingMethodMapper;
import com.tomtop.mapper.shipping.ShippingTemplateMapper;
import com.tomtop.service.ICacheManagerService;
import com.tomtop.service.ICurrencyService;
import com.tomtop.service.IShippingManageService;

@Service
public class ShippingManageServiceImp implements IShippingManageService{
	
	private static final Logger log=Logger.getLogger(ShippingManageServiceImp.class.getName());
	
	private static final float PRICE_POINT=30f;

	private static final Double SHIPPING_OR=60.0;
	
	private static final Double SHIPPING_AND=90.0;
	
	private static final Double SHIPPING_WEIGHT=2000.0;//已克为单位
	
	private static final String USD_CURRENCY="USD";
	
	private static final String SHIPPING_COUNTRY_SWITCH="2";
	@Autowired
	NewShippingMethodMapper newShippingMethodMapper;
	@Autowired
	ShippingTemplateMapper shippingTemplateMapper;
	@Autowired
	FreightService freightService;
	@Autowired
	ICurrencyService currencyService;
	
	@Autowired
	ICacheManagerService cacheManagerService;
	@Autowired
	CacheUtils cacheUtils;
	@Override
	public ShippingPriceBo getShippingPrice(ShippingPriceCalculate shippingPriceCalculate) {
		if(shippingPriceCalculate!=null && shippingPriceCalculate.getShippingCalculateBaseList().size()==1){//做缓存处理，否则不做缓存处理
			
			/****************************************  带缓存优化项   ****************************************/
			return getShippingPriceManager(shippingPriceCalculate);
		}else{
			return getShippingPriceManager(shippingPriceCalculate);
		}
	}
    /****************************************  带缓存优化项   ****************************************/
	@Override
	public ShippingPriceBo getShippingPriceSimple(ShippingPriceCalculate shippingPriceCalculate) {
		
		return getShippingPriceManager( shippingPriceCalculate);
	}
	/**
	 * 1:通过 sku获取模板列表
	 * 2：整理各种类型下面，可发送发货代码列表   eg:平邮  --》HT,F 挂号  ----》wj,uu
	 * 3:各种类型独立过滤和运算
	 * 4：将各个模块计算值，存入结果中
	 */
	private ShippingPriceBo getShippingPriceManager(ShippingPriceCalculate shippingPriceCalculate ){
		
		ShippingPriceBo shippingPriceBo=new ShippingPriceBo();
		shippingPriceBo.setExpressShow(false);
		shippingPriceBo.setRegistShow(false);
		shippingPriceBo.setSpecialShow(false);
		shippingPriceBo.setSurfaceShow(false);
		shippingPriceBo.setSurfaceMapRegist("N");
		shippingPriceBo.setExpressCodeMap(new HashMap<String,String>());//初始化CODE
		shippingPriceBo.setSpecialCodeMap(new HashMap<String,String>());
		shippingPriceBo.setSurfaceCodeMap(new HashMap<String,String>());
		shippingPriceBo.setRegistCodeMap(new HashMap<String,String>());
		if(shippingPriceCalculate==null || CollectionUtils.isEmpty(shippingPriceCalculate.getShippingCalculateBaseList())){
			log.info("getShippingPriceManager ShippingCalculateBase is empty!");
			return shippingPriceBo;
		}
		
		//平邮模板列表
		List<ShippingTemplate> plist=Lists.newArrayList();
		//挂号模板列表
		List<ShippingTemplate> glist=Lists.newArrayList();
		//快递模板列表
		List<ShippingTemplate> klist=Lists.newArrayList();
		//特快模板列表
		List<ShippingTemplate> tlist=Lists.newArrayList();
		
		//1：通过SKU获取可用模板列别
		//List<String> transform = Lists.transform(list, i->i.getTemplateId() );
		double exchange = currencyService.exchange(shippingPriceCalculate.getCurrency(), USD_CURRENCY);
		
		List<ShippingTemplate> templateList = getTemplate(exchange, shippingPriceCalculate,shippingPriceBo);//设置好模板类型和过滤条件
		
		if(CollectionUtils.isEmpty(templateList)){
			shippingPriceBo.setExpressShow(false);
			shippingPriceBo.setRegistShow(false);
			shippingPriceBo.setSpecialShow(false);
			shippingPriceBo.setSurfaceShow(false);
			log.info("getShippingPriceManager templateList is empty!");
			return shippingPriceBo;
		}
		//2：整理各种类型下面，可发送发货代码列表   eg:平邮  --》HT,F 挂号  ----》wj,uu
		groupTemplate(plist,glist,klist,tlist,templateList);
		
		//3:各种类型独立过滤和运算
		filterAndCalculate(exchange,plist,glist,klist,tlist,shippingPriceBo, shippingPriceCalculate);
		
		
		//4:将结果存入实体输出
		return shippingPriceBo;
	}
	
	private void filterAndCalculate(double exchange,List<ShippingTemplate> p,List<ShippingTemplate> g
			,List<ShippingTemplate> k
			,List<ShippingTemplate> t,
			ShippingPriceBo shippingPriceBo,
			ShippingPriceCalculate shippingPriceCalculate){
		
		//计算平邮
		surfaceFilterAndCalculateItem(exchange,p,shippingPriceBo, shippingPriceCalculate);
		//计算挂号
		registFilterAndCalculateItem(g,shippingPriceBo,shippingPriceCalculate);
		
		Map<String, Boolean> fileFreeWeight = fileFreeWeight(k,t);
		//计算快递
		expressFilterAndCalculateItem(k,shippingPriceBo,shippingPriceCalculate,fileFreeWeight);
				//计算特快
		specialFilterAndCalculateItem(t,shippingPriceBo,shippingPriceCalculate,fileFreeWeight);
	}
	private Map<String,Boolean> fileFreeWeight(List<ShippingTemplate> k,List<ShippingTemplate> t){
		List<ShippingTemplate> fillList=Lists.newArrayList();
		fillList.addAll(k);
		fillList.addAll(t);
		Map<String,Boolean> map=Maps.newHashMap();
		if(CollectionUtils.isNotEmpty(fillList)){
			Map<String,ShippingTemplate> tempMap=Maps.newHashMap();
			for(ShippingTemplate shippingTemplate : fillList){
				if(ShippingType.ExpressType.toString().equals(shippingTemplate.getTypeName()) || 
						ShippingType.SpecialType.toString().equals(shippingTemplate.getTypeName())){
					tempMap.put(shippingTemplate.getShippingTemplateId()+"_"+shippingTemplate.getTypeName(), shippingTemplate);
				}
			}
			if(MapUtils.isNotEmpty(tempMap)){
				 Iterator<Entry<String, ShippingTemplate>> iterator = tempMap.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String, ShippingTemplate> next = iterator.next();
					ShippingTemplate value = next.getValue();
					String key = next.getKey();
					String expressKey=value.getShippingTemplateId()+"_"+ShippingType.ExpressType.toString();
					String specialKey=value.getShippingTemplateId()+"_"+ShippingType.SpecialType.toString();
					if(((expressKey).equals(key) && !map.containsKey(specialKey)) ){
						map.put(specialKey, value.isWhetherFreeshipping());
					}else if( (specialKey.equals(key) && !map.containsKey(expressKey))){
						map.put(expressKey, value.isWhetherFreeshipping());
					}
				}
			}
		}
		log.info("--------->fileFreeWeight:"+map);
		return map;
	} 
	private boolean filterVolume(List<ShippingCalculateBase> list,
			Map<String,ShippingTemplate> map,ShippingPriceBo shippingPriceBo,Map<String,String> codeMap,
			String templateType,VolumeWeightBo volumeWeightBo,int languageId){
		if(CollectionUtils.isEmpty(list) || MapUtils.isEmpty(map) || volumeWeightBo==null){
			shippingPriceBo.getSurfaceCodeMap().put(Cons.C_SURFACE_NOT_ALLOW, getSystemParamValue(Cons.C_SURFACE_NOT_ALLOW+"_"+languageId));
			log.info("filterVolumeWeight empty! map:"+map+"  list:"+list+"  volumeWeightBo:"+volumeWeightBo);
			return false;
		}
		Map<String,Double> bordMap=Maps.newHashMap();
		for(ShippingCalculateBase shippingCalculateBase : list){
			int qty=shippingCalculateBase.getQty();
			VolumeweightCalculateUtils.getMutilIqtyVolumeweight(bordMap, shippingCalculateBase.getHigh(),
					shippingCalculateBase.getWidth(),shippingCalculateBase.getLength(),0.0, shippingCalculateBase.getVolumeWeight(),qty );
		}	
		volumeWeightBo.setHigh(bordMap.get("high"));
		volumeWeightBo.setLength(bordMap.get("length"));
		volumeWeightBo.setWidth(bordMap.get("width"));
		volumeWeightBo.setVolumeWeight(bordMap.get("volumeweight"));
		log.info("filterVolumeWeight templateType:"+templateType+"    volumeWeightBo:"+ volumeWeightBo);
		boolean temp=(volumeWeightBo.getHigh()==0 && volumeWeightBo.getLength()==0 && volumeWeightBo.getWidth()==0 && volumeWeightBo.getVolumeWeight()==0);
		if(!temp){//如果长宽高都为0，说明数据没有被初始化，不走过滤条件
			boolean total=(volumeWeightBo.getHigh()+volumeWeightBo.getLength()+volumeWeightBo.getWidth())>SHIPPING_AND;
			boolean item=volumeWeightBo.getHigh()>SHIPPING_OR ||volumeWeightBo.getLength()>SHIPPING_OR || volumeWeightBo.getWidth()>SHIPPING_OR ;
			boolean lwx= volumeWeightBo.getWeight()>SHIPPING_WEIGHT;
			if(item || total){
				codeMap.put(Cons.C_SUPER_STANDARD, getSystemParamValue(Cons.C_SUPER_STANDARD+"_"+languageId));
			}
			if(lwx){
				codeMap.put(Cons.C_OVER_WEIGHT, getSystemParamValue(Cons.C_OVER_WEIGHT+"_"+languageId));
			}
			if(item||total||lwx ){
				shippingPriceBo.setSurfaceMapRegist("Y");
				shippingPriceBo.setSurfaceShow(false);
				shippingPriceBo.setRegistShow(false);
				return true;
			}
		}
		return false;
	}
	
	private String getSystemParamValue(String key){
		String value="";
		Map<String, String> systemParams = cacheManagerService.getSystemParams();
		//String key=Cons.C_SURFACE_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage();
		if(MapUtils.isNotEmpty(systemParams) && systemParams.containsKey(key)){
			value=systemParams.get(key);
		}
		return value;
	}
	/**0：是否每个SKu都包含平邮
	 * 1：订单金额》30$
	 * 		大于30：
	 * 			a>处理免邮,全部免邮的，显示挂号费为0，不显示平邮
	 * 			b>不显示平邮
	 * 		结束  ------------end
	 * 2:是否为混合模板，如果有就以特殊模板计算
	 * 3：计算模板
	 * 4：取运费最高 ，相等的话，取其中一个
	 * 
	 */
	private void surfaceFilterAndCalculateItem(double exchange,List<ShippingTemplate> tempList,
			ShippingPriceBo shippingPriceBo,ShippingPriceCalculate shippingPriceCalculate){
		long timeStart=System.currentTimeMillis();
		
		if(CollectionUtils.isEmpty(tempList) ){
			log.info("surfaceFilterAndCalculateItem tempList empty!");
			return;
		}
		if(tempList.size()!=shippingPriceCalculate.getShippingCalculateBaseList().size()){
			log.info("surfaceFilterAndCalculateItem tempList empty!");
			shippingPriceBo.setSurfaceShow(false);
			shippingPriceBo.getSurfaceCodeMap().put(Cons.C_SURFACE_NOT_ALLOW,getSystemParamValue(Cons.C_SURFACE_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
			//发货代码，后面在设置
			return;
		}
		//初始值
		shippingPriceBo.setSurfaceShow(false);
		
		try{
			
			Double priceTotal=0.0;
			Double weightTotal=0.0;//消除免邮项，如果有SKU都为免邮，那么此项重量为0
			boolean isAllFree=true;//是否全为免邮
			VolumeWeightBo volumeWeightBo=new VolumeWeightBo();//体积重参数;排除免邮SKU计算
			List<ShippingTemplate> specialTempList=Lists.newArrayList();//特殊模板
			Map<String,ShippingTemplate> tempMap=Maps.newHashMap();//存储模板ID 和模板项
			String freeCode="";
			//
			for(ShippingTemplate shippingTemplate : tempList){
				if(!shippingTemplate.isWhetherFreeshipping()){//是否免邮
					isAllFree=false;
				}else if(StringUtils.isEmpty(freeCode)){
					String priorityShippingCode = shippingTemplate.getPriorityShippingCode();
					if(StringUtils.isNotEmpty(priorityShippingCode)){
						freeCode=priorityShippingCode.split(",")[0];//获取其中一个免邮的发货代码
					}
				}
				if(shippingTemplate.isWhetherSpecial()){//是否为特殊模板
					specialTempList.add(shippingTemplate);
				}
				//用模板ID+“——”+模板类型 结构构造key
				tempMap.put(shippingTemplate.getShippingTemplateId()+"_"+shippingTemplate.getTypeName(), shippingTemplate);
			}
			//过滤体积重
			if(filterVolume(shippingPriceCalculate.getShippingCalculateBaseList(),tempMap,shippingPriceBo, 
					shippingPriceBo.getSurfaceCodeMap(),ShippingType.SurfaceType.toString(),volumeWeightBo,shippingPriceCalculate.getLanguage())){
			/*if(filterVolume(shippingPriceCalculate.getShippingCalculateBaseList(),tempMap,shippingPriceBo, 
					shippingPriceBo.getSurfaceCodeMap(),Cons.C_SUPER_STANDARD,Cons.C_OVER_WEIGHT,ShippingType.SurfaceType.toString(),volumeWeightBo)){*/
				log.info("surfaceFilterAndCalculateItem ------------>shippingPriceBo:"+shippingPriceBo);
				return;
			}
			
			log.info("surfaceFilterAndCalculateItem ------------>templateMap:"+tempMap);
			
			priceTotal=shippingPriceCalculate.getTotalPrice();
			for(ShippingCalculateBase shippingCalculateBase : shippingPriceCalculate.getShippingCalculateBaseList()){
				String mapKey=shippingCalculateBase.getTemplateId()+"_"+ShippingType.SurfaceType.toString();
				log.info("surfaceFilterAndCalculateItem sku:"+shippingCalculateBase.getSku()+"------------>mapKey:"+mapKey);
				if(tempMap.containsKey(mapKey)){//处理免邮项不参加体积计算,并且模板该计算实体中，如果不存在 模板ID+“——”+类型
					ShippingTemplate shippingTemplate = tempMap.get(mapKey);
					
					//设置好是否为特殊品
					shippingCalculateBase.setSpecial(shippingTemplate.isWhetherSpecial());
					if(shippingTemplate.isWhetherFreeshipping()){
						log.debug("surfaceFilterAndCalculateItem ShippingCalculateBase template is freeshipping");
					}else{
						weightTotal=weightTotal+shippingCalculateBase.getWeight()*shippingCalculateBase.getQty();
					}
				}else{//如果该SKU 没有对应的模板ID的平邮类型，那么平邮发不了或，终止计算
					shippingPriceBo.setSurfaceShow(false);
					log.info("surfaceFilterAndCalculateItem ShippingCalculateBase not contain templateId+'_'"+
							ShippingType.SurfaceType.toString()+"! shippingCalculateBase:"+shippingCalculateBase);
					shippingPriceBo.getSurfaceCodeMap().put(Cons.C_SURFACE_NOT_ALLOW,getSystemParamValue(Cons.C_SURFACE_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
					return ;
				}
				
			}
			log.info("surfaceFilterAndCalculateItem ---->priceTotal:"+priceTotal);
			log.info("surfaceFilterAndCalculateItem ---->weightTotal:"+weightTotal);
			//转换币种
			
			double proceFilterPrice=exchange*priceTotal;
			log.info("surfaceFilterAndCalculateItem exchange:"+exchange+"  proceFilterPrice:"+proceFilterPrice);
			if(proceFilterPrice>PRICE_POINT){//订单金额大于PRICE_POINT（30）
				log.info("surfaceFilterAndCalculateItem priceTotal >"+PRICE_POINT);
				shippingPriceBo.getSurfaceCodeMap().put(Cons.C_ORDER_PRICE_OVER, getSystemParamValue(Cons.C_ORDER_PRICE_OVER+"_"+shippingPriceCalculate.getLanguage()));
				if(isAllFree){
					shippingPriceBo.setSurfaceTitle(shippingPriceCalculate.getSurfaceTitle());
					shippingPriceBo.setSurfaceDescription(shippingPriceCalculate.getSurfaceDescription());
					shippingPriceBo.setRegistPrice(0.0);
					shippingPriceBo.setRegistShow(true);
					shippingPriceBo.setSurfaceMapRegist("Y");
				}else{
					log.info("surfaceFilterAndCalculateItem isAllFree:"+isAllFree);//不显示平邮
				}
				shippingPriceBo.setSurfacePrice(0.0);//平邮不显示
				shippingPriceBo.setSurfaceShow(false);
			}else{
				//如果是全免邮
				if(isAllFree){
					Map<String, Object> freeCodeAndTemplate = this.getFreeCodeAndTemplate(tempList, shippingPriceCalculate.getTotalWeight(), shippingPriceCalculate.getStorageId(), shippingPriceCalculate.getCountry());
					if(MapUtils.isNotEmpty(freeCodeAndTemplate)){
						ShippingMethod shippingMethod = (ShippingMethod) freeCodeAndTemplate.get("code");
						ShippingTemplate filerShippingTemplate = (ShippingTemplate) freeCodeAndTemplate.get("template");
						Boolean bistracking = shippingMethod.getBistracking();
						shippingPriceBo.setSurfaceStrack((bistracking!=null && bistracking==true)?true:false);
						shippingPriceBo.setSurfaceCode(shippingMethod.getCcode());
						shippingPriceBo.setSurfaceCodeId(shippingMethod.getIid());
						shippingPriceBo.setSurfaceOrder(filerShippingTemplate.getTemplateOrder());
						shippingPriceBo.setSurfaceTitle(shippingPriceCalculate.getSurfaceTitle());
						shippingPriceBo.setSurfaceDescription(shippingPriceCalculate.getSurfaceDescription());
						log.info("surfaceFilterAndCalculateItem is all freeShipping! weightTotal:"+weightTotal);
						shippingPriceBo.setSurfaceShow(true);
						shippingPriceBo.setSurfacePrice(0.0);
					}else{
						shippingPriceBo.getSurfaceCodeMap().put(Cons.C_SURFACE_NOT_ALLOW,getSystemParamValue(Cons.C_SURFACE_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
					}
					return;
				}
				final String country=shippingPriceCalculate.getCountry();
				final int storageId=shippingPriceCalculate.getStorageId();
				TreeMap<Double,String> treeMap=new TreeMap<Double,String>();
				Map<String,ShippingMethod> shippingMethodMap=Maps.newHashMap();
				Map<String,ShippingTemplate> shippingTemplateMap=Maps.newHashMap();
				final int languageId=shippingPriceCalculate.getLanguage();
				if(specialTempList.size()>0){//如果存在特殊模板
					//获取物流计算规则
					for(ShippingTemplate shippingTemplate : specialTempList){
						for(ShippingCalculateBase shippingCalculateBase : shippingPriceCalculate.getShippingCalculateBaseList()){
							Entry<Double, String> calculateItem = calculateItem( shippingMethodMap,shippingTemplate, shippingCalculateBase,
									weightTotal,shippingPriceCalculate.getTotalWeight(),shippingPriceCalculate.getCurrency(),
									volumeWeightBo,ShippingType.SurfaceType.toString(),country,storageId,shippingPriceBo.getSurfaceCodeMap(),languageId);
							if(calculateItem!=null){
								
								treeMap.put(calculateItem.getKey(), calculateItem.getValue());
								shippingTemplateMap.put(calculateItem.getValue(), shippingTemplate);
							}else{
								log.info("surfaceFilterAndCalculateItem calculateItem null !shippingTemplate:"+shippingTemplate);
							}
						}
					}
					
				}else{
					for(ShippingTemplate shippingTemplate : tempList){
						for(ShippingCalculateBase shippingCalculateBase : shippingPriceCalculate.getShippingCalculateBaseList()){
							Entry<Double, String> calculateItem = calculateItem(shippingMethodMap, shippingTemplate, shippingCalculateBase,
									weightTotal,shippingPriceCalculate.getTotalWeight(),shippingPriceCalculate.getCurrency(),
									volumeWeightBo,ShippingType.SurfaceType.toString(),country,storageId,shippingPriceBo.getSurfaceCodeMap(),languageId);
							if(calculateItem!=null){
								treeMap.put(calculateItem.getKey(), calculateItem.getValue());
								shippingTemplateMap.put(calculateItem.getValue(), shippingTemplate);
							}else{
								log.info("surfaceFilterAndCalculateItem calculateItem null !shippingTemplate:"+shippingTemplate);
							}
						}
					}
					
				}
				//设置好运费
				log.info("surfaceFilterAndCalculateItem price   !treeMap:"+treeMap);//查看符合记录集
				if(MapUtils.isNotEmpty(treeMap)){
					if(isOverWeight(treeMap)){
						log.info("surfaceFilterAndCalculateItem may be over weight!");
						shippingPriceBo.getSurfaceCodeMap().put(Cons.C_SURFACE_NOT_ALLOW,getSystemParamValue(Cons.C_SURFACE_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
						return;//没有一个符合的，直接不发棕
					}
					shippingPriceBo.setSurfaceTitle(shippingPriceCalculate.getSurfaceTitle());
					shippingPriceBo.setSurfaceDescription(shippingPriceCalculate.getSurfaceDescription());
					
					Entry<Double, String> lastEntry = treeMap.lastEntry();
					shippingPriceBo.setSurfacePrice(lastEntry.getKey());
					
					if(shippingMethodMap.containsKey(lastEntry.getValue())){//是否可跟踪
						ShippingMethod shippingMethod = shippingMethodMap.get(lastEntry.getValue());
						Boolean bistracking = shippingMethod.getBistracking();
						shippingPriceBo.setSurfaceStrack((bistracking!=null && bistracking==true)?true:false);
						shippingPriceBo.setSurfaceCodeId(shippingMethod.getIid());
					}
					if(shippingTemplateMap.containsKey(lastEntry.getValue())){
						shippingPriceBo.setSurfaceOrder(shippingTemplateMap.get(lastEntry.getValue()).getTemplateOrder());
					}
					shippingPriceBo.setSurfaceCode(lastEntry.getValue());
					shippingPriceBo.setSurfaceShow(true);
					log.info("surfaceFilterAndCalculateItem price  ------> !lastEntry:"+lastEntry);

				}else{
					shippingPriceBo.getSurfaceCodeMap().put(Cons.C_SURFACE_NOT_ALLOW,getSystemParamValue(Cons.C_SURFACE_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
					log.info("surfaceFilterAndCalculateItem treeMap empty  !treeMap:"+treeMap);
				}
				
			}
			log.info("surfaceFilterAndCalculateItem price !  shippingPriceBo:"+shippingPriceBo);
		}catch(Exception e){
			shippingPriceBo.getSurfaceCodeMap().put(Cons.C_SURFACE_NOT_ALLOW,getSystemParamValue(Cons.C_SURFACE_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
			log.error("surfaceFilterAndCalculateItem setting fail ",e);//异常后，不影响其他模板计算
		}
		log.info("surfaceFilterAndCalculateItem------------>Shipping : "+(System.currentTimeMillis()-timeStart)/1000f+" 秒 <------------");
	}
	
	
	private boolean isOverWeight(TreeMap<Double, String> treeMap){
		//处理超重情况
		Set<Entry<Double, String>> entrySet = treeMap.entrySet();
		boolean isOverWeight=true;
		for(Entry<Double, String> entry : entrySet){
			if(entry.getKey()!=0){
				isOverWeight=false;
			}
		}
		return isOverWeight;
	}
	/**
	 * 计算运费
	 * @param rule
	 * @param superRule
	 * @param weight
	 * @param currency
	 * @return
	 */
	private Double getShippingPrice(HashMap<String, String> subs,String rule,String  superRule,Double weight,String  currency,Double extra){
		return freightService.getFreight(subs,rule, superRule, weight, currency,extra);
	}
	
	//一个模板，对应多个sku并归为一个SKU，计算
	private Entry<Double,String> calculateItem(Map<String,ShippingMethod> isStrack,ShippingTemplate shippingTemplate,ShippingCalculateBase shippingCalculateBase, Double weightTotal
			,  Double realWeightTotal,final String currency,VolumeWeightBo volumeWeightBo,String calculateItemType,final String country,
			final int storageId,Map<String,String> countryCodeMap,int languageId){
		try{//单次计算，不影响其他模板计算
			
			String codeArray=shippingTemplate.getPriorityShippingCode();
			if(StringUtils.isEmpty(codeArray) || StringUtils.isEmpty(calculateItemType)){
				log.info("calculateItem codeArray empty or calculateItemType is null !codeArray:"+codeArray+" calculateItemType:"+calculateItemType+" weightTotal:"+weightTotal);
				return null;
			}
			if(shippingTemplate.isWhetherFreeshipping() || weightTotal==0){//判断是否都为免邮或者重量为0
				TreeMap<Double,String> map=new TreeMap<Double,String>();
				String priorityShippingCode = shippingTemplate.getPriorityShippingCode();
				if(StringUtils.isNotEmpty(priorityShippingCode)){
					log.info("calculateItem shippingCalculateBase:"+shippingCalculateBase+"  Freeshipping:"+shippingTemplate.isWhetherFreeshipping()+"  weightTotal:"+weightTotal);
					map.put(0.0, priorityShippingCode.split(",")[0]);
					
				}else{//模板中不存在优先代码
					log.error("calculateItem shippingTemplate not contain PriorityShippingCode shippingTemplate.id:"+shippingTemplate.getId());
					throw new RuntimeException("calculateItem shippingTemplate not contain PriorityShippingCode shippingTemplate.id:"+shippingTemplate.getId());
				}
				
				return map.firstEntry();
			}else if(ShippingType.SpecialType.toString().equals(calculateItemType)){//是否为特殊模板,如果为特殊模板，且体积重大于重量，需要用体积重计算价格
				if(shippingTemplate.isCalculateWeight()){
					
					log.info("calculateItem calculateItemType volumeWeightBo:"+volumeWeightBo+"  realWeightTotal:"+realWeightTotal);
					if(volumeWeightBo.getVolumeWeight()>realWeightTotal){//如果体积重大于体重，需要用体积重计算物品运费
						log.info("calculateItem weightTotal need changed ! and use getVolumeWeight calculate");
						weightTotal=volumeWeightBo.getVolumeWeight();
						realWeightTotal=volumeWeightBo.getVolumeWeight();
					}else{
						log.info("calculateItem use weight calculate");
					}
				}
				
			}
			
			//查看计算的重量,
			log.info("calculateItem calculate final freeshipping weightTotal:"+weightTotal +"  realWeightTotal:"+realWeightTotal);
			final List<String> codeList=Lists.newArrayList(codeArray.split(","));
			final boolean isSpecialItem=shippingTemplate.isWhetherSpecial();
			//获取物流计算规则
			List<ShippingMethod> shippingMethods = newShippingMethodMapper.getShippingMethods(storageId, country, realWeightTotal, isSpecialItem,codeList);
			
			if(CollectionUtils.isEmpty(shippingMethods)){
				countryCodeMap.put(Cons.C_COUNTRY_NOT_REACH, getSystemParamValue(Cons.C_COUNTRY_NOT_REACH+"_"+languageId));
				log.info("calculateItem shippingMethods is empty");
			}else{
				
				log.info("@@@@@@------------>calculateItem calculateItemType:"+calculateItemType+"ShippingMethod.size:"+shippingMethods.size());
				long timeStart=System.currentTimeMillis();
				TreeMap<Double,String> treeMap=new TreeMap<Double,String>();
				
				
				double exchange = currencyService.exchange("USD", "CNY");
				double extraCharge=exchange*shippingTemplate.getExtraCharge();
				
				HashMap<String, String> subs = Maps.newHashMap();
				subs.put("\\$w", weightTotal.toString());
				subs.put("\\$cg", Double
						.valueOf(currencyService.exchange("GBP", "CNY")).toString());
				subs.put("\\$cu", Double
						.valueOf(currencyService.exchange("USD", "CNY")).toString());
				subs.put("\\$ce", Double
						.valueOf(currencyService.exchange("EUR", "CNY")).toString());
				subs.put("\\$ch", Double
						.valueOf(currencyService.exchange("HKD", "CNY")).toString());
				subs.put("\\$ca", Double
						.valueOf(currencyService.exchange("AUD", "CNY")).toString());
				subs.put("\\$cc", Double
						.valueOf(currencyService.exchange("CAD", "CNY")).toString());
				
				for(ShippingMethod shippingMethod : shippingMethods){
					//计算价格体重需要减去免邮的重量，
					Double shippingPrice = getShippingPrice(subs,shippingMethod.getCrule(),shippingMethod.getCsuperrule(),
							weightTotal,currency,extraCharge);
					if(shippingPrice!=null && shippingPrice>0){
						treeMap.put(shippingPrice, shippingMethod.getCcode());
						isStrack.put(shippingMethod.getCcode(), shippingMethod);
					}
				}
				log.info("calculateItem    total---------------->Shipping : "+(System.currentTimeMillis()-timeStart)/1000f+" 's <---------------------------");
				log.info("calculateItem treeMap:"+treeMap);
				
				if(FilterTypeEnum.PriceLown.toString().equals(shippingTemplate.getFilter())){
					//筛选出最高纪录
					return treeMap.firstEntry();
				}else{
					return treeMap.lastEntry();
				}
			}
		}catch(Exception e){
			log.error("calculateItem shippingTemplate error! shippingTemplate:"+shippingTemplate,e);
			
		}
		return null;
	}
	private void registFilterAndCalculateItem(List<ShippingTemplate> tempList,
			ShippingPriceBo shippingPriceBo,ShippingPriceCalculate shippingPriceCalculate){
		long timeStart=System.currentTimeMillis();
		try{
			if(shippingPriceBo.getSurfaceMapRegist()=="Y"){//如果已经设定了挂号价格，就不计算
				if(shippingPriceBo.isRegistShow()){//如果是已经设置好了挂号，设置挂号发货代码，如果不存在，则不显示
					Map<String, Object> freeCodeAndTemplate = this.getFreeCodeAndTemplate(tempList, shippingPriceCalculate.getTotalWeight(), shippingPriceCalculate.getStorageId(), shippingPriceCalculate.getCountry());
					if(CollectionUtils.isNotEmpty(tempList) && MapUtils.isNotEmpty(freeCodeAndTemplate)){

						ShippingMethod shippingMethod = (ShippingMethod) freeCodeAndTemplate.get("code");
						ShippingTemplate filerShippingTemplate = (ShippingTemplate) freeCodeAndTemplate.get("template");
						shippingPriceBo.setRegistCode(shippingMethod.getCcode());
						shippingPriceBo.setRegistCodeId(shippingMethod.getIid());
						shippingPriceBo.setRegistOrder(filerShippingTemplate.getTemplateOrder());
						
						log.info("registFilterAndCalculateItem is all freeShipping! weightTotal:"+shippingPriceCalculate.getTotalWeight());
						shippingPriceBo.setRegistTitle(shippingPriceCalculate.getRegistTitle());
						shippingPriceBo.setRegistDescription(shippingPriceCalculate.getRegistDescription());
						shippingPriceBo.setRegistPrice(0.0);//平邮免邮。重量小于
						shippingPriceBo.setRegistShow(true);
						return;
					}else{
						log.info("registFilterAndCalculateItem tempList empty! tempList:"+tempList);
						shippingPriceBo.getRegistCodeMap().put(Cons.C_REGIST_NOT_ALLOW, getSystemParamValue(Cons.C_REGIST_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
						shippingPriceBo.setRegistShow(false);//如果是平邮决定挂号显示免邮，但是挂号有没有一个模板能匹配上的，则不显示挂号
					}
					
					
				}
				log.info("registFilterAndCalculateItem already setted! shippingPriceBo:"+shippingPriceBo);
				return ;
			}
			List<ShippingCalculateBase> list = shippingPriceCalculate.getShippingCalculateBaseList();
			if(CollectionUtils.isEmpty(tempList) ){
				log.info("registFilterAndCalculateItem tempList empty!");
				return;
			}
			if(tempList.size()!=list.size()){//如果不全部是平邮，说明有些物品不能发平邮，直接终止计算
				log.info("registFilterAndCalculateItem not all regist! tempList:"+tempList+"  list:"+list);
				shippingPriceBo.setRegistShow(false);
				shippingPriceBo.getRegistCodeMap().put(Cons.C_REGIST_NOT_ALLOW,  getSystemParamValue(Cons.C_REGIST_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
				return;
			}
			Double weightTotal=0.0;//消除免邮项，如果有SKU都为免邮，那么此项重量为0
			List<ShippingTemplate> specialTempList=Lists.newArrayList();//特殊模板
			VolumeWeightBo volumeWeightBo=new VolumeWeightBo();//体积重参数;排除免邮SKU计算
			Map<String,ShippingTemplate> tempMap=Maps.newHashMap();//存储模板ID 和模板项
			String freeCode="";
			//
			for(ShippingTemplate shippingTemplate : tempList){
				if(shippingTemplate.isWhetherSpecial()){//是否为特殊模板
					specialTempList.add(shippingTemplate);
				}
				if(StringUtils.isEmpty(freeCode) && shippingTemplate.isWhetherFreeshipping()){
					String priorityShippingCode = shippingTemplate.getPriorityShippingCode();
					if(StringUtils.isNotEmpty(priorityShippingCode)){
						freeCode=priorityShippingCode.split(",")[0];//获取其中一个免邮的发货代码
					}
				}
				//用模板ID+“——”+模板类型 结构构造key
				tempMap.put(shippingTemplate.getShippingTemplateId()+"_"+shippingTemplate.getTypeName(), shippingTemplate);
			}
			//过滤体积重
			if(filterVolume(list,tempMap, shippingPriceBo,shippingPriceBo.getRegistCodeMap(),ShippingType.RegistType.toString(),volumeWeightBo,shippingPriceCalculate.getLanguage())){
			/*if(filterVolume(list,tempMap, shippingPriceBo,shippingPriceBo.getRegistCodeMap(),
					Cons.C_SUPER_STANDARD,Cons.C_OVER_WEIGHT,ShippingType.RegistType.toString(),volumeWeightBo)){*/
				log.info("registFilterAndCalculateItem ------------>shippingPriceBo:"+shippingPriceBo);
				return;
			}
			
			log.info("registFilterAndCalculateItem ------------>templateMap:"+tempMap);
			for(ShippingCalculateBase shippingCalculateBase : list){
				String mapKey=shippingCalculateBase.getTemplateId()+"_"+ShippingType.RegistType.toString();
				log.info("registFilterAndCalculateItem sku:"+shippingCalculateBase.getSku()+"------------>mapKey:"+mapKey);
				if(tempMap.containsKey(mapKey)){//处理免邮项不参加体积计算,并且模板该计算实体中，如果不存在 模板ID+“——”+类型
					ShippingTemplate shippingTemplate = tempMap.get(mapKey);
					
					//设置好是否为特殊品
					shippingCalculateBase.setSpecial(shippingTemplate.isWhetherSpecial());
					if(shippingTemplate.isWhetherFreeshipping()){
						log.debug("registFilterAndCalculateItem ShippingCalculateBase template is freeshipping");
					}else{
						weightTotal=weightTotal+shippingCalculateBase.getWeight()*shippingCalculateBase.getQty();
					}
				}else{//如果该SKU 没有对应的模板ID的平邮类型，那么平邮发不了或，终止计算
					shippingPriceBo.setRegistShow(false);
					shippingPriceBo.getRegistCodeMap().put(Cons.C_REGIST_NOT_ALLOW, getSystemParamValue(Cons.C_REGIST_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
					log.info("registFilterAndCalculateItem ShippingCalculateBase not contain templateId+'_'"+ShippingType.RegistType.toString()+"! shippingCalculateBase:"+shippingCalculateBase);
					return ;
				}
				
			}
			log.info("registFilterAndCalculateItem ---->weightTotal:"+weightTotal);
			
			//如果是全免邮
			if(weightTotal==0.0 && StringUtils.isNotEmpty(freeCode)){
				Map<String, Object> freeCodeAndTemplate = this.getFreeCodeAndTemplate(tempList, shippingPriceCalculate.getTotalWeight(), shippingPriceCalculate.getStorageId(), shippingPriceCalculate.getCountry());
				if(MapUtils.isNotEmpty(freeCodeAndTemplate)){
					ShippingMethod shippingMethod = (ShippingMethod) freeCodeAndTemplate.get("code");
					ShippingTemplate filerShippingTemplate = (ShippingTemplate) freeCodeAndTemplate.get("template");
					Boolean bistracking = shippingMethod.getBistracking();
					shippingPriceBo.setRegistStrack((bistracking!=null && bistracking==true)?true:false);
					shippingPriceBo.setRegistCode(shippingMethod.getCcode());
					shippingPriceBo.setRegistCodeId(shippingMethod.getIid());
					shippingPriceBo.setRegistOrder(filerShippingTemplate.getTemplateOrder());
				}
				log.info("registFilterAndCalculateItem is all freeShipping! weightTotal:"+weightTotal);
				shippingPriceBo.setRegistTitle(shippingPriceCalculate.getRegistTitle());
				shippingPriceBo.setRegistDescription(shippingPriceCalculate.getRegistDescription());
				shippingPriceBo.setRegistPrice(0.0);//平邮免邮。重量小于
				shippingPriceBo.setRegistShow(true);
				return;
			}
			
			TreeMap<Double,String> treeMap=new TreeMap<Double,String>();
			Map<String,ShippingMethod> shippingMethodMap=Maps.newHashMap();
			Map<String,ShippingTemplate> shippingTemplateMap=Maps.newHashMap();
			final String country=shippingPriceCalculate.getCountry();
			final int storageId=shippingPriceCalculate.getStorageId();
			final int languageId=shippingPriceCalculate.getLanguage();
			if(specialTempList.size()>0){//如果存在特殊模板
				//获取物流计算规则
				for(ShippingTemplate shippingTemplate : specialTempList){
					for(ShippingCalculateBase shippingCalculateBase : list){
						Entry<Double, String> calculateItem = calculateItem(shippingMethodMap, shippingTemplate, shippingCalculateBase,
								weightTotal,shippingPriceCalculate.getTotalWeight(),shippingPriceCalculate.getCurrency(),
								volumeWeightBo,ShippingType.RegistType.toString(),country,storageId,shippingPriceBo.getRegistCodeMap(),languageId);
						if(calculateItem!=null){
							
							treeMap.put(calculateItem.getKey(), calculateItem.getValue());
							shippingTemplateMap.put(calculateItem.getValue(), shippingTemplate);
							
						}else{
							log.info("registFilterAndCalculateItem calculateItem null !shippingTemplate:"+shippingTemplate);
						}
					}
				}
				
			}else{
				for(ShippingTemplate shippingTemplate : tempList){
					for(ShippingCalculateBase shippingCalculateBase : list){
						Entry<Double, String> calculateItem = calculateItem( shippingMethodMap,shippingTemplate, shippingCalculateBase,
								weightTotal,shippingPriceCalculate.getTotalWeight(),shippingPriceCalculate.getCurrency(),
								volumeWeightBo,ShippingType.RegistType.toString(),country,storageId,shippingPriceBo.getRegistCodeMap(),languageId);
						if(calculateItem!=null){
							treeMap.put(calculateItem.getKey(), calculateItem.getValue());
							shippingTemplateMap.put(calculateItem.getValue(), shippingTemplate);
						}else{
							log.info("registFilterAndCalculateItem calculateItem null !shippingTemplate:"+shippingTemplate);
						}
					}
				}
				
			}
			//设置好运费
			log.info("registFilterAndCalculateItem price   !treeMap:"+treeMap);//查看符合记录集
			if(MapUtils.isNotEmpty(treeMap)){
				if(isOverWeight(treeMap)){
					log.info("registFilterAndCalculateItem may be over weight!");
					shippingPriceBo.getRegistCodeMap().put(Cons.C_OVER_WEIGHT, getSystemParamValue(Cons.C_OVER_WEIGHT+"_"+shippingPriceCalculate.getLanguage()));
					return;//没有一个符合的，直接不发棕
				}
				shippingPriceBo.setRegistTitle(shippingPriceCalculate.getRegistTitle());
				shippingPriceBo.setRegistDescription(shippingPriceCalculate.getRegistDescription());
				Entry<Double, String> lastEntry = treeMap.lastEntry();
				shippingPriceBo.setRegistPrice(lastEntry.getKey());
				shippingPriceBo.setRegistCode(lastEntry.getValue());
				shippingPriceBo.setRegistShow(true);
				if(shippingMethodMap.containsKey(lastEntry.getValue())){//是否可跟踪
					ShippingMethod shippingMethod = shippingMethodMap.get(lastEntry.getValue());
					Boolean bistracking = shippingMethod.getBistracking();
					shippingPriceBo.setRegistStrack((bistracking!=null && bistracking==true)?true:false);
					shippingPriceBo.setRegistCodeId(shippingMethod.getIid());
				}
				if(shippingTemplateMap.containsKey(lastEntry.getValue())){
					shippingPriceBo.setRegistOrder(shippingTemplateMap.get(lastEntry.getValue()).getTemplateOrder());
				}
				log.info("registFilterAndCalculateItem price ---------->  !lastEntry:"+lastEntry);//查看符合记录集

			}else{
				log.info("registFilterAndCalculateItem treeMap empty  !treeMap:"+treeMap);
				
				shippingPriceBo.getRegistCodeMap().put(Cons.C_REGIST_NOT_ALLOW, getSystemParamValue(Cons.C_REGIST_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
			}
			
			log.info("registFilterAndCalculateItem price !  shippingPriceBo:"+shippingPriceBo);
			
		}catch(Exception e){
			log.error("registFilterAndCalculateItem error tempList:"+tempList,e);
			shippingPriceBo.getRegistCodeMap().put(Cons.C_REGIST_NOT_ALLOW, getSystemParamValue(Cons.C_REGIST_NOT_ALLOW+"_"+shippingPriceCalculate.getLanguage()));
		}
		log.info("registFilterAndCalculateItem------------>Shipping : "+(System.currentTimeMillis()-timeStart)/1000f+" 秒 <------------");
	}
	/**
	 * 快递费用设置
	 * @param tempList
	 * @param list
	 * @param shippingPriceBo
	 * @param currency
	 */
	private void expressFilterAndCalculateItem(List<ShippingTemplate> tempList,
			ShippingPriceBo shippingPriceBo,ShippingPriceCalculate shippingPriceCalculate,Map<String, Boolean> fileFreeWeight ){
		long timeStart=System.currentTimeMillis();
		try{
			if(CollectionUtils.isEmpty(tempList)){
				log.info("expressFilterAndCalculateItem tempList empty!");
				return;
			}
			
			Double weightTotal=0.0;//消除免邮项，如果有SKU都为免邮，那么此项重量为0
			List<ShippingTemplate> specialTempList=Lists.newArrayList();//特殊模板
			VolumeWeightBo volumeWeightBo=new VolumeWeightBo();//体积重参数;排除免邮SKU计算
			Map<String,ShippingTemplate> tempMap=Maps.newHashMap();//存储模板ID 和模板项
			String freeCode="";
			//
			for(ShippingTemplate shippingTemplate : tempList){
				if(shippingTemplate.isWhetherSpecial()){//是否为特殊模板
					specialTempList.add(shippingTemplate);
				}
				if(StringUtils.isEmpty(freeCode) && shippingTemplate.isWhetherFreeshipping()){
					String priorityShippingCode = shippingTemplate.getPriorityShippingCode();
					if(StringUtils.isNotEmpty(priorityShippingCode)){
						freeCode=priorityShippingCode.split(",")[0];//获取其中一个免邮的发货代码
					}
				}
				//用模板ID+“——”+模板类型 结构构造key
				tempMap.put(shippingTemplate.getShippingTemplateId()+"_"+shippingTemplate.getTypeName(), shippingTemplate);
			}
			
			log.info("expressFilterAndCalculateItem ------------>templateMap:"+tempMap);
			List<ShippingCalculateBase> list = shippingPriceCalculate.getShippingCalculateBaseList();
			String currency=shippingPriceCalculate.getCurrency();
			for(ShippingCalculateBase shippingCalculateBase : list){
				String mapKey=shippingCalculateBase.getTemplateId()+"_"+ShippingType.ExpressType.toString();
				log.info("expressFilterAndCalculateItem sku:"+shippingCalculateBase.getSku()+"------------>mapKey:"+mapKey);
				int qty = shippingCalculateBase.getQty();
				
				volumeWeightBo.setVolumeWeight(volumeWeightBo.getVolumeWeight()+shippingCalculateBase.getVolumeWeight()*qty);//体积重，计算全部物品重量
				if(tempMap.containsKey(mapKey)){//处理免邮项不参加体积计算,并且模板该计算实体中，如果不存在 模板ID+“——”+类型
					ShippingTemplate shippingTemplate = tempMap.get(mapKey);
					
					//设置好是否为特殊品,如果 产品本身是特殊品，模板必须要为特殊品，如果没有，则快递发货不了
					if(shippingCalculateBase.isSpecial() && !shippingTemplate.isWhetherSpecial()){
						log.info("expressFilterAndCalculateItem-------------> sku is special but tempNotSpecial");
						shippingPriceBo.setExpressShow(false);
						return;
					}
					shippingCalculateBase.setSpecial(shippingTemplate.isWhetherSpecial());
					
					
					if(!shippingTemplate.isWhetherFreeshipping()){
						weightTotal=weightTotal+shippingCalculateBase.getWeight()*qty;
					}
					
				}else{//如果该SKU 没有对应的模板ID的平邮类型，那么统一按非免邮处理
					if(fileFreeWeight.containsKey(mapKey) && fileFreeWeight.get(mapKey)){//包含模板，并且模板是免邮的，否则需要计算重量
						log.debug("expressFilterAndCalculateItem fileFreeWeight is freeshipping.  fileFreeWeight:"+fileFreeWeight);
						continue;
					}
					weightTotal=weightTotal+shippingCalculateBase.getWeight()*qty;
					log.info("expressFilterAndCalculateItem ShippingCalculateBase not contain templateId+'_'"+
							ShippingType.ExpressType.toString()+"! shippingCalculateBase:"+shippingCalculateBase);
				}
				
			}
			log.info("expressFilterAndCalculateItem ---->weightTotal:"+weightTotal);
			
			//如果是全免邮
			if(weightTotal==0.0 && StringUtils.isNotEmpty(freeCode)){
				Map<String, Object> freeCodeAndTemplate = this.getFreeCodeAndTemplate(tempList, shippingPriceCalculate.getTotalWeight(), shippingPriceCalculate.getStorageId(), shippingPriceCalculate.getCountry());
				if(MapUtils.isNotEmpty(freeCodeAndTemplate)){
					ShippingMethod shippingMethod = (ShippingMethod) freeCodeAndTemplate.get("code");
					ShippingTemplate filerShippingTemplate = (ShippingTemplate) freeCodeAndTemplate.get("template");
					Boolean bistracking = shippingMethod.getBistracking();
					shippingPriceBo.setExpressStrack((bistracking!=null && bistracking==true)?true:false);
					shippingPriceBo.setExpressCode(shippingMethod.getCcode());
					shippingPriceBo.setExpressCodeId(shippingMethod.getIid());
					shippingPriceBo.setExpressOrder(filerShippingTemplate.getTemplateOrder());
					log.info("expressFilterAndCalculateItem is all freeShipping! weightTotal:"+weightTotal);
					shippingPriceBo.setExpressTitle(shippingPriceCalculate.getExpressTitle());;
					shippingPriceBo.setExpressDescription(shippingPriceCalculate.getExpressDescription());
					shippingPriceBo.setExpressPrice(0.0);//平邮免邮。重量小于
					shippingPriceBo.setExpressShow(true);
				}else{
					shippingPriceBo.getExpressCodeMap().put(Cons.C_DISABLE_EXPRESS,  getSystemParamValue(Cons.C_DISABLE_EXPRESS+"_"+shippingPriceCalculate.getLanguage()));
				}
				return;
			}
			
			TreeMap<Double,String> treeMap=new TreeMap<Double,String>();
			Map<String,ShippingMethod> shippingMethodMap=Maps.newHashMap();
			Map<String,ShippingTemplate> shippingTemplateMap=Maps.newHashMap();
			final String country=shippingPriceCalculate.getCountry();
			final int storageId=shippingPriceCalculate.getStorageId();
			final int languageId=shippingPriceCalculate.getLanguage();
			if(specialTempList.size()>0){//如果存在特殊模板
				//获取物流计算规则
				for(ShippingTemplate shippingTemplate : specialTempList){
					for(ShippingCalculateBase shippingCalculateBase : list){
						String mapKey=shippingCalculateBase.getTemplateId()+"_"+ShippingType.ExpressType.toString();
						if(shippingTemplate.isWhetherFreeshipping() && fileFreeWeight.containsKey(mapKey) && !fileFreeWeight.get(mapKey)){
							shippingTemplate.setWhetherFreeshipping(false);
						}
						Entry<Double, String> calculateItem = calculateItem( shippingMethodMap,shippingTemplate, shippingCalculateBase,
								weightTotal,shippingPriceCalculate.getTotalWeight(),currency,
								volumeWeightBo,ShippingType.ExpressType.toString(),country,storageId,shippingPriceBo.getExpressCodeMap(),languageId);
						if(calculateItem!=null){
							shippingTemplateMap.put(calculateItem.getValue(), shippingTemplate);
							treeMap.put(calculateItem.getKey(), calculateItem.getValue());
						}else{
							log.info("expressFilterAndCalculateItem calculateItem null !shippingTemplate:"+shippingTemplate);
						}
					}
				}
				
			}else{
				for(ShippingTemplate shippingTemplate : tempList){
					for(ShippingCalculateBase shippingCalculateBase : list){
						String mapKey=shippingCalculateBase.getTemplateId()+"_"+ShippingType.ExpressType.toString();
						if(shippingTemplate.isWhetherFreeshipping() && fileFreeWeight.containsKey(mapKey) && !fileFreeWeight.get(mapKey)){
							shippingTemplate.setWhetherFreeshipping(false);
						}
						Entry<Double, String> calculateItem = calculateItem( shippingMethodMap, shippingTemplate, shippingCalculateBase,
								weightTotal,shippingPriceCalculate.getTotalWeight(),currency,
								volumeWeightBo,ShippingType.ExpressType.toString(),country,storageId,shippingPriceBo.getExpressCodeMap(),languageId);
						if(calculateItem!=null){
							shippingTemplateMap.put(calculateItem.getValue(), shippingTemplate);
							treeMap.put(calculateItem.getKey(), calculateItem.getValue());
						}else{
							log.info("expressFilterAndCalculateItem calculateItem null !shippingTemplate:"+shippingTemplate);
						}
					}
				}
				
			}
			//设置好运费
			log.info("expressFilterAndCalculateItem price   !treeMap:"+treeMap);//查看符合记录集
			if(MapUtils.isNotEmpty(treeMap)){
				if(isOverWeight(treeMap)){
					log.info("expressFilterAndCalculateItem may be over weight!");
					shippingPriceBo.getExpressCodeMap().put(Cons.C_DISABLE_EXPRESS, getSystemParamValue(Cons.C_DISABLE_EXPRESS+"_"+shippingPriceCalculate.getLanguage()));
					return;//没有一个符合的，直接不发棕
				}
				shippingPriceBo.setExpressTitle(shippingPriceCalculate.getExpressTitle());;
				shippingPriceBo.setExpressDescription(shippingPriceCalculate.getExpressDescription());
				Entry<Double, String> lastEntry = treeMap.lastEntry();
				shippingPriceBo.setExpressPrice(lastEntry.getKey());
				shippingPriceBo.setExpressCode(lastEntry.getValue());
				shippingPriceBo.setExpressShow(true);
				if(shippingMethodMap.containsKey(lastEntry.getValue())){//是否可跟踪
					ShippingMethod shippingMethod = shippingMethodMap.get(lastEntry.getValue());
					Boolean bistracking = shippingMethod.getBistracking();
					shippingPriceBo.setExpressStrack((bistracking!=null && bistracking==true)?true:false);
					shippingPriceBo.setExpressCodeId(shippingMethod.getIid());
				}
				if(shippingTemplateMap.containsKey(lastEntry.getValue())){
					shippingPriceBo.setExpressOrder(shippingTemplateMap.get(lastEntry.getValue()).getTemplateOrder());
				}
				log.info("expressFilterAndCalculateItem price  -----------> !lastEntry:"+lastEntry);
			}else{
				shippingPriceBo.getExpressCodeMap().put(Cons.C_DISABLE_EXPRESS, getSystemParamValue(Cons.C_DISABLE_EXPRESS+"_"+shippingPriceCalculate.getLanguage()));
				log.info("expressFilterAndCalculateItem treeMap empty  !treeMap:"+treeMap);
			}
			
			log.info("expressFilterAndCalculateItem price !  shippingPriceBo:"+shippingPriceBo);
			
			
			
		}catch(Exception e){
			shippingPriceBo.getExpressCodeMap().put(Cons.C_DISABLE_EXPRESS, getSystemParamValue(Cons.C_DISABLE_EXPRESS+"_"+shippingPriceCalculate.getLanguage()));
			log.error("expressFilterAndCalculateItem error tempList:"+tempList,e);
		}
		log.info("expressFilterAndCalculateItem------------>Shipping : "+(System.currentTimeMillis()-timeStart)/1000f+" 秒 <------------");
	}
	private void specialFilterAndCalculateItem(List<ShippingTemplate> tempList,
			ShippingPriceBo shippingPriceBo,ShippingPriceCalculate shippingPriceCalculate,Map<String, Boolean> fileFreeWeight ){
		long timeStart=System.currentTimeMillis();
		try{
			if(CollectionUtils.isEmpty(tempList)){
				log.info("specialFilterAndCalculateItem tempList empty!");
				return;
			}
			
			Double weightTotal=0.0;//消除免邮项，如果有SKU都为免邮，那么此项重量为0
			List<ShippingTemplate> specialTempList=Lists.newArrayList();//特殊模板
			VolumeWeightBo volumeWeightBo=new VolumeWeightBo();//体积重参数;排除免邮SKU计算
			Map<String,ShippingTemplate> tempMap=Maps.newHashMap();//存储模板ID 和模板项
			String freeCode="";
			//
			for(ShippingTemplate shippingTemplate : tempList){
				if(shippingTemplate.isWhetherSpecial()){//是否为特殊模板
					specialTempList.add(shippingTemplate);
				}
				if(StringUtils.isEmpty(freeCode) && shippingTemplate.isWhetherFreeshipping()){
					String priorityShippingCode = shippingTemplate.getPriorityShippingCode();
					if(StringUtils.isNotEmpty(priorityShippingCode)){
						freeCode=priorityShippingCode.split(",")[0];//获取其中一个免邮的发货代码
					}
				}
				//用模板ID+“——”+模板类型 结构构造key
				tempMap.put(shippingTemplate.getShippingTemplateId()+"_"+shippingTemplate.getTypeName(), shippingTemplate);
			}
			log.info("specialFilterAndCalculateItem ------------>templateMap:"+tempMap);
			List<ShippingCalculateBase> list = shippingPriceCalculate.getShippingCalculateBaseList();
			String currency = shippingPriceCalculate.getCurrency();
			for(ShippingCalculateBase shippingCalculateBase : list){
				String mapKey=shippingCalculateBase.getTemplateId()+"_"+ShippingType.SpecialType.toString();
				log.info("specialFilterAndCalculateItem sku:"+shippingCalculateBase.getSku()+"------------>mapKey:"+mapKey);
				int qty = shippingCalculateBase.getQty();
				
				volumeWeightBo.setVolumeWeight(volumeWeightBo.getVolumeWeight()+shippingCalculateBase.getVolumeWeight()*qty);
				if(tempMap.containsKey(mapKey)){//处理免邮项不参加体积计算,并且模板该计算实体中，如果不存在 模板ID+“——”+类型
					ShippingTemplate shippingTemplate = tempMap.get(mapKey);
					
					//设置好是否为特殊品,如果 产品本身是特殊品，模板必须要为特殊品，如果没有，则特快发货不了
					if(shippingCalculateBase.isSpecial() && !shippingTemplate.isWhetherSpecial()){
						log.info("expressFilterAndCalculateItem-------------> sku is special but tempNotSpecial");
						shippingPriceBo.setSpecialShow(false);
						shippingPriceBo.getSpecialCodeMap().put(Cons.C_DISABLE_SPECIAL,  getSystemParamValue(Cons.C_DISABLE_SPECIAL+"_"+shippingPriceCalculate.getLanguage()));
						return;
					}
					//设置好是否为特殊品
					shippingCalculateBase.setSpecial(shippingTemplate.isWhetherSpecial());
					if(shippingTemplate.isWhetherFreeshipping()){//过滤掉免邮商品，不参与体积重计算
						log.debug("specialFilterAndCalculateItem ShippingCalculateBase template is freeshipping");
					}else{
						weightTotal=weightTotal+shippingCalculateBase.getWeight()*qty;
						
					}
					
				}else{//如果该SKU 没有对应的模板ID的特快类型，打印出来
					if(fileFreeWeight.containsKey(mapKey) && fileFreeWeight.get(mapKey)){
						log.debug("specialFilterAndCalculateItem fileFreeWeight is freeshipping.  fileFreeWeight:"+fileFreeWeight);
						continue;
					}
					//如果该商品没有对应的模板配置，则需要计算体积重参数，就不当做免邮处理
					weightTotal=weightTotal+shippingCalculateBase.getWeight()*qty;
					log.info("specialFilterAndCalculateItem ShippingCalculateBase not contain templateId+'_'"+ShippingType.SpecialType.toString()+"! shippingCalculateBase:"+shippingCalculateBase);
				}
				
			}
			log.info("specialFilterAndCalculateItem ---->weightTotal:"+weightTotal);
			

			//如果是全免邮
			if(weightTotal==0.0 && StringUtils.isNotEmpty(freeCode)){
				Map<String, Object> freeCodeAndTemplate = this.getFreeCodeAndTemplate(tempList, shippingPriceCalculate.getTotalWeight(), shippingPriceCalculate.getStorageId(), shippingPriceCalculate.getCountry());
				if(MapUtils.isNotEmpty(freeCodeAndTemplate)){
					ShippingMethod shippingMethod = (ShippingMethod) freeCodeAndTemplate.get("code");
					ShippingTemplate filerShippingTemplate = (ShippingTemplate) freeCodeAndTemplate.get("template");
					Boolean bistracking = shippingMethod.getBistracking();
					shippingPriceBo.setSpecialStrack((bistracking!=null && bistracking==true)?true:false);
					shippingPriceBo.setSpecialCode(shippingMethod.getCcode());
					shippingPriceBo.setSpecialCodeId(shippingMethod.getIid());
					shippingPriceBo.setSpecialOrder(filerShippingTemplate.getTemplateOrder());
					shippingPriceBo.setSpecialTitle(shippingPriceCalculate.getSpecialTitle());
					shippingPriceBo.setSpecialDescription(shippingPriceCalculate.getSpecialDescription());
					log.info("specialFilterAndCalculateItem is all freeShipping! weightTotal:"+weightTotal);
					shippingPriceBo.setSpecialPrice(0.0);
					shippingPriceBo.setSpecialShow(true);
				}else{
					shippingPriceBo.getSpecialCodeMap().put(Cons.C_DISABLE_SPECIAL,  getSystemParamValue(Cons.C_DISABLE_SPECIAL+"_"+shippingPriceCalculate.getLanguage()));
				}
				return;
			}
			
			TreeMap<Double,String> treeMap=new TreeMap<Double,String>();
			Map<String,ShippingMethod> shippingMethodMap=Maps.newHashMap();
			Map<String,ShippingTemplate> shippingTemplateMap=Maps.newHashMap();
			final String country=shippingPriceCalculate.getCountry();
			final int storageId=shippingPriceCalculate.getStorageId();
			final int languageId=shippingPriceCalculate.getLanguage();
			if(specialTempList.size()>0){//如果存在特殊模板
				//获取物流计算规则
				for(ShippingTemplate shippingTemplate : specialTempList){
					
					for(ShippingCalculateBase shippingCalculateBase : list){
						String mapKey=shippingCalculateBase.getTemplateId()+"_"+ShippingType.SpecialType.toString();
						if(shippingTemplate.isWhetherFreeshipping() && fileFreeWeight.containsKey(mapKey) && !fileFreeWeight.get(mapKey)){
							shippingTemplate.setWhetherFreeshipping(false);
						}
						Entry<Double, String> calculateItem = calculateItem(shippingMethodMap, shippingTemplate, shippingCalculateBase,
								weightTotal,shippingPriceCalculate.getTotalWeight(),currency,
								volumeWeightBo,ShippingType.SpecialType.toString(),country,storageId,shippingPriceBo.getSpecialCodeMap(),languageId);
						if(calculateItem!=null){
							shippingTemplateMap.put(calculateItem.getValue(), shippingTemplate);
							treeMap.put(calculateItem.getKey(), calculateItem.getValue());
						}else{
							log.info("specialFilterAndCalculateItem calculateItem null !shippingTemplate:"+shippingTemplate);
						}
					}
				}
				
			}else{
				for(ShippingTemplate shippingTemplate : tempList){
					for(ShippingCalculateBase shippingCalculateBase : list){
						String mapKey=shippingCalculateBase.getTemplateId()+"_"+ShippingType.SpecialType.toString();
						if(shippingTemplate.isWhetherFreeshipping() && fileFreeWeight.containsKey(mapKey) && !fileFreeWeight.get(mapKey)){
							shippingTemplate.setWhetherFreeshipping(false);
						}
						Entry<Double, String> calculateItem = calculateItem(shippingMethodMap, shippingTemplate, shippingCalculateBase,
								weightTotal,shippingPriceCalculate.getTotalWeight(),currency,
								volumeWeightBo,ShippingType.SpecialType.toString(),country,storageId,shippingPriceBo.getSpecialCodeMap(),languageId);
						if(calculateItem!=null){
							shippingTemplateMap.put(calculateItem.getValue(), shippingTemplate);
							treeMap.put(calculateItem.getKey(), calculateItem.getValue());
						}else{
							log.info("specialFilterAndCalculateItem calculateItem null !shippingTemplate:"+shippingTemplate);
						}
					}
				}
				
			}
			//设置好运费
			log.info("specialFilterAndCalculateItem price   !treeMap:"+treeMap);//查看符合记录集
			if(MapUtils.isNotEmpty(treeMap)){
				if(isOverWeight(treeMap)){
					log.info("specialFilterAndCalculateItem may be over weight!");
					shippingPriceBo.getSpecialCodeMap().put(Cons.C_DISABLE_SPECIAL,  getSystemParamValue(Cons.C_DISABLE_SPECIAL+"_"+shippingPriceCalculate.getLanguage()));
					return;//没有一个符合的，直接不发棕
				}
				shippingPriceBo.setSpecialTitle(shippingPriceCalculate.getSpecialTitle());
				shippingPriceBo.setSpecialDescription(shippingPriceCalculate.getSpecialDescription());
				Entry<Double, String> lastEntry = treeMap.lastEntry();
				shippingPriceBo.setSpecialPrice(lastEntry.getKey());
				shippingPriceBo.setSpecialCode(lastEntry.getValue());
				shippingPriceBo.setSpecialShow(true);
				if(shippingMethodMap.containsKey(lastEntry.getValue())){//是否可跟踪
					ShippingMethod shippingMethod = shippingMethodMap.get(lastEntry.getValue());
					Boolean bistracking = shippingMethod.getBistracking();
					shippingPriceBo.setSpecialStrack((bistracking!=null && bistracking==true)?true:false);
					shippingPriceBo.setSpecialCodeId(shippingMethod.getIid());
				}
				if(shippingTemplateMap.containsKey(lastEntry.getValue())){
					shippingPriceBo.setSpecialOrder(shippingTemplateMap.get(lastEntry.getValue()).getTemplateOrder());
				}
				log.info("specialFilterAndCalculateItem price  -------------> !lastEntry:"+lastEntry);//查看符合记录集

			}else{
				shippingPriceBo.getSpecialCodeMap().put(Cons.C_DISABLE_SPECIAL,  getSystemParamValue(Cons.C_DISABLE_SPECIAL+"_"+shippingPriceCalculate.getLanguage()));
				log.info("specialFilterAndCalculateItem treeMap empty  !treeMap:"+treeMap);
			}
			
			log.info("specialFilterAndCalculateItem price !  shippingPriceBo:"+shippingPriceBo);
			
			
		}catch(Exception e){
			shippingPriceBo.getSpecialCodeMap().put(Cons.C_DISABLE_SPECIAL,  getSystemParamValue(Cons.C_DISABLE_SPECIAL+"_"+shippingPriceCalculate.getLanguage()));
			log.error("specialFilterAndCalculateItem error tempList:"+tempList,e);
		}
		log.info("specialFilterAndCalculateItem------------>Shipping : "+(System.currentTimeMillis()-timeStart)/1000f+" 秒 <------------");
	}
	
	private void groupTemplate(List<ShippingTemplate> p,List<ShippingTemplate> g,
			List<ShippingTemplate> k,List<ShippingTemplate> t,List<ShippingTemplate> list){
		log.info("groupTemplate list.size:"+list.size());
		for(ShippingTemplate shippingTemplate : list){
			if(ShippingType.SurfaceType.toString().equals(shippingTemplate.getTypeName())){
				p.add(shippingTemplate);
			}else if(ShippingType.RegistType.toString().equals(shippingTemplate.getTypeName())){
				g.add(shippingTemplate);
			}else if(ShippingType.ExpressType.toString().equals(shippingTemplate.getTypeName())){
				k.add(shippingTemplate);
			}else if(ShippingType.SpecialType.toString().equals(shippingTemplate.getTypeName())){
				t.add(shippingTemplate);
			}
		}
	}
	
	/**
	 * 获取模板列表
	 * @param list
	 * @return
	 */
	private List<ShippingTemplate>  getTemplate(double exchange ,ShippingPriceCalculate shippingPriceCalculate,ShippingPriceBo shippingPriceBo){
		
		
		List<ShippingTemplate> templateInfoList=Lists.newArrayList();
		int language=shippingPriceCalculate.getLanguage();
		int storageId = shippingPriceCalculate.getStorageId();
		for(ShippingCalculateBase shippingCalculateBase : shippingPriceCalculate.getShippingCalculateBaseList()){//没一个SKU对应模板ID，获取模板配置项
			
			
			//判断是不是每种类型至少有一个
			List<ShippingTemplate> templateConfigInfo = getTemplateListByTemplateId(shippingCalculateBase.getTemplateId(),storageId);
			
			if(CollectionUtils.isNotEmpty(templateConfigInfo)){//有可能由于参数设置错误，导致一个SKU可以匹配上多个配置项情况，这种情况应该是不允许的
				for(ShippingTemplate shippingTemplate :templateConfigInfo){
					int filterId = shippingTemplate.getFilterId();
					int shippingTypeId = shippingTemplate.getShippingTypeId();
					
					/****************************************  带缓存优化项   ****************************************/
					setShippingPriceCalculateTitleDescription(exchange, shippingTemplate,
							 shippingPriceCalculate, filterId, language, shippingTypeId, templateInfoList,shippingPriceBo);
					
				}
			}else{//只要出现一个不匹配，直接返回,无法发货
				log.info("getTemplate shippingCalculateBase no template ! shippingCalculateBase:"+shippingCalculateBase);
				return templateInfoList;
			}
		}
		return templateInfoList;
		
	}
	private boolean setShippingPriceCalculateTitleDescription(double exchange,ShippingTemplate shippingTemplate,
			ShippingPriceCalculate shippingPriceCalculate,int filterId,int language,int shippingTypeId,List<ShippingTemplate> templateInfoList
			,ShippingPriceBo shippingPriceBo){
		ShippingTitleDescribeParamsVo shippingTitleDescribe = getShippingTitleDescribe(language, shippingTypeId);
		
		CacheParamsVo filterName = getFilterName(filterId);
		ShippingTypePo ty = getTemplateTypeName(shippingTypeId);
		if(filterName!=null || ty!=null){
			log.info("getTemplate filterName:"+filterName+"   ty:"+ty);
			
			String title=shippingTitleDescribe.getTitle();
			String description=shippingTitleDescribe.getDescription();
			
			double priceLimit=exchange*shippingPriceCalculate.getTotalPrice();
			if(FilterTypeEnum.PriceHigh.toString().equals(filterName.getCvalue())
					||FilterTypeEnum.PriceLown.toString().equals(filterName.getCvalue())){
				shippingTemplate.setFilter(filterName.getCvalue());
			}else{
				log.info("getTemplate Filter type nuknow! filterName:"+filterName.getCvalue());
				return false;
			}
			
			double weightParams=shippingPriceCalculate.getTotalWeight();
			int languageId=shippingPriceCalculate.getLanguage();
			String country=shippingPriceCalculate.getCountry();
			//类型映射到模板序列
			shippingTemplate.setTemplateOrder(ty.getTypeOrder());
			//体积重参数过滤
			if(shippingTemplate.isCalculateWeight()){
				double tempWeight=0.0;
				for(ShippingCalculateBase s : shippingPriceCalculate.getShippingCalculateBaseList()){
					tempWeight=tempWeight+s.getVolumeWeight()*s.getQty();
				}
				if(tempWeight>weightParams){//模板允许计算体积重，并且体积重大于重量
					weightParams=tempWeight;
				}
			}
			
			if(ShippingType.SurfaceType.toString().equals(ty.getTypeName())){

				if(StringUtils.isEmpty(shippingPriceCalculate.getSurfaceTitle() ) && shippingTitleDescribe!=null){
					shippingPriceCalculate.setSurfaceTitle(title);
					shippingPriceCalculate.setSurfaceDescription(description);
					shippingPriceBo.setSurfaceTitle(title);
					shippingPriceBo.setSurfaceDescription(description);
					shippingPriceBo.setSurfaceOrder(ty.getTypeOrder());
				}
				filterContry(country,ty.getTypeName(),shippingTemplate,weightParams,priceLimit,templateInfoList,languageId,shippingPriceBo.getSurfaceCodeMap());
			
			}else if(ShippingType.RegistType.toString().equals(ty.getTypeName())){
				if(StringUtils.isEmpty(shippingPriceCalculate.getRegistTitle()) && shippingTitleDescribe!=null ){
					shippingPriceCalculate.setRegistTitle(title);
					shippingPriceCalculate.setRegistDescription(description);
					shippingPriceBo.setRegistTitle(title);
					shippingPriceBo.setRegistDescription(description);
					shippingPriceBo.setRegistOrder(ty.getTypeOrder());
				}
				filterContry(country,ty.getTypeName(),shippingTemplate,weightParams,priceLimit,templateInfoList,languageId,shippingPriceBo.getRegistCodeMap());
			
			}else if(ShippingType.ExpressType.toString().equals(ty.getTypeName())){
				if( StringUtils.isEmpty(shippingPriceCalculate.getExpressTitle()) && shippingTitleDescribe!=null  ){
					shippingPriceCalculate.setExpressTitle(title);
					shippingPriceCalculate.setExpressDescription(description);
					shippingPriceBo.setExpressTitle(title);
					shippingPriceBo.setExpressDescription(description);
					shippingPriceBo.setExpressOrder(ty.getTypeOrder());
				}
				filterContry(country,ty.getTypeName(),shippingTemplate,weightParams,priceLimit,templateInfoList,languageId,shippingPriceBo.getExpressCodeMap());
			
			}else if(ShippingType.SpecialType.toString().equals(ty.getTypeName())){
				if(StringUtils.isEmpty(shippingPriceCalculate.getSpecialTitle()) && shippingTitleDescribe!=null  ){
					shippingPriceCalculate.setSpecialTitle(title);
					shippingPriceCalculate.setSpecialDescription(description);
					shippingPriceBo.setSpecialTitle(title);
					shippingPriceBo.setSpecialDescription(description);
					shippingPriceBo.setSpecialOrder(ty.getTypeOrder());
				}
				filterContry(country,ty.getTypeName(),shippingTemplate,weightParams,priceLimit,templateInfoList,languageId,shippingPriceBo.getSpecialCodeMap());
			
			}else{
				log.info("getTemplate template TypeName  nuknow! typeName:"+ty.getTypeName());
				return false;
			}
		}else{
			log.info("getTemplate CacheParamsBo exist null ! filterId:"+filterId+"  shippingTypeId:"+shippingTypeId);
		}
		return true;
	}
	//不能再数据库过滤，需要在代码层级处理价格和重量 
	
	private void filterContry(String country,String templateTypeName,ShippingTemplate s ,double weight,double price,List<ShippingTemplate> templateInfoList,int languageId,Map<String, String> map){
		s.setTypeName(templateTypeName);
		if(s.getStartAmount()>=0 && s.getAmountLimit()>0){
			if(s.getStartAmount()>price || s.getAmountLimit()<price){
				map.put(Cons.C_ORDER_PRICE_OVER,  getSystemParamValue(Cons.C_ORDER_PRICE_OVER+"_"+languageId));
				log.info("filterContry---------->Amount  not allow !delete record template.id:"+s.getShippingTemplateId());
				return;
			}
		}
		if(s.getStartWeight()>=0 && s.getWeightLimit()>0){
			if(s.getStartWeight()*1000>weight || s.getWeightLimit()*1000<weight){
				map.put(Cons.C_OVER_WEIGHT,  getSystemParamValue(Cons.C_OVER_WEIGHT+"_"+languageId));
				log.info("filterContry---------->weight not allow !delete record template.id:"+s.getShippingTemplateId());
				return;
			}
		}
		if(SHIPPING_COUNTRY_SWITCH.equals(s.getCountrySwitch())){
			templateInfoList.add(s);
			return ;
		}else if( StringUtils.isEmpty(s.getCountry())){
			log.info("filterContry---------->conuntry empty!delete record template.id:"+s.getShippingTemplateId());
			return;
		}
		
		String[] split=s.getCountry().split(",");
		String countrySwitch = s.getCountrySwitch();
		if(CountryTypeEnum.Include.toString().equals(countrySwitch)){
			boolean isExclude=false;
			for(String item : split){
				if((StringUtils.isNotEmpty(item) && item.equals(country))){
					//templateInfoList.add(s);
					isExclude=true;
					break;
				}
			}
			if(isExclude){
				templateInfoList.add(s);
			}else{
				map.put(Cons.C_COUNTRY_NOT_REACH,  getSystemParamValue(Cons.C_COUNTRY_NOT_REACH+"_"+languageId));
			}
		}else if(CountryTypeEnum.Exclude.toString().equals(countrySwitch)){
			boolean isExclude=true;
			for(String item : split){
				if((StringUtils.isNotEmpty(item) && item.equals(country))){
					isExclude=false;
					break;
				}
			}
			if(isExclude){
				templateInfoList.add(s);
			}else{
				map.put(Cons.C_COUNTRY_NOT_REACH,  getSystemParamValue(Cons.C_COUNTRY_NOT_REACH+"_"+languageId));
			}
		}
	}
	
	private Map<String,Object> getFreeCodeAndTemplate(List<ShippingTemplate> tempList,final double weight,final int storageId,final String country ){
		if(CollectionUtils.isEmpty(tempList)){
			return null;
		}
		ShippingMethod shippingMethod=null;
		ShippingTemplate filerShippingTemplate=null;
		for(ShippingTemplate shippingTemplate: tempList){
			String priorityShippingCode = shippingTemplate.getPriorityShippingCode();
			if(StringUtils.isEmpty(priorityShippingCode)){
				continue;
			}
			List<String> codeList=Lists.newArrayList(priorityShippingCode.split(","));
			//获取物流计算规则
			List<ShippingMethod> shippingMethods = newShippingMethodMapper.getShippingMethods(storageId, country, weight, shippingTemplate.isWhetherSpecial(),codeList);
			if(CollectionUtils.isNotEmpty(shippingMethods)){
				shippingMethod=shippingMethods.get(0);
				filerShippingTemplate=shippingTemplate;
				break;
			}
		}
		if(shippingMethod!=null && filerShippingTemplate!=null){
			Map<String,Object> map=Maps.newHashMap();
			map.put("template", filerShippingTemplate);
			map.put("code", shippingMethod);
			return map;
		}else{
			return null;
		}
	}
	
	
	/********************************************** 运费计算模块  **************************************************/
	@Override
	public List<ShippingPriceCalculateDto> shippingCalculateService(ShippingPriceParamsDto shippingPriceParamsDto){
		
		List<ShippingPriceCalculateDto> list=Lists.newArrayList();
		TreeMultimap<Double,ShippingPriceCalculateDto> map = TreeMultimap.create();
		String allShippingCode = shippingTemplateMapper.getAllShippingCodeTypeById(shippingPriceParamsDto.getShippingTypeId());
		if(StringUtils.isNotEmpty(allShippingCode)){
			String[] split = allShippingCode.split(",");
			List<String> shippingMethodCodeList=Lists.newArrayList(split);
			List<ShippingMethod> shippingMethods = newShippingMethodMapper.getShippingMethods(shippingPriceParamsDto.getStorageId(), shippingPriceParamsDto.getCountry(), shippingPriceParamsDto.getWeight(), false,shippingMethodCodeList);
			if(CollectionUtils.isNotEmpty(shippingMethods)){
				
				HashMap<String, String> subs = Maps.newHashMap();
				subs.put("\\$w",  shippingPriceParamsDto.getWeight()+"");
				subs.put("\\$cg", Double
						.valueOf(currencyService.exchange("GBP", "CNY")).toString());
				subs.put("\\$cu", Double
						.valueOf(currencyService.exchange("USD", "CNY")).toString());
				subs.put("\\$ce", Double
						.valueOf(currencyService.exchange("EUR", "CNY")).toString());
				subs.put("\\$ch", Double
						.valueOf(currencyService.exchange("HKD", "CNY")).toString());
				subs.put("\\$ca", Double
						.valueOf(currencyService.exchange("AUD", "CNY")).toString());
				subs.put("\\$cc", Double
						.valueOf(currencyService.exchange("CAD", "CNY")).toString());
				
				
				for(ShippingMethod item : shippingMethods){
					
					Double freight = freightService.getFreight(subs,item.getCrule(), item.getCsuperrule(), shippingPriceParamsDto.getWeight(), USD_CURRENCY,null);
					if(freight!=null && freight>0){//价格为0的不统计
						ShippingPriceCalculateDto shippingPriceCalculateDto=new ShippingPriceCalculateDto();
						shippingPriceCalculateDto.setCountry(shippingPriceParamsDto.getCountry());
						shippingPriceCalculateDto.setPrice(freight);
						shippingPriceCalculateDto.setShippingCode(item.getCcode());
						shippingPriceCalculateDto.setShippingTypeId(shippingPriceParamsDto.getShippingTypeId());
						shippingPriceCalculateDto.setStorageNameId(shippingPriceParamsDto.getStorageId());
						map.put(freight, shippingPriceCalculateDto);
					}
				}
				Iterator<Entry<Double, ShippingPriceCalculateDto>> iter = map.entries().iterator();
				while(iter.hasNext())
				{
				        Map.Entry<Double, ShippingPriceCalculateDto> entry = iter.next();
				        list.add(entry.getValue());
				}
			}else{
				log.info("shippingCalculateService shippingMethods empty!");
			}
		}else{
			log.info("shippingCalculateService allShippingCode empty!");
		}
		
		log.info("shippingCalculateService list.size:"+list.size());
		return list;
	}
	
	/*************************************************** 配置参数缓存  *****************************************************************/
	
	private ShippingTitleDescribeParamsVo getShippingTitleDescribe(Integer language,Integer tmepLateTypeId){
		return cacheManagerService.getShippingTitleDescribe(language, tmepLateTypeId);
	}
	/**
	 * 过滤结果集，配置项缓存
	 * @param filterId
	 * @return
	 */
	private CacheParamsVo getFilterName(Integer filterId){
		return cacheManagerService.getFilterName(filterId);
	}
	/**
	 * 模板类型缓存项
	 * @param shippingTypeId
	 * @return
	 */
	private ShippingTypePo getTemplateTypeName(Integer shippingTypeId){
		return cacheManagerService.getTemplateTypeName(shippingTypeId);
		
		
	}
	/**
	 * 模板数据配置项缓存
	 */
	private List<ShippingTemplate> getTemplateListByTemplateId(Integer templateId,Integer storageId){
		return cacheManagerService.getTemplateListByTemplateId(templateId, storageId);
	}
	@Override
	public ShippingDisplayNamePo shippingCodeName(String code,Integer languageId){
		List<ShippingDisplayNamePo> allshippingCodeName = cacheManagerService.getAllshippingCodeName();
		List<ShippingTypePo> allTemplateType = cacheManagerService.getAllTemplateType();
		if(CollectionUtils.isNotEmpty(allshippingCodeName) && CollectionUtils.isNotEmpty(allTemplateType)){
			Map<String,ShippingDisplayNamePo> shippingCodeNameMap= Maps.uniqueIndex(allshippingCodeName.iterator(),new Function<ShippingDisplayNamePo,String>(){  
			    @Override  
			    public String apply(ShippingDisplayNamePo input) {  
			        return input.getShippingTypeId()+"_"+input.getLanguageId();
			    }  
			}); 
			ShippingTypePo find=null;
			for(ShippingTypePo  shippingTypePo: allTemplateType){
				String shippingCode = shippingTypePo.getShippingCode();
				if(StringUtils.isEmpty(shippingCode)){
					continue;
				}
				boolean isFind=false;
				String[] split = shippingCode.split(",");
				for(String item : split){
					if(code.equals(item)){
						isFind=true;
						break;
					}
				}
				if(isFind){
					find=shippingTypePo;
				}
				
			}
			if(find!=null){
				languageId=languageId==null?1:languageId;
				String key=find.getId()+"_"+languageId;
				return shippingCodeNameMap.containsKey(key)?shippingCodeNameMap.get(key):null;
			}
		}
		return null;
	}
}
