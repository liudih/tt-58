package com.tomtop.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class CommonUtils {

	public static final int NOT_DATA_RES = 0;

	public static final int SUCCESS_RES = 1;

	public static final int ERROR_RES = -1;

	public static final String NOT_DATA_MSG_RES = "not data";

	public static final String SUCCESS_MSG_RES = "success";

	public static final String ERROR_MSG_RES = "error";

	public static final int ZERO = 0;

	public static final int ONE = 1;

	public static final int CODE_NULL = -70001;
	public static final int NAME_NULL = -70002;
	public static final int TYPE_NULL = -70003;
	public static final int URL_NULL = -70004;
	public static final int START_NOT_RULE = -70005;
	public static final int CODE_EXISTED = -70006;
	public static final int METHOD_WRONG = -70006;

	/**
	 * 错误提示编码:参数为空
	 */
	public static final String ERROR_CODE_PARAM_IS_NULL = "-10000";

	public static final String ADMAIN = "admin";

	/**
	 * 验证邮箱地址是否正确 　　
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	/**
	 * 从下标0开始截取字符串到指定长度
	 * 
	 * @param str
	 *            源字符串
	 * @param length
	 *            截取的长度
	 * @return 截取后的结果
	 */
	public static String substring(String str, int length) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		if (str.length() > length) {
			return str.substring(0, length);
		}
		return str;
	}

	/**
	 * 过滤字符串中特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		if (StringUtils.isEmpty(str)) {
			return str;
		}
		// 清除掉所有特殊字符
		// String
		// regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		String regEx = "[<>]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	/**
	 * 过滤特殊字符串并截取到指定长度
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String filterAndSubstring(String str, int length) {
		return substring(stringFilter(str), length);
	}

}
