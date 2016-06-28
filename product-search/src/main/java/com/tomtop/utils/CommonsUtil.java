package com.tomtop.utils;

import java.io.FileWriter;
import java.io.IOException;

public class CommonsUtil {

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
		return str;
	}
	
	/**
	 * check 字符长度
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkLength(String str,Integer length){
		if(str.length() > length){
			return false;
		}
		return true;
	}
	
	/**
	 * check 关键字特殊的转换
	 * 
	 * @param str
	 * @return
	 */
	public static String checkKeyWord(String str){
		if(str != null){
			if(str.indexOf(">") != -1){
				str = str.replaceAll(">", "");
			}
			if(str.indexOf("<") != -1){
				str = str.replaceAll("<", "");
			}
			if(str.indexOf("~D~") != -1){
				str = str.replaceAll("~D~", ".");
			}
			if(str.indexOf("~J~") != -1){
				str = str.replaceAll("~J~", "#");
			}
		}else{
			str = "";
		}
		return str.trim();
	}
	
	/**
	 * 替换除数字字母以外的字符为“-”
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceNoEnStr(String str) {
		if(str != null && !"".equals(str)){
			str = str.replaceAll("[\\pP\\p{Punct}.°. ]", " ").trim().replaceAll("[ ]+", "-");
			str = checkSpecialChar(str);
		}else{
			str = "";
		}
		return str;
	}
	
	public String filterDangerString(String value) {
        if (value == null) {
            return null;
        }
        value = value.replaceAll("\\|", "");
        value = value.replaceAll("&", "&");
        value = value.replaceAll(";", "");
        value = value.replaceAll("@", "");
        value = value.replaceAll("'", "");
        value = value.replaceAll("\"", "");
        value = value.replaceAll("\\'", "");
        value = value.replaceAll("<", "<");
        value = value.replaceAll(">", ">");
        value = value.replaceAll("\\(", "");
        value = value.replaceAll("\\)", "");
        value = value.replaceAll("\\+", "");
        value = value.replaceAll("\r", "");
        value = value.replaceAll("\n", "");
        value = value.replaceAll("script", "");
        value = value.replaceAll("'", "");
        value = value.replaceAll(">", "");
        value = value.replaceAll("<", "");
        value = value.replaceAll("=", "");
        value = value.replaceAll("/", "");
        return value;
    }
	
	public static void fileInput(String path,String value){
		 FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(path,true);
		    fileWriter.write(value + "\r\n");
		    fileWriter.flush();
		    fileWriter.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 多语言颜色特殊处理
	 * 
	 * @param str
	 * @return
	 */
	public static String replceKeyName(String str){
		str = str.trim();
		if("цвет".equals(str) || "farbe".equals(str) || "couleur".equals(str) || "colore".equals(str) || "カラー".equals(str) || "cor".equals(str)){
			str = "color";
		}
		return str;
	}
}
