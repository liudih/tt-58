package com.tomtop.member.configuration;

import java.io.IOException;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class OAuthSettings {
	
	private static Logger logger = LoggerFactory.getLogger(OAuthSettings.class);
	
	/**
	 * example:oauths.get("facebook", "1", "app_id")
	 */
	private static MultiKeyMap oauths = new MultiKeyMap();
	
    private static String appid = "app_id";	 
    private static String appSecret = "app_secret";	 
    private static String redirectUri = "redirect_uri";	 
	
	static{
		try {
			String text = IOUtils.toString(OAuthSettings.class.getClassLoader()
					.getResourceAsStream("oauth.json"));
			JSONObject webs = JSON.parseObject(text);
			for(String web : webs.keySet()){
				JSONObject websites = webs.getJSONObject(web);
				for(String website : websites.keySet()){
					JSONObject keys = websites.getJSONObject(website);
					for(String key : keys.keySet()){
						oauths.put(web, website, key, keys.getString(key));
					}
				}
			}
		} catch (IOException e) {
			logger.error("=============>load oauth.json fail with ex<===============", e);
		}
	}
	
	/**
	 * @param type 第三方登陆
	 * @param website 站点
	 * @return
	 */
	public static String appid(String type,Integer website){
		return (String)oauths.get(type, website.toString(), appid);
	}
	
	/**
	 * @param type 第三方登陆
	 * @param website 站点
	 * @return
	 */
	public static String appSecret(String type,Integer website){
		return (String)oauths.get(type, website.toString(), appSecret);
	}
	
	/**
	 * @param type 第三方登陆
	 * @param website 站点
	 * @return
	 */
	public static String redirectUri(String type,Integer website){
		return (String)oauths.get(type, website.toString(), redirectUri);
	}
}
