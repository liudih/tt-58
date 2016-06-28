package com.tomtop.base.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.base.models.bo.CurrencyBo;
import com.tomtop.base.service.ICurrencyBaseService;
import com.tomtop.base.utils.CommonUtils;
import com.tomtop.framework.core.utils.Result;

@RestController
public class TestController {

	@Autowired
	ICurrencyBaseService currencyBaseService;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Result test() {
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
}
