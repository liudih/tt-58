package com.tomtop.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtils {
	private static final Logger Logger = LoggerFactory
			.getLogger(CookieUtils.class);
	
	public static final Pattern PATTERN = Pattern
			.compile("(\\w+\\.)*([\\w]+)\\.");
	

	public static void setCookie(String key, String value) {
		HttpServletRequest request = Request.currentRequest();
		HttpServletResponse response = Request.currentResponse();

		if (request == null) {
			return;
		}
		String domain = null;

		String host = request.getHeader("Host");

		Matcher matcher = PATTERN.matcher(host);
		if (matcher.find()) {
			domain = "." + matcher.group(2) + ".com";
		}

		Logger.debug("Host:{}", host);
		Logger.debug("domain:{}", domain);

		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(365 * 24 * 3600);
		cookie.setPath("/");

		if (domain != null) {
			Logger.debug("domain:{}", domain);
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}

	public static void removeCookie(String key) {

		HttpServletRequest request = Request.currentRequest();
		HttpServletResponse response = Request.currentResponse();
		if (request == null) {
			return;
		}

		String domain = null;
		String host = request.getHeader("Host");
		Logger.debug("Host:{}", host);

		Matcher matcher = PATTERN.matcher(host);
		if (matcher.find()) {
			domain = "." + matcher.group(2) + ".com";
		}

		Cookie cookie = new Cookie(key, "");
		cookie.setMaxAge(-1);
		cookie.setPath("/");

		if (domain != null) {
			Logger.debug("domain:{}", domain);
			cookie.setDomain(domain);
		}

		response.addCookie(cookie);
	}

	public static String getCookie(String key) {
		HttpServletRequest request = Request.currentRequest();
		if (null == request || key == null) {
			return "";
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				String name = c.getName();
				if (name.equals(key)) {
					return c.getValue();
				}
			}
		}
		return "";
	}

}
