package com.tomtop.member.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

	public static final int EXCEPTION = -999;
	public static final int NOT_DATA_RES = 0;   
	
	public static final int SUCCESS_RES = 1;   
	
	public static final int ERROR_RES = -1;   
	
	public static final int SEVEN =7;
	public static final int MEMBER_ADDRESS_OVER_ERROR = -58001;  
	
	public static final int COMMENT_LENT_ERR = -60001;   
	public static final int COMMENT_ADD_NULL_ERR = -60002;   
	public static final int COMMENT_HAVE_BEEN_ERR = -60003;   
	public static final int COMMENT_ORDER_NOT_COMPLETED_ERR = -60004; 
	public static final int COMMENT_INSERT_ERR = -60005; 
	public static final int COMMENT_ORDER_DETAIL_ERR = -60006; 
	public static final int COMMENT_ORDER_DETAIL_LISTINGID_ERR = -60007; 
	public static final int COMMENT_ORDER_DETAIL_SKU_ERR = -60008; 
	
	public static final int COLLECT_IDS_NOT_FIND = -62001;  
	
	public static final int MESSAGE_STR_ERR = -70001;   
	public static final int MESSAGE_BRO_NOT_FIND = -70002;   
	public static final int MESSAGE_IDS_NOT_FIND = -70003;   
	
	
	
	public static final String NOT_DATA_MSG_RES = "not data";   
	
	public static final String SUCCESS_MSG_RES = "success"; 
	
	public static final String ERROR_MSG_RES = "error"; 
	
	/**
	 * 验证邮箱地址是否正确 　　
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
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
	 * 替换除数字字母以外的字符为“>” or "<"
	 * 
	 * @param str
	 * @return
	 */
	public static String checkSpecialChar(String str){
		if(str.indexOf(">") != -1){
			str = str.replaceAll(">", "");
		}
		if(str.indexOf("<") != -1){
			str = str.replaceAll("<", "");
		}
		if(str.indexOf("\\>") != -1){
			str = str.replaceAll("\\>", "");
		}
		if(str.indexOf("\\<") != -1){
			str = str.replaceAll("\\<", "");
		}
		if(str.indexOf("%3C") != -1){
			str = str.replaceAll("%3C", "");
		}
		if(str.indexOf("%3E") != -1){
			str = str.replaceAll("%3E", "");
		}
		return str;
	}
}
