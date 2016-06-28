package com.tomtop.controller;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.common.enums.ShippingType;
import com.tomtop.entry.bo.ShippingCalculateLessParam;
import com.tomtop.entry.bo.ShippingPriceBo;
import com.tomtop.entry.form.ShippingPriceCalculateDto;
import com.tomtop.entry.form.ShippingPriceParamsDto;
import com.tomtop.entry.form.ShippingPriceResultItem;
import com.tomtop.entry.po.ShippingDisplayNamePo;
import com.tomtop.entry.po.ShippingPriceCalculate;
import com.tomtop.service.IProductBaseInfoService;
import com.tomtop.service.IShippingManageService;
import com.tomtop.service.impl.ShippingParamsValidate;

/**
 * spring boot mvc demo
 */
@RestController
public class ShippingController {

	private static final Logger log = Logger.getLogger(ShippingController.class
			.getName());

	@Value("${shipping_token}")
	private String token;

	@Autowired
	IShippingManageService shippingManageService;
	@Autowired
	ShippingParamsValidate shippingParamsValidate;
	@Autowired
	IProductBaseInfoService productBaseInfoService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/report/base/param")
	@ResponseBody
	public String getReportDetail() {
		return "report item details !listingId:" ;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/report/base/{listingId}")
	@ResponseBody
	public String getReportDetail(@PathVariable("listingId") String listingId) {
		return "report item details !listingId:" + listingId;
	}
	
	@RequestMapping( method=RequestMethod.POST,value = "/shipping")
	@ResponseBody
	public String getShippingPriceByListingIds(HttpServletRequest request,@RequestBody String jsonParam) {
		long timeStart=System.currentTimeMillis();
		//参数校验
		String header = request.getHeader("token");
		if(!validateToeknValue(header)){//校验权限
			Map<String,Object> map=Maps.newHashMap();
			log.info("getShippingPriceByListingIds params fail, token is unavailable token:"+token);
			map.put("status", "N");
			map.put("msg", "403");
			return JSONObject.toJSONString(map);
		}
		ObjectMapper om=new ObjectMapper();
		Map<String,Object> result=Maps.newHashMap();
		JsonNode node;
		try {
			node = om.readTree(jsonParam);
			result=getShippingPriceItem(om,node);
		} catch (Exception e) {
			log.error("getShippingPriceByListingIds error   jsonParam:"+jsonParam+"----------",e);
			result.put("status", "E");
			result.put("msg", e.getMessage());
		}
		log.info("total---------------->Shipping : "+(System.currentTimeMillis()-timeStart)/1000f+" 秒 <---------------------------");
		return JSONObject.toJSONString(result);
	}
	@RequestMapping( method=RequestMethod.POST,value = "/shipping/multi")
	@ResponseBody
	public String getShippingPriceMulti(HttpServletRequest request,@RequestBody String jsonParam) {
		long timeStart=System.currentTimeMillis();
		//参数校验
		String header = request.getHeader("token");
		Map<String,Object> map=Maps.newHashMap();
		if(!validateToeknValue(header)){//校验权限
			log.info("getShippingPriceMulti params fail, token is unavailable token:"+token);
			map.put("status", "N");
			map.put("msg", "403");
			return JSONObject.toJSONString(map);
		}
		List<Map<String,Object>> list=Lists.newArrayList();
		ObjectMapper om=new ObjectMapper();
		JsonNode node;
		try {
			node = om.readTree(jsonParam);
			if(node.isArray()){
				Iterator<JsonNode> iterator = node.iterator();
				while(iterator.hasNext()){
					Map<String,Object> result=Maps.newHashMap();
					JsonNode next = iterator.next();
					String asText = next.get("id").asText();
					JsonNode jsonNode = next.get("shipingItem");
					if(StringUtils.isNotEmpty(asText)){
						result.put("id", asText);
						result.put("shipingItem", getShippingPriceItem(om,jsonNode));
						list.add(result);
					}
				}
			}else{
				map=getShippingPriceItem(om,node);
			}
		} catch (Exception e) {
			log.error("getShippingPriceMulti error   jsonParam:"+jsonParam+"----------",e);
			map.put("status", "E");
			map.put("msg", e.getMessage());
		}
		log.info("total---------------->Shipping : "+(System.currentTimeMillis()-timeStart)/1000f+" 秒 <---------------------------");
		return JSONObject.toJSONString(list);
	}
	private Map<String,Object> getShippingPriceItem(ObjectMapper om,JsonNode node ){
		Map<String,Object> map=Maps.newHashMap();
		try{
				
					/*JsonNode node = om.readTree(jsonParam);*/
					if (node == null) {//校验转换参数
						map.put("status", "N");
						map.put("msg", "conver to object null");
						log.info("getShippingPriceItem node null");
						return map;
					}
					ShippingCalculateLessParam scb = om.convertValue(
								node,
								ShippingCalculateLessParam.class);
					
					ShippingPriceCalculate shippingCalculateBase = productBaseInfoService.getShippingCalculateBase(scb);
					
					if(shippingCalculateBase!=null){
						getResult(shippingCalculateBase,map);
					}else{
						log.info("getShippingPriceItem shippingCalculateBase null!");
						map.put("status", "N");
						map.put("msg", "shippingCalculateBase null");
					}
					
				}catch(Exception e){
					log.error("getShippingPriceItem error   jsonParam:"+node+"----------",e);
					map.put("status", "E");
					map.put("msg", e.getMessage());
				}
		return map;
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/shipping/price/getMutil")
	@ResponseBody
	public String getShippingPrice(HttpServletRequest request,@RequestBody String jsonParam) {
		Map<String,Object> map=Maps.newHashMap();
		//参数校验
		String header = request.getHeader("token");
		if(!validateToeknValue(header)){//校验权限
			log.info("getShippingPrice params fail, token is unavailable token:"+token);
			map.put("status", "N");
			map.put("msg", "403");
			
			return JSONObject.toJSONString(map);
		}
		try{
			
			ObjectMapper om=new ObjectMapper();
			JsonNode node = om.readTree(jsonParam);
			if (node == null) {//校验转换参数
				map.put("status", "N");
				map.put("msg", "conver to object null");
				log.info("getShippingPrice node null");
				return JSONObject.toJSONString(map);
			}
			/*List<ShippingCalculateBase> shippingCalculateBaseList = Lists.newArrayList();
			if (node.isArray()) {
				Iterator<JsonNode> it = node.iterator();
				while(it.hasNext()){
					ShippingPriceCalculate scb = om.convertValue(
							it.next(),
							ShippingPriceCalculate.class);
					shippingCalculateBaseList.add(scb);
				}
			} else {*/
			ShippingPriceCalculate scb = om.convertValue(
						node,
						ShippingPriceCalculate.class);
			//}		
			getResult(scb,map);
			
		}catch(Exception e){
			log.error("getShippingPrice error   jsonParam:"+jsonParam+"----------",e);
			map.put("status", "E");
			map.put("msg", e.getMessage());
		}
		return JSONObject.toJSONString(map);
	}
	
	private void getResult(ShippingPriceCalculate scb,Map<String,Object> map){
		
		shippingParamsValidate.validateParams(scb,map);//校验参数
		
		if(MapUtils.isEmpty(map)){
			log.debug("getResult Shipping ---------------->params ok");
			
			ShippingPriceBo shippingPrice = shippingManageService.getShippingPrice(scb);
			List<ShippingPriceResultItem> data=converShippingPriceResult(shippingPrice);
			map.put("status", "Y");
			map.put("data", data);
			log.info("getResult Shipping ---------------->result ok! data:"+data);
		}else{
			map.put("status", "N");
			log.info("getResult getShippingPrice params fail   map:"+map);
		}
	}
	

	private boolean validateToeknValue(String sk) {
		if (StringUtils.isEmpty(token) || StringUtils.isEmpty(sk)) {
			return false;
		}
		return token.equals(sk);
	}
	private boolean filterItem(Boolean isShow,Map map){
		if(!isShow && (MapUtils.isEmpty(map))){
			return false;
		}
		return true;
	}
	private List<ShippingPriceResultItem> converShippingPriceResult(ShippingPriceBo s){
		//ShippingPriceResult spr=new ShippingPriceResult();
		TreeMap<String,ShippingPriceResultItem> map=new TreeMap<String,ShippingPriceResultItem>(); 
		List<ShippingPriceResultItem> list=Lists.newArrayList();
		if(s==null){
			return list;
		}
		if(StringUtils.isNotEmpty(s.getSurfaceTitle()) /*&& (s.getSurfacePrice()!=null)*/ && filterItem(s.isSurfaceShow(),s.getSurfaceCodeMap())){
			ShippingPriceResultItem surfaceResult = new ShippingPriceResultItem() ;
			setDisplayItem(s.getSurfaceCode(),s.getSurfaceTitle(),s.getSurfaceDescription(),s.getSurfacePrice(),
					surfaceResult,s.isSurfaceStrack(),ShippingType.SurfaceType.toString(),s.getSurfaceOrder(),s.getSurfaceCodeId(),
					s.isSurfaceShow(),s.getSurfaceCodeMap());
			map.put(surfaceResult.getOrder()+"_"+surfaceResult.getType(),surfaceResult);
		}
		if(StringUtils.isNotEmpty(s.getRegistTitle()) && filterItem(s.isRegistShow(),s.getRegistCodeMap())){
			ShippingPriceResultItem registResult = new ShippingPriceResultItem() ;
			setDisplayItem(s.getRegistCode(),s.getRegistTitle(),s.getRegistDescription(),s.getRegistPrice(),
						registResult,s.isRegistStrack(),ShippingType.RegistType.toString(),s.getRegistOrder(),s.getRegistCodeId(),
						s.isRegistShow(),s.getRegistCodeMap());	
			map.put(registResult.getOrder()+"_"+registResult.getType(),registResult);
		}
		if(StringUtils.isNotEmpty(s.getExpressTitle())  && filterItem(s.isExpressShow(),s.getExpressCodeMap())){
			ShippingPriceResultItem expressResult = new ShippingPriceResultItem() ;
			setDisplayItem(s.getExpressCode(),s.getExpressTitle(),s.getExpressDescription(),s.getExpressPrice(),
						expressResult,s.isExpressStrack(),ShippingType.ExpressType.toString(),s.getExpressOrder(),s.getExpressCodeId(),
						s.isExpressShow(),s.getExpressCodeMap());
			map.put(expressResult.getOrder()+"_"+expressResult.getType(),expressResult);
		}
		if(StringUtils.isNotEmpty(s.getSpecialTitle())  && filterItem(s.isSpecialShow(),s.getSpecialCodeMap())){
			ShippingPriceResultItem specialResult = new ShippingPriceResultItem() ;
			setDisplayItem(s.getSpecialCode(),s.getSpecialTitle(),s.getSpecialDescription(),s.getSpecialPrice(),
						specialResult,s.isSpecialStrack(),ShippingType.SpecialType.toString(),s.getSpecialOrder(),s.getSpecialCodeId(),
						s.isSpecialShow(),s.getSpecialCodeMap());
			map.put(specialResult.getOrder()+"_"+specialResult.getType(),specialResult);
		}
		
		log.info("--------------->"+map);
		if(MapUtils.isNotEmpty(map)){
			Set<Entry<String, ShippingPriceResultItem>> entrySet = map.entrySet();
			for(Entry<String, ShippingPriceResultItem> entry : entrySet){
				list.add(map.get(entry.getKey()));
			}
		}

		log.info("converShippingPriceResult   ShippingPriceResult :"+map);
		return list;
		
		
	}
	private void setDisplayItem(String code,String title,String description,Double price,ShippingPriceResultItem item,boolean isStrack,String type,
			int typeOrder,int id,boolean isShow,Map<String,String> errorCodeInfor){
		if(item==null){
			item=new ShippingPriceResultItem();
		}
		
		item.setId(id);
		item.setOrder(typeOrder);
		item.setCode(code);
		item.setTitle(title);
		item.setDescription(description);
		item.setPrice(price);
		item.setType(type);
		item.setIsShow(isShow);
		item.setIsStrack(isStrack);
		if(!isShow && MapUtils.isNotEmpty(errorCodeInfor)){
			Set<String> keySet = errorCodeInfor.keySet();
			String errorCode = keySet.iterator().next();
			String errorDescription = errorCodeInfor.get(errorCode);
			item.setErrorCode(errorCode);
			item.setErrorDescription(errorDescription);
		}
		log.info("----------->setDisplayItem:"+item);
	}
	
	
	
	/**
	 * 物流计算
	 * @param request
	 * @param jsonParam
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/shipping/code")
	@ResponseBody
	public String shippingCalculateService(HttpServletRequest request,@RequestBody String jsonParam) {
		Map<String,Object> map=Maps.newHashMap();
		//参数校验
		String header = request.getHeader("token");
		if(!validateToeknValue(header)){//校验权限
			log.info("shippingCalculateService params fail, token is unavailable token:"+token);
			map.put("status", "N");
			map.put("msg", "403");
			
			return JSONObject.toJSONString(map);
		}
		try{
			ObjectMapper om=new ObjectMapper();
			JsonNode node = om.readTree(jsonParam);
			ShippingPriceParamsDto shippingPriceParamsDto = om.convertValue(
					node,
					ShippingPriceParamsDto.class);
			List<ShippingPriceCalculateDto> list = shippingManageService.shippingCalculateService(shippingPriceParamsDto);
			map.put("status", "Y");
			map.put("data", list);
		}catch(Exception e){
			log.error("shippingCalculateService error   jsonParam:"+jsonParam+"----------",e);
			map.put("status", "E");
			map.put("msg", e.getMessage());
		}
		return JSONObject.toJSONString(map);
	}
	
	/**
	 * 物流计算
	 * @param request
	 * @param jsonParam
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/shipping/{code}/name")
	@ResponseBody
	public String shippingCodeName(HttpServletRequest request,@RequestParam(value = "languageid", required = false) Integer  languageid,
			@PathVariable("code") String code) {
		Map<String,Object> map=Maps.newHashMap();
		//参数校验
		String header = request.getHeader("token");
		if(!validateToeknValue(header)){//校验权限
			log.info("shippingCodeName params fail, token is unavailable token:"+token);
			map.put("status", "N");
			map.put("msg", "403");
			
			return JSONObject.toJSONString(map);
		}
		try{
			ShippingDisplayNamePo shippingCodeName = shippingManageService.shippingCodeName(code,languageid);
			map.put("status", "Y");
			map.put("data", shippingCodeName);
		}catch(Exception e){
			log.error("shippingCodeName error   code:"+code+" language:"+languageid+"----------",e);
			map.put("status", "E");
			map.put("msg", e.getMessage());
		}
		return JSONObject.toJSONString(map);
	}
	
	@CacheEvict(value = "templateCacheManager", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/clean/templateCache", method = RequestMethod.GET)
	@ResponseBody
	public String clearTemplateCache() {
		return "clear templateCache success";
	}
	
	@CacheEvict(value = "exchangeCacheManager", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/clean/exchangeCache", method = RequestMethod.GET)
	@ResponseBody
	public String clearExchangeCache() {
		return "clear exchangeCache  success";
	}
	
	@CacheEvict(value = "productBaseCacheManager", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/clean/productBaseCache", method = RequestMethod.GET)
	@ResponseBody
	public String clearProductBaseCach() {
		return "clear productBaseCach  success";
	}
	@CacheEvict(value = "systemParams", allEntries = true, beforeInvocation = true)
	@RequestMapping(value = "/clean/systemParamsCache", method = RequestMethod.GET)
	@ResponseBody
	public String clearSystemParamsCach() {
		return "clear systemParamsCache  success";
	}
}
