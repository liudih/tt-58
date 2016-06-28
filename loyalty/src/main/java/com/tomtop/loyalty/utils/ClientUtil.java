package com.tomtop.loyalty.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class ClientUtil {

	private static Logger logger = LoggerFactory.getLogger(ClientUtil.class);

	/**
	 * post提交
	 * 
	 * @param url
	 * @return
	 */
	public static String postUrl(String url, String content) {
		// url = url + "?";
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(10000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/json");
		post.getParams().setContentCharset("UTF-8");
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		String[] paramArray = content.split("&");
		for (int i = 0; i < paramArray.length; i++) {
			String equalString = paramArray[i];
			String[] keyArray = equalString.split("=");
			String key = keyArray[0];
			String value = keyArray[1];
			post.addParameter(key, value);
			System.out.println(key + ":" + value);
		}
		String result = "";
		try {
			int statusCode = httpClient.executeMethod(post);
			if (statusCode == HttpStatus.SC_OK) {
				result = post.getResponseBodyAsString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (post != null)
				post.releaseConnection(); // 释放连接
		}
		return result;
	}

	/**
	 * get提交
	 * 
	 * @param uri
	 * @return
	 */
	public static String getRequest(String uri) {
		HttpMethod method = null;
		try {
			HttpClient client = new HttpClient(new HttpClientParams(),
					new SimpleHttpConnectionManager(true));
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(10000);
			client.getHttpConnectionManager().getParams().setSoTimeout(10000);
			method = new GetMethod(uri); // 使用GET方法
			client.executeMethod(method); // 请求资源
			int status = method.getStatusCode();
			if (200 == status) {
				return method.getResponseBodyAsString().trim();
			} else {
				throw new Exception("请求失败，响应状态码" + status);
			}
		} catch (Exception e) {
			logger.error("--executeMethod failed--url=" + uri, e);
			return null;
		} finally {
			if (method != null)

				method.releaseConnection(); // 释放连接
		}
	}

	/**
	 * paypalGet提交
	 * 
	 * @param uri
	 * @return
	 */
	public static String paypalGet(String uri, String headerValue) {
		HttpMethod method = null;
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(10000);
			client.getHttpConnectionManager().getParams().setSoTimeout(10000);
			method = new GetMethod(uri); // 使用GET方法
			method.setRequestHeader("Content-Type", "application/json");
			method.setRequestHeader("Authorization", headerValue);
			client.executeMethod(method); // 请求资源
			int status = method.getStatusCode();
			if (200 == status) {
				return method.getResponseBodyAsString().trim();
			} else {
				throw new Exception("请求失败，响应状态码" + status);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (method != null)
				method.releaseConnection(); // 释放连接
		}
	}

	public static String postJson(String url, Map<String, Object> map) {
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(10000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/json");
		post.getParams().setContentCharset("UTF-8");
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		String json = JSON.toJSONString(map);
		try {
			RequestEntity params = new StringRequestEntity(json,
					"application/json", "UTF-8");
			post.setRequestEntity(params);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String result = "";
		try {
			int statusCode = httpClient.executeMethod(post);
			if (statusCode == HttpStatus.SC_OK) {
				result = post.getResponseBodyAsString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (post != null)
				post.releaseConnection(); // 释放连接
		}
		return result;
	}

	public static <T> T getRequestEntity(String uri, Class<T> t) {
		String resultString = "";
		HttpMethod method = null;
		try {
			HttpClient client = new HttpClient(new HttpClientParams(),
					new SimpleHttpConnectionManager(true));
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(10000);
			client.getHttpConnectionManager().getParams().setSoTimeout(10000);
			method = new GetMethod(uri); // 使用GET方法
			client.executeMethod(method); // 请求资源
			int status = method.getStatusCode();
			if (200 == status) {
				resultString = method.getResponseBodyAsString().trim();
				T c = JSON.parseObject(resultString, t);
				return c;
			} else {
				throw new Exception("请求失败，响应状态码" + status);
			}
		} catch (Exception e) {
			logger.error("--executeMethod failed--url=" + uri, e);
			return null;
		} finally {
			if (method != null)
				method.releaseConnection(); // 释放连接
		}
	}

}
