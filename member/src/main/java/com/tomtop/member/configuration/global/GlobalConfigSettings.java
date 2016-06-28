package com.tomtop.member.configuration.global;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author xcf
 *
 */
@ConfigurationProperties(value = "global", prefix = "global",locations = "classpath:golbal-config.properties")  
public class GlobalConfigSettings {

	private String urls;
	
	private String fromEmails;
	
	private String fromNames;

	private JSONArray urlJsonArry;
	private JSONArray femailJsonArry;
	private JSONArray fnameJsonArry;

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {

		this.urls = urls;
		this.urlJsonArry = JSON.parseArray(this.urls);
	}
	public String getFromEmails() {
		return fromEmails;
	}

	public void setFromEmails(String fromEmails) {
		this.fromEmails = fromEmails;
		this.femailJsonArry = JSON.parseArray(this.fromEmails);
	}

	public String getFromNames() {
		return fromNames;
	}

	public void setFromNames(String fromNames) {
		this.fromNames = fromNames;
		this.fnameJsonArry = JSON.parseArray(this.fromNames);
	}

	public String getUrlByKey(Integer websiteId) {
		if (null == this.urlJsonArry || this.urlJsonArry.size() == 0) {
			return null;
		}

		for (int i = 0; i < this.urlJsonArry.size(); i++) {
			JSONObject json = this.urlJsonArry.getJSONObject(i);
			if (json.containsKey(websiteId.toString())) {
				return json.get(websiteId.toString()).toString();
			} else {
				continue;
			}
		}
		return null;
	}

	public String getFromEmailByKey(Integer websiteId) {
		if (null == this.femailJsonArry || this.femailJsonArry.size() == 0) {
			return null;
		}

		for (int i = 0; i < this.femailJsonArry.size(); i++) {
			JSONObject json = this.femailJsonArry.getJSONObject(i);
			if (json.containsKey(websiteId.toString())) {
				return json.get(websiteId.toString()).toString();
			} else {
				continue;
			}
		}
		return null;
	}
	
	public String getFromNameByKey(Integer websiteId) {
		if (null == this.fnameJsonArry || this.fnameJsonArry.size() == 0) {
			return null;
		}

		for (int i = 0; i < this.fnameJsonArry.size(); i++) {
			JSONObject json = this.fnameJsonArry.getJSONObject(i);
			if (json.containsKey(websiteId.toString())) {
				return json.get(websiteId.toString()).toString();
			} else {
				continue;
			}
		}
		return null;
	}
}
