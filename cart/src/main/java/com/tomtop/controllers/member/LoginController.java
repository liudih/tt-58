package com.tomtop.controllers.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.controllers.cart.CartController;
import com.tomtop.dto.ISOCountry;
import com.tomtop.utils.CookieUtils;
import com.tomtop.utils.FoundationService;
import com.tomtop.utils.HttpClientUtil;

@Controller
@RequestMapping("/cart")
public class LoginController {
	
	@Autowired
	FoundationService foundationService;
	
	Logger logger = Logger.getLogger(LoginController.class);

	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	public Object tologin(Model model,@RequestParam("email") String email,
			@RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> mjson = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("email", email);
		param.put("pwd", password);
		param.put("client", foundationService.getSiteID());
		String result = HttpClientUtil.doPost(FoundationService.MEMBER_URL+"/member/v1/login",JSON.toJSONString(param));
		JSONObject jb = JSON.parseObject(result);
		Integer ret = jb.getInteger("ret");
		logger.debug("result=="+result);
		if(ret==1){
			String token = jb.getJSONObject("data").getString("token");
			String uuid = jb.getJSONObject("data").getString("uuid");
			CookieUtils.setCookie(foundationService.TT_TOKEN, token);
			CookieUtils.setCookie(foundationService.TT_UUID, uuid);
			mjson.put("result", "success");
		}else{
			mjson.put("result", jb.getString("errMsg"));
		}
		return mjson;
	}
	
	
	
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	@ResponseBody
	public Object register(Model model,@RequestParam("email") String email,
			@RequestParam("passwd") String password,
			@RequestParam("country") String country){
		
		Map<String, Object> mjson = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("email", email);
		param.put("pwd", password);
		param.put("countryShort", country);
		param.put("client", foundationService.getClientID());
		String result = HttpClientUtil.doPut(FoundationService.MEMBER_URL+"/member/v1/register",JSON.toJSONString(param));
		JSONObject jb = JSON.parseObject(result);
		Integer ret = jb.getInteger("ret");
		logger.debug("result=="+result);
		if(ret==1){
			mjson.put("result", "success");
		}else{
			mjson.put("result", jb.getString("errMsg"));
		}
		return mjson;
	}
	
	@RequestMapping(value="/validEmail", method = RequestMethod.GET)
	@ResponseBody
	public Object validEmail(Model model,@RequestParam("email") String email) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		boolean valid = false;
		if (email==null || "".equals(email)) {
			valid = false;
		}
		// 首先验证邮箱是否是合法的
		Pattern emailRegx = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
		Matcher matcher = emailRegx.matcher(email);
		if (matcher.find()) {
			valid = true;
		}else{
			valid = false;
		}
		if(valid){
			mjson.put("errorCode", 0);
		}else{
			mjson.put("errorCode", 1);
		}
		return mjson;
	}
}
