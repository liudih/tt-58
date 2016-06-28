package com.tomtop.loyalty.controllers;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.loyalty.models.IntegralModel;
import com.tomtop.loyalty.models.Page;
import com.tomtop.loyalty.models.Pageable;
import com.tomtop.loyalty.service.IIntegralService;
import com.tomtop.loyalty.utils.CommonUtils;

@RestController
@RequestMapping(value = "/integral")
public class IntegralController {
	
	@Autowired
	private IIntegralService iintegralService;
	
	
	
	
	
	/**
	 * 添加用户积分流水记录
	 * @param model 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/save")
	public Result save(@PathVariable String model) throws Exception {
		Result result = new Result();
		if(StringUtils.isBlank(model)){
			result.setErrMsg("参数为空");
			result.setErrCode(CommonUtils.ERROR_MSG_RES);
			result.setRet(CommonUtils.ERROR_RES);
			result.setData(model);
			return result;
		}
		
		IntegralModel integralModel = JSON.parseObject(model, IntegralModel.class);
		int id = iintegralService.addIntegralForUser(integralModel, integralModel.getDotype());
		if(id>0){
			result.setRet(CommonUtils.SUCCESS_RES);
		}else{
			result.setErrCode(CommonUtils.ERROR_MSG_RES);
			result.setRet(CommonUtils.ERROR_RES);
		}
		result.setData(id);
		return result;
	}
	
	
	/**
	 * 查询积分列表
	 * @param model 查询条件
	 * @param size 每页多少条
	 * @param num  第几页
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/qlist")
	public Result queryList(@PathVariable String model,@PathVariable int size,@PathVariable int num ) throws Exception {
		IntegralModel integralModel = JSON.parseObject(model, IntegralModel.class);
		Pageable condition = new Pageable(num,size,integralModel);
		Page<IntegralModel> page = iintegralService.searchIntegralForUser(condition);
		Result result = new Result();
		
		com.tomtop.framework.core.utils.Page np = new com.tomtop.framework.core.utils.Page();
		np.setCurrentPage(page.getPageNumber());
		np.setPageSize(page.getPageSize());
		Double totalPage = Math.ceil(page.getTotal()/page.getPageSize());
		np.setTotalPage(totalPage.intValue());
		Long totalNum = page.getTotal();
		np.setTotalRecord(totalNum.intValue());
		np.setStartRec(page.getPageable().getBegin());
		np.setEndRec(page.getPageable().getEnd());
		result.setPage(np);
		result.setData(page.getContent());
		result.setRet(CommonUtils.SUCCESS_RES);
		return result;
	}
	
	
	/**
	 * 查询总积分
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/qcount/{website}")
	public int queryCount(@RequestParam(value = "email", required = true) String email,@PathVariable Integer website) throws Exception {
		return iintegralService.countIntegralForUser(email,website);
	}
	

	
	/**
	 * 使用积分成功减积分的接口,MQ回调事件接口
	 * @param model 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/subtraction")
	public Result saveForSubtraction(@PathVariable String model) throws Exception {
		Result result = new Result();
		if(StringUtils.isBlank(model)){
			result.setErrMsg("参数为空");
			result.setErrCode(CommonUtils.ERROR_MSG_RES);
			result.setRet(CommonUtils.ERROR_RES);
			result.setData(model);
			return result;
		}
		
		IntegralModel integralModel = JSON.parseObject(model, IntegralModel.class);
		int id = iintegralService.addIntegralForUser(integralModel, integralModel.getDotype());
		if(id>0){
			result.setRet(CommonUtils.SUCCESS_RES);
		}else{
			result.setErrCode(CommonUtils.ERROR_MSG_RES);
			result.setRet(CommonUtils.ERROR_RES);
		}
		result.setData(id);
		return result;
	}
	
}
