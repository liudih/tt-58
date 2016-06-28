package com.tomtop.configuration;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tomtop.utils.Request;

public class ApplicationContextHeaderFilter extends OncePerRequestFilter {

	Logger logger = LoggerFactory
			.getLogger(ApplicationContextHeaderFilter.class);

	private static final Pattern PATTERN = Pattern.compile(".+(js|css|png|gif|jpg)$");

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getRequestURI();
		Matcher m = PATTERN.matcher(path);
		boolean isMatch = m.matches();
		if (!isMatch) {
			Request.setRequest(request);
			Request.setResponse(response);
		}

		filterChain.doFilter(request, response);
	}

}
