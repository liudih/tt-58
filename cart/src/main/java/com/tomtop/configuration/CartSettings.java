package com.tomtop.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author lijun
 *
 */
@ConfigurationProperties(value = "cart")
public class CartSettings extends AbstractSettings {

	private String imgUrlPrefix;
	private String imgurl;
	private String url;
	private JSONObject imgUrlPrefixJson = null;

	public String getImgUrlPrefix() {
		return imgUrlPrefix;
	}

	public void setImgUrlPrefix(String imgUrlPrefix) {
		this.imgUrlPrefix = imgUrlPrefix;
		imgUrlPrefixJson = JSONObject.parseObject(imgUrlPrefix);
	}

	public String getPrefix(String domain) {
		if (imgUrlPrefixJson.containsKey(domain)) {
			return imgUrlPrefixJson.getString(domain);
		}
		return null;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public JSONObject getImgUrlPrefixJson() {
		return imgUrlPrefixJson;
	}

	public void setImgUrlPrefixJson(JSONObject imgUrlPrefixJson) {
		this.imgUrlPrefixJson = imgUrlPrefixJson;
	}

}

