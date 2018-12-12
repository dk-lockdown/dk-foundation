package com.dk.foundation.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by duguk on 2018/1/5.
 */
public class MD5Helper {

	/**
	 * md5加密
	 *
	 * @param message
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String encrypt(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		Base64.Encoder baseEncoder = Base64.getEncoder();
		String value = baseEncoder.encode(md5.digest(message.getBytes("utf-8"))).toString();
		return value;
	}

}
