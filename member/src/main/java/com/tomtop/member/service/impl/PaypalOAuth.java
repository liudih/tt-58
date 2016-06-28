package com.tomtop.member.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.member.configuration.OAuthSettings;
import com.tomtop.member.models.bo.OAuthUserBo;
import com.tomtop.member.models.other.PaypalUser;
import com.tomtop.member.service.IOtherSignInOAuth;
import com.tomtop.member.utils.ClientUtil;

@Service
public class PaypalOAuth implements IOtherSignInOAuth {
	
	private final static String CODE = "code";
	private final static String type = "paypal";
	
	private final static String endpoint = "api.sandbox.paypal.com";
	//private final static String endpoint = "api.paypal.com";
	
	@Override
	public String createLink(Integer website) {
		StringBuilder sb = new StringBuilder("https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize")
		.append("?client_id=").append(OAuthSettings.appid(type, website))
		.append("&redirect_uri=").append(OAuthSettings.redirectUri(type, website))
		.append("&response_type=").append(CODE)
		.append("&scope=").append("openid email");
		return sb.toString();
	}

	@Override
	public String getAccessToken(String code, Integer website) {
		try {
			StringBuilder url = new StringBuilder("https://").append(endpoint)
				.append("/v1/identity/openidconnect/tokenservice")
				.append("?client_id=").append(OAuthSettings.appid(type, website))
				.append("&client_secret=").append(OAuthSettings.appSecret(type, website))
				.append("&grant_type=authorization_code")
				.append("&code=").append(code)
				.append("&redirect_uri=").append(URLEncoder.encode(OAuthSettings.redirectUri(type, website), "UTF-8"));
			String body = ClientUtil.getRequest(url.toString());
			if(body == null){
				return null;
			}
			JSONObject object = JSON.parseObject(body);
			String token = (String) object.get("access_token");
			
			return token;
		
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Should not happened", e);
		}
	}

	@Override
	public OAuthUserBo getUserInfo(String token) {
		String url = "https://" + endpoint
				+ "/v1/identity/openidconnect/userinfo?schema=openid";
		String headerValue = "Bearer " + token;
		
		String feedback = ClientUtil.paypalGet(url,headerValue);
		if(feedback == null){
			return null;
		}
		JSONObject object = JSON.parseObject(feedback);
		PaypalUser pu = new PaypalUser();
		String email = (String) object.get("email");
		String userId = (String) object.get("user_id");
		pu.setEmail(email);
		pu.setUid(userId);
		
		return pu;
	}

	@Override
	public String type() {
		return type;
	}

}
