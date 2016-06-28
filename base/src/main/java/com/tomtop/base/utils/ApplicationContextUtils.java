package com.tomtop.base.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * appliction 工具类，(获取application上下文)
 * 
 * @author liulj
 *
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

	/**
	 * appliction上下文
	 */
	private static ApplicationContext applicationContext;

	public final void setApplicationContext(
			ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		ApplicationContextUtils.applicationContext = applicationContext;
	}

	/**
	 * 获取application上下文
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
