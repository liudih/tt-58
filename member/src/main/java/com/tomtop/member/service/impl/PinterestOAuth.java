package com.tomtop.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.member.configuration.OAuthSettings;
import com.tomtop.member.models.bo.OAuthUserBo;
import com.tomtop.member.models.other.GoogleUser;
import com.tomtop.member.service.IOtherSignInOAuth;
import com.tomtop.member.utils.ClientUtil;

/**
 * See https://developers.pinterest.com/docs/api/overview/  find Authentication
 * @author lixiangjing
 *
 */
//@Service
public class PinterestOAuth implements IOtherSignInOAuth {

	private final static String CODE = "code";
	private final static String STATE = "state";
	private final static String TYPE = "pinterest";
	
	@Override
	public String createLink(Integer website) {
		StringBuilder sb = new StringBuilder("https://api.pinterest.com/oauth/")
		.append("?client_id=").append(OAuthSettings.appid(TYPE, website))
		.append("&redirect_uri=").append(OAuthSettings.redirectUri(TYPE, website))
		.append("&response_type=").append(CODE)
		.append("&state=").append(STATE)
		.append("&scope=").append("read_public,write_public");
		return sb.toString();
	}

	@Override
	public String getAccessToken(String code, Integer website) {
		String url = "https://api.pinterest.com/v1/oauth/token";
		StringBuilder param = new StringBuilder("client_id=").append(OAuthSettings.appid(TYPE, website))
				.append("&client_secret=").append(OAuthSettings.appSecret(TYPE, website))
				.append("&code=").append(code)
				.append("&redirect_uri=").append(OAuthSettings.redirectUri(TYPE, website))
				.append("&grant_type=authorization_code");
		String body = ClientUtil.postUrl(url, param.toString());
		if(body == null){
			return null;
		}
		JSONObject object = JSON.parseObject(body);
		String token = (String) object.get("access_token");
		return token;
	}

	@Override
	public OAuthUserBo getUserInfo(String token) {
		String uri = "https://api.pinterest.com/v1/me/?access_token=" + token;
		String feedback = ClientUtil.getRequest(uri);
		if(feedback == null){
			return null;
		}
		JSONObject object = JSON.parseObject(feedback);
		// TODO GoogleUser
		GoogleUser gu = JSONObject.toJavaObject(object, GoogleUser.class);
		gu.setEmail(gu.getEmails().get(0).getValue());
		gu.setUid(gu.getId());
		return gu;
	}

	@Override
	public String type() {
		return TYPE;
	}

}
