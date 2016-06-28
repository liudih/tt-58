package com.tomtop.member.controllers;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.TokenUUidBo;
import com.tomtop.member.models.filter.FindPasswordFilter;
import com.tomtop.member.models.filter.PwdUpdateFilter;
import com.tomtop.member.service.IForgotPasswordService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.utils.CommonUtils;

/**
 * 找回密码操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/findpwd")
public class FindPasswordController {

	@Autowired
	IForgotPasswordService forgotPasswordService;
	
	@Autowired
	IMemberService memberService;

	/**
	 * 找回密码发送邮件
	 * 
	 * @param FindPasswordFilter
	 *            email - client
	 * 
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/send")
	public Result findPassword(@RequestBody FindPasswordFilter fpilter) {
		Result res = new Result();
		String email = fpilter.getEmail();
		Integer siteid = fpilter.getWebsite();
		Integer lang = fpilter.getLang();
		BaseBo fpb = forgotPasswordService.forgotPassword(email, siteid, lang);
		Integer re = fpb.getRes();
		String msg = fpb.getMsg();
		if (re == CommonUtils.SUCCESS_RES) {
			res.setRet(CommonUtils.SUCCESS_RES);
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
		}
		return res;
	}

	/**
	 * 通过cid修改密码
	 * 
	 * @param FindPasswordFilter
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/v1/alert")
	public Result alertPassword(@RequestBody FindPasswordFilter fpilter) {
		Result res = new Result();
		String cid = fpilter.getCid();
		String pwd = fpilter.getPwd();
		Integer siteid = fpilter.getWebsite();
		BaseBo bb = forgotPasswordService.alertPassword(cid, pwd, siteid);
		Integer re = bb.getRes();
		String msg = bb.getMsg();
		if (re == CommonUtils.SUCCESS_RES) {
			res.setRet(CommonUtils.SUCCESS_RES);
		} else {
			res.setRet(CommonUtils.ERROR_RES);
			res.setErrCode(re.toString());
			res.setErrMsg(msg);
			return res;
		}
		return res;
	}

	

	/**
	 * 会员中心修改密码 根据旧密码
	 * 
	 * @param FindPasswordFilter
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/v1/pwd/_update")
	public Result modifyPwdByOldPwd(@RequestBody String jsonParam) {
		Result res = new Result();
		try {
			ObjectMapper om = new ObjectMapper();
			PwdUpdateFilter fpilter = om.readValue(jsonParam, PwdUpdateFilter.class);

			if (StringUtils.isEmpty(fpilter.getCemail())
					|| StringUtils.isEmpty(fpilter.getCnewpassword())
					|| StringUtils.isEmpty(fpilter.getCcnewpassword())
					|| StringUtils.isEmpty(fpilter.getCpassword())
					|| StringUtils.isEmpty(fpilter.getCuuid())
					|| null == fpilter.getClient()) {
				res.setErrMsg("Parameters cannot be empty");
				res.setErrCode("-1");
				return res;
			}
			String oldPwd = fpilter.getCpassword();
			String email = fpilter.getCemail();
			
			if (!fpilter.getCnewpassword().equals(fpilter.getCcnewpassword())) {
				res.setErrMsg("Two password input is not consistent");
				res.setErrCode("-1");
				return res;
			}

			//驗證舊密碼是否正確,正確返回token及uuid
			TokenUUidBo tokenBo = this.memberService
					.getToken(email, oldPwd,
							fpilter.getWebsite());
				
			if (null != tokenBo && null != tokenBo.getToken()) {
				if (tokenBo.getUuid().equals(fpilter.getCuuid())) {
					this.forgotPasswordService.updatePassword(email,
							fpilter.getCnewpassword(), fpilter.getWebsite());
					res.setRet(1);
					return res;
				}
				
			}else{
				res.setErrMsg(tokenBo.getMsg());
				res.setErrCode("-1");
				return res;
			}

			

		} catch (Exception e) {
			res.setRet(-1);
			res.setErrCode("500");
			res.setData("comment modify faile" + e.getMessage());
			e.printStackTrace();
		}

		return res;
	}
}
