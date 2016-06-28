package com.tomtop.member.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.dto.Statistics;
import com.tomtop.member.service.INewMemberService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 用户操作类 20160413
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/member")
public class NewMemberController {

	@Autowired
	INewMemberService newMemberService;
	/**
	 * 會員中心統計数
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/v1/center/count", method = RequestMethod.GET)
	public Result getMemberStatistics(
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website,
			@RequestParam(value = "email") String email) {
		Result res = new Result();
		try {
				List<Statistics> stlist = newMemberService.getMemberStatistics(email, website);
				res.setRet(CommonUtils.SUCCESS_RES);
				res.setData(stlist);

		} catch (Exception e) {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode("500");
			res.setData("get MemberStatistics faile" + e.getMessage());

			e.printStackTrace();
		}
		return res;
	}
}
