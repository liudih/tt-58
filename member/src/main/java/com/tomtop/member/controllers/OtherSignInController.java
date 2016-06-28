package com.tomtop.member.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Maps;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.member.models.bo.BaseBo;
import com.tomtop.member.models.bo.OAuthUserBo;
import com.tomtop.member.models.bo.OtherSignBo;
import com.tomtop.member.models.bo.TokenUUidBo;
import com.tomtop.member.service.IMemberOtherService;
import com.tomtop.member.service.IMemberService;
import com.tomtop.member.service.IOtherSignInOAuth;
import com.tomtop.member.utils.CommonUtils;

/**
 * 第三方登陆操作类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/other")
public class OtherSignInController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IMemberOtherService memberOtherService;
	@Autowired
	IMemberService memberService;
	@Autowired
	Set<IOtherSignInOAuth> otherSignInOAuths;
	
	Map<String, IOtherSignInOAuth> otherSignInOAuthMap = Maps.newLinkedHashMap();
	
	@Autowired
	public OtherSignInController(Set<IOtherSignInOAuth> oAuths) {
		for(IOtherSignInOAuth oAuth : oAuths){
			otherSignInOAuthMap.put(oAuth.type(), oAuth);
		}
	}
	
	/**
	 * 获取第三方登陆链接
	 */
	@RequestMapping(method = RequestMethod.GET,value = "/v1/signIn")
	public Result getOtherSignInLink(
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website){
		Result res = new Result();
		List<OtherSignBo> osbList = new ArrayList<OtherSignBo>();
		for(IOtherSignInOAuth oAuth : otherSignInOAuths){
			OtherSignBo osb = new OtherSignBo();
			osb.setUrl(oAuth.createLink(website));
			osb.setType(oAuth.type());
			osbList.add(osb);
		}
		res.setRet(CommonUtils.SUCCESS_RES);
		res.setData(osbList);
		
		return res;
	}
	
	@RequestMapping(method = RequestMethod.GET,value = "/{type}")
	public Result callBack(@PathVariable("type") String type, @RequestParam(value="code", required = false, defaultValue = "") String code,
			@RequestParam(value = "website", required = false, defaultValue = "1") Integer website){
		Result res = new Result();
		IOtherSignInOAuth oAuth = otherSignInOAuthMap.get(type);
		if(oAuth==null){
			res.setRet(0);
			res.setErrCode("-20005");
			res.setErrMsg(type+" callBack Not supported");
			return res;
		}
		
		logger.info("{} login code {} ========== ", type, code);
		if (!StringUtils.isEmpty(code)) {
			code = CommonUtils.checkSpecialChar(code);
			String token = oAuth.getAccessToken(code, website);
			if(StringUtils.isEmpty(token)){
				res.setRet(0);
				res.setErrCode("-20001");
				res.setErrMsg("Handshake failure ");
				return res;
			}
			OAuthUserBo user = oAuth.getUserInfo(token);
			if(user == null){
				res.setRet(0);
				res.setErrCode("-20002");
				res.setErrMsg("get "+type+" user failure ");
				return res;
			}
			//把用户存进loginOrRegistration
			BaseBo bb = null;
			try {
				bb = memberOtherService.loginOrRegistration(user.getEmail(), type, user.getUid(), website, null);
			} catch (Exception e) {
				e.printStackTrace();
				res.setRet(0);
				res.setErrCode("-20004");
				res.setErrMsg(type+" callBack error");
				return res;
			}
			if(bb.getRes() == CommonUtils.SUCCESS_RES){
				TokenUUidBo tub = memberService.getOtherToken(user.getEmail(), website);
				user.setToken(tub.getToken());
				user.setUuid(tub.getUuid());
				res.setRet(CommonUtils.SUCCESS_RES);
				res.setData(user);
			}else{
				res.setRet(0);
				res.setErrCode("-20003");
				res.setErrMsg(type+" other login or registration error");
			}
		}else{
			res.setRet(0);
			res.setErrCode("-20004");
			res.setErrMsg(type+" callBack error");
		}
		return res;
	}
}
