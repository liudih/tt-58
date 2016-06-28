package com.tomtop.base.controllers;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.CountryBo;
import com.tomtop.base.models.dto.CountryBase;
import com.tomtop.base.service.ICountryBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 国家信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/country")
public class CountryBaseController {

	@Autowired
	ICountryBaseService countryBaseService;
	
	/**
	 * 获取所有国家信息
	 * 
	 * @return BaseResult
	 */
	@PostConstruct
	@Cacheable(value = "country", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getCountry(){
		Result res = new Result();
		List<CountryBo> cblist = countryBaseService.getCountryList();
		if(cblist != null && cblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 获取国家信息
	 * @param id
	 *        主键Id
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "country", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/{id}")
	public Result getCountryById(@PathVariable Integer id){
		Result res = new Result();
		CountryBo cb = countryBaseService.getCountryById(id);
		if(cb.getRes() != CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		cb.setRes(null);//设置为null对象值res不显示,只针对于返回单个对象值时使用
		res.setData(cb);
		
		return res;
	}
	/**
	 * 获取国家信息
	 * @param name
	 *        国家名称
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "country", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/name/{name}")
	public Result getCountryByName(@PathVariable String name){
		Result res = new Result();
		CountryBase cb = new CountryBase();
		cb.setName(name);
		List<CountryBo> cblist = countryBaseService.getCountryList(cb);
		if(cblist != null && cblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
	
	/**
	 * 获取国家信息
	 * @param code
	 *        国家code
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "country", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/code/{code}")
	public Result getCountryByCode(@PathVariable String code){
		Result res = new Result();
		CountryBase cb = new CountryBase();
		cb.setIsoCodeTwo(code);
		List<CountryBo> cblist = countryBaseService.getCountryList(cb);
		if(cblist != null && cblist.size() > 0 ){
			res.setRet(CommonUtils.SUCCESS_RES);
			res.setData(cblist.toArray());
		}else{
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
		}
		return res;
	}
}
