package com.tomtop.member.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class ClientUtil {
	
	//private static Logger logger = LoggerFactory.getLogger("ClientUtil");
	
	/**
	 * post提交
	 * 
	 * @param url
	 * @return
	 */
	public static String postUrl(String url, String content) {
		//url = url + "?";
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		post.getParams().setContentCharset("UTF-8");
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		String[] paramArray = content.split("&");
		for (int i = 0; i < paramArray.length; i++) {
			String equalString = paramArray[i];
			String[] keyArray = equalString.split("=");
			String key = keyArray[0];
			String value = keyArray[1];
			post.addParameter(key, value);
		}
		String result = "";
		try {
			int statusCode = httpClient.executeMethod(post);
			if (statusCode == HttpStatus.SC_OK) {
				result = post.getResponseBodyAsString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			client.getHttpConnectionManager().getParams().setSoTimeout(10000);
			method = new GetMethod(uri); // 使用GET方法
			client.executeMethod(method); // 请求资源
			int status = method.getStatusCode();
			if (200 == status) {
				return method.getResponseBodyAsString().trim();
			} else {
				throw new Exception("请求失败，响应状态码" + status + ",reponse:"+method.getResponseBodyAsString().trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	public static String paypalGet(String uri,String headerValue) {
		HttpMethod method = null;
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
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
}
