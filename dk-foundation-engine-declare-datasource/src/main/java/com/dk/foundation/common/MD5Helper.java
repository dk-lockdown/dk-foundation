package com.dk.foundation.common;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by duguk on 2018/1/5.
 */
public class MD5Helper {

	/**
	 * md5加密
	 *
	 * @param message
	 * @return
	 */
	public static String encrypt(String message) {
		return DigestUtils.md5Hex(message);
	}

}
