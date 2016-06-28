package com.tomtop.base.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.CurrencyBo;
import com.tomtop.base.models.dto.CurrencyBase;
import com.tomtop.base.service.ICurrencyBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

/**
 * 货币信息操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/base/currency")
public class CurrencyBaseController {

	@Autowired
	ICurrencyBaseService currencyBaseService;
	
	/**
	 * 获取所有货币信息
	 * 
	 * @return BaseResult
	 */
	@Cacheable(value = "currency", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1")
	public Result getCurrency(){
		Result res = new Result();
		List<CurrencyBo> cblist = currencyBaseService.getCurrencyList();
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
	 * 获取货币信息
	 * @param id
	 *        主键Id
	 *        
	 * @return BaseResult
	 */
	@Cacheable(value = "currency", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/{id}")
	public Result getCurrencyById(@PathVariable Integer id){
		Result res = new Result();
		CurrencyBo cb = currencyBaseService.getCurrencyById(id);
		if(cb.getRes() != CommonUtils.SUCCESS_RES){
			res.setRet(CommonUtils.NOT_DATA_RES);
			res.setErrMsg(CommonUtils.NOT_DATA_MSG_RES);
			return res;
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		cb.setRes(null);
		res.setData(cb);
		
		return res;
	}
	
	/**
	 * 获取货币信息
	 * @param name
	 *        货币名称
	 * @param code
	 *        货币代码
	 * @return BaseResult
	 */
	@Cacheable(value = "currency", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET,value = "/v1/where")
	public Result getCurrencyByWhere(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "code", required = false) String code){
		Result res = new Result();
		CurrencyBase cb = new CurrencyBase();
		if(name != null && !"".equals(name)){
			cb.setName(name);
		}
		if(code != null && !"".equals(code)){
			cb.setCode(code);
		}
		List<CurrencyBo> cblist = currencyBaseService.getCurrencyList(cb);
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
	 * 获取汇率
	 * @param code
	 *        货币代码
	 *        
	 * @return BaseResult
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,value = "/v1/code/{code}")
	public Result getCurrencyByCode(@PathVariable String code){
		Result res = new Result();
		CurrencyBase cb = new CurrencyBase();

		if(code != null && !"".equals(code)){
			cb.setCode(code);
		}
		Double rate = currencyBaseService.getRate(code);
				res.setRet(1);
				res.setData(rate);
		return res;
	}
	
	/**
	 * 货币价格转换
	 * @param code
	 *        货币代码
	 *        
	 * @return BaseResult
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET,value = "/v1/exchange")
	public Result getCurrencyByCode(
			@RequestParam(value = "money", required = false) Double money,
			@RequestParam(value = "oldCy", required = false) String oldCy,
			@RequestParam(value = "newCy", required = false) String newCy){
		Result res = new Result();
		if(money == null){
			res.setRet(0);
			res.setErrMsg("money is null");
			return res;
		}
		if(oldCy == null || "".equals(oldCy.trim())){
			res.setRet(0);
			res.setErrMsg("oldCy is null");
			return res;
		}
		if(newCy == null || "".equals(oldCy.trim())){
			res.setRet(0);
			res.setErrMsg("newCy is null");
			return res;
		}
		Double rate = currencyBaseService.exchange(money, oldCy, newCy);
		res.setRet(1);
		res.setData(rate);
		return res;
	}
}
