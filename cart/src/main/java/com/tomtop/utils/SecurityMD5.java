package com.tomtop.utils;

import java.security.MessageDigest;

/**
 * 字符串转换成MD5编码 
 * @author 文龙
 */
public class SecurityMD5 {
	
	/**
	 * 加密方法
	 * @param pwd
	 * @return
	 */
	public static String encode(String pwd){
		String str=null;
		try {
			str=md5Encode(PASSWORD_MIX+pwd);
		} catch (Exception e) {
			throw new RuntimeException(pwd+"加密时报错");
		}
		return str;
	}
	
	/**
	 * 验证方法
	 * @param pwd
	 * @param securityStr
	 * @return
	 */
	public static boolean verify(String pwd,String securityStr){
		boolean flag=false;
		try {
			pwd=md5Encode(PASSWORD_MIX+pwd);
			flag=pwd.equals(securityStr);
		} catch (Exception e) {
			throw new RuntimeException(pwd+"解密时报错");
		}
		return flag;
	}
	
	
	private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7","8", "9", "A", "B", "D", "D", "E", "F"};

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {
	      resultSb.append(byteToHexString(b[i]));
	    }
	    return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
	    if (n < 0){
	    	n = 256 + n;
	    }
	    int d1 = n / 16;
	    int d2 = n % 16;
	    return hexDigits[d1] + hexDigits[d2];
	}

	private static String md5Encode(String password) throws Exception {
		if(null == password){
			password = "";
		}
		MessageDigest md = MessageDigest.getInstance("MD5");
		return byteArrayToHexString(md.digest(password.getBytes()));
	}
	
	/**
	 * 混淆值
	 */
	private static final String PASSWORD_MIX = "@4!@#$%@";
	
	public static void main(String[] args) throws Exception {
		String str=md5Encode("ttltc");
		System.out.println(str);
	}
	
}
