package com.tomtop.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 字符串加密
 * 
 * @author fcl
 *
 */
@Service
public class Encryption {
	
	private static final Logger Logger = LoggerFactory
			.getLogger(Encryption.class);

	/**
	 * 将摘要信息转换成MD5编码
	 * 
	 * @param message
	 *            摘要信息
	 * @return MD5编码之后的字符串
	 */
	public String encodeMD5(String message) {
		return encode("MD5", message);
	}

	/**
	 * 将摘要信息转换成SHA编码
	 * 
	 * @param message
	 *            摘要信息
	 * @return SHA编码之后的字符串
	 */
	public String encodeSHA(String message) {
		return encode("SHA", message);
	}

	/**
	 * 将摘要信息转换成SHA-256编码
	 * 
	 * @param message
	 *            摘要信息
	 * @return SHA-256编码之后的字符串
	 */
	public String encodeSHA256(String message) {
		return encode("SHA-256", message);
	}

	/**
	 * 将摘要信息转换成SHA-512编码
	 * 
	 * @param message
	 *            摘要信息
	 * @return SHA-512编码之后的字符串
	 */
	public String encodeSHA512(String message) {
		return encode("SHA-512", message);
	}

	private String encode(String code, String message) {
		String encode = null;
		try {
			MessageDigest md = MessageDigest.getInstance(code);
			encode = Hex.encodeHexString(md.digest(message.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			Logger.error("sha256 oceanpayment faile", e);
		}
		return encode;
	}
}