package com.tomtop.configuration;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.tomtop.utils.FoundationService;

@ConfigurationProperties(value = "payment")
public class PaymentSettings extends AbstractSettings {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(PaymentSettings.class);

	private boolean sandbox;
	private String paypalUser;
	private String paypalPwd;
	private String paypalSignature;
	private String paypalEndPoint;
	private String paypalRedirectURL;
	private String paypalImg;
	private String paypalVersion;
	private String paypalIpn;
	private String oceanIpn;
	private String oceanEndPoint;
	private String oceanBackUrl;
	private String brandName;
	private JSONObject brandNameJson;

	private String methods;
	private Map<String, String[]> methodsList;

	@Autowired
	FoundationService foundationService;

	public PaymentSettings() {

	}

	public boolean isSandbox() {
		return sandbox;
	}

	public void setSandbox(boolean sandbox) {
		this.sandbox = sandbox;
	}

	public String getPaypalUser() {
		return paypalUser;
	}

	public void setPaypalUser(String paypalUser) {
		this.paypalUser = paypalUser;
	}

	public String getPaypalPwd() {
		return paypalPwd;
	}

	public void setPaypalPwd(String paypalPwd) {
		this.paypalPwd = paypalPwd;
	}

	public String getPaypalSignature() {
		return paypalSignature;
	}

	public void setPaypalSignature(String paypalSignature) {
		this.paypalSignature = paypalSignature;
	}

	public String getPaypalEndPoint() {
		return paypalEndPoint;
	}

	public void setPaypalEndPoint(String paypalEndPoint) {
		this.paypalEndPoint = paypalEndPoint;
	}

	public String getPaypalRedirectURL() {
		return paypalRedirectURL;
	}

	public void setPaypalRedirectURL(String paypalRedirectURL) {
		this.paypalRedirectURL = paypalRedirectURL;
	}

	public String getPaypalImg() {
		return paypalImg;
	}

	public void setPaypalImg(String paypalImg) {
		this.paypalImg = paypalImg;
	}

	public String getPaypalVersion() {
		return paypalVersion;
	}

	public void setPaypalVersion(String paypalVersion) {
		this.paypalVersion = paypalVersion;
	}

	public String getPaypalIpn() {
		return paypalIpn;
	}

	public void setPaypalIpn(String paypalIpn) {
		this.paypalIpn = paypalIpn;
	}

	public String getOceanIpn() {
		return oceanIpn;
	}

	public void setOceanIpn(String oceanIpn) {
		this.oceanIpn = oceanIpn;
	}

	public String getOceanEndPoint() {
		return oceanEndPoint;
	}

	public void setOceanEndPoint(String oceanEndPoint) {
		this.oceanEndPoint = oceanEndPoint;
	}

	public String getOceanBackUrl() {
		return oceanBackUrl;
	}

	public void setOceanBackUrl(String oceanBackUrl) {
		this.oceanBackUrl = oceanBackUrl;
	}

	public String getBrandName() {
		if (this.brandNameJson == null) {
			return null;
		}
		String hostName = this.foundationService.getHostName();
		return this.brandNameJson.getString(hostName);
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
		if (StringUtils.isNotEmpty(brandName)) {
			try {
				brandNameJson = JSON.parseObject(brandName);
			} catch (Exception e) {
				LOGGER.error("config:payment.brandName can not parse to json",
						e);
			}

		}
	}

	/**
	 * 获取支付方式配置
	 * 
	 * @param websiteName
	 * @return not return null
	 */
	public String[] getMethods(String websiteName) {
		if (methodsList == null) {
			return null;
		}
		String[] method = methodsList.get(websiteName);
		return method;
	}

	@SuppressWarnings("unchecked")
	public void setMethods(String methods) {
		this.methods = methods;
		Map<String, JSONArray> map = JSON.parseObject(methods, Map.class);
		this.methodsList = Maps.transformValues(map,
				new Function<JSONArray, String[]>() {

					@Override
					public String[] apply(JSONArray input) {
						String[] result = new String[input.size()];
						input.toArray(result);
						return result;
					}

				});
	}

}
