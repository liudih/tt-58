package com.tomtop.loyalty.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtils {
	private static Logger logger = LoggerFactory.getLogger(CookieUtils.class);
	public static final String DOMAIN = "tomtop.com";

	public static void setCookie(String key, String value,
			HttpServletRequest request, HttpServletResponse response) {
		if (null == request || null == response) {
			return;
		}
		String host = request.getHeader("Host");
		logger.debug("Host:{}", host);

		if (host != null && host.indexOf(DOMAIN) != -1) {
			logger.debug("domain:{}", DOMAIN);
			Cookie cookie = new Cookie(key, value);
			cookie.setDomain("." + DOMAIN);
			cookie.setPath("/");
			cookie.setMaxAge(365 * 24 * 3600);
			response.addCookie(cookie);
		} else {
			Cookie cookie = new Cookie(key, value);
			cookie.setPath("/");
			cookie.setMaxAge(365 * 24 * 3600);
			response.addCookie(cookie);
		}
	}

	public static void removeCookie(String key, HttpServletRequest request,
			HttpServletResponse response) {
		if (null == request || null == response) {
			return;
		}
		String host = request.getHeader("Host");
		logger.debug("Host:{}", host);

		if (host != null && host.indexOf(DOMAIN) != -1) {
			logger.debug("domain:{}", DOMAIN);
			Cookie cookie = new Cookie(key, "");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			cookie.setDomain("." + DOMAIN);

			response.addCookie(cookie);
		} else {
			Cookie cookie = new Cookie(key, "");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	public static String getCookie(String key, HttpServletRequest request,
			HttpServletResponse response) {
		if (null == request || null == response || StringUtils.isEmpty(key)) {
			return "";
		}
		Map<String, String> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(key)) {
			String cookieValue = cookieMap.get(key);
			return cookieValue;
		} else {
			return "";
		}
	}

	public static Map<String, String> ReadCookieMap(HttpServletRequest request) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}
		return cookieMap;
	}

}
