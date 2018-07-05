package com.dk.foundation.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by duguk on 2018/1/5.
 */
public class MD5Helper {

	/**
	 * md5加密
	 * @param message
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static String encrypt(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		 MessageDigest md5 = MessageDigest.getInstance("MD5"); 
		 sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder(); 
		 String value=baseEncoder.encode(md5.digest(message.getBytes("utf-8"))); 
		 return value;
	}
	
}
