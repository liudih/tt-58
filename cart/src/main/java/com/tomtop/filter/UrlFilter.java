package com.tomtop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.tomtop.valueobjects.Constants;

/**
 * url过滤器
 * 
 * @author shuliangxing
 *
 * @date 2016年5月11日 下午6:30:19
 */
@WebFilter(filterName = "urlFilter", urlPatterns = "/*")
public class UrlFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * 统一设置访问终端标识
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// 访问终端类型, 默认电脑
		String terminalType = Constants.TERMINAL_TYPE_PC;

		HttpServletRequest req = (HttpServletRequest) request;
		String host = req.getHeader(Constants.HOST);
		// 移动端请求地址已"m."开头, 如:http://m.chicuu.com/
		if (!StringUtils.isEmpty(host)
				&& host.startsWith(Constants.HOST_MOBILE_PREFIX)) {
			terminalType = Constants.TERMINAL_TYPE_MOBILE;
		}

		req.setAttribute(Constants.TERMINAL_TYPE, terminalType);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
