package com.tomtop.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tomtop.valueobjects.base.LoginContext;

public class Request {

	private static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> responses = new ThreadLocal<HttpServletResponse>();
	private static ThreadLocal<LoginContext> loginCtxs = new ThreadLocal<LoginContext>();

	public static HttpServletRequest currentRequest() {
		return requests.get();
	}

	public static void setRequest(HttpServletRequest request) {
		requests.set(request);
	}

	public static HttpServletResponse currentResponse() {
		return responses.get();
	}

	public static void setResponse(HttpServletResponse response) {
		responses.set(response);
	}

	public static void setLoginCtx(LoginContext loginCtx) {
		loginCtxs.set(loginCtx);
	}

	public static LoginContext currentLoginCtx() {
		return loginCtxs.get();
	}
	
	
	public static void destroy(){
		loginCtxs.remove();
	}
}
