package com.tomtop.member.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tomtop.member.configuration.OAuthSettings;
import com.tomtop.member.models.bo.OAuthUserBo;
import com.tomtop.member.models.other.FaceBookUser;
import com.tomtop.member.service.IOtherSignInOAuth;
import com.tomtop.member.utils.ClientUtil;

@Service
public class FaceBookOAuth implements IOtherSignInOAuth {

	private final static String CODE = "code";
	private final static String TYPE = "facebook";
	
	@Override
	public String createLink(Integer website) {
		StringBuilder sb = new StringBuilder("https://www.facebook.com/dialog/oauth")
		.append("?client_id=").append(OAuthSettings.appid(TYPE, website))
		.append("&redirect_uri=").append(OAuthSettings.redirectUri(TYPE, website))
		.append("&response_type=").append(CODE)
		.append("&scope=").append("email");
		return sb.toString();
	}

	@Override
	public String getAccessToken(String code, Integer website) {
		try {
			StringBuilder url = new StringBuilder("https://graph.facebook.com/oauth/access_token")
					.append("?client_id=").append(OAuthSettings.appid(TYPE, website))
					.append("&client_secret=").append(OAuthSettings.appSecret(TYPE, website))
					.append("&code=").append(code)
					.append("&redirect_uri=").append(URLEncoder.encode(OAuthSettings.redirectUri(TYPE, website), "UTF-8"));
			String body = ClientUtil.getRequest(url.toString());
			if(body == null){
				return null;
			}
			String token = body.replaceFirst("access_token=([^&]*).*$", "$1");
			return token;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Should not happened", e);
		}
	}

	@Override
	public OAuthUserBo getUserInfo(String token) {
		String uri = "https://graph.facebook.com/v2.2/me?"
				+ "fields=id,name,email,first_name,middle_name,last_name,gender,"
				+ "link,timezone,updated_time,verified&access_token=" + token;
		String feedback = ClientUtil.getRequest(uri);
		if(feedback == null){
			return null;
		}
		JSONObject object = JSON.parseObject(feedback);
		FaceBookUser fbu = new FaceBookUser();
		
		String id = (String) object.get("id");
		String name = (String) object.get("name");
		String email = (String) object.get("email");
		String firstName = (String) object.get("first_name");
		String lastName = (String) object.get("last_name");
		String gender = (String) object.get("gender");
		String link = (String) object.get("link");
		//Integer timezone = (Integer) object.get("timezone");
		//Boolean verified = (Boolean) object.get("verified");
		
		fbu.setUid(id);
		fbu.setName(name);
		fbu.setEmail(email);
		fbu.setFirstName(firstName);
		fbu.setLastName(lastName);
		fbu.setGender(gender);
		fbu.setLink(link);
		//fbu.setTimezone(timezone);
		//fbu.setVerified(verified);
		
		return fbu;
	}

	@Override
	public String type() {
		return TYPE;
	}

}
