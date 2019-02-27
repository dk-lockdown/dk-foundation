package com.dk.foundation.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by duguk on 2018/1/5.
 */
public class AESHelper {
	final static Logger logger = LoggerFactory.getLogger(AESHelper.class);

	/**
	 * 把加密加密串返回为url parameter map参数
	 *
	 * @param sign 加密/签名串
	 * @param sKey 密钥
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getParamMapWithAES(String sign, String sKey) throws Exception {
		String decodeString = decryptWithAES(sign, sKey);
		return getUrlParams(decodeString);
	}

	/**
	 * url参数转换成map
	 *
	 * @param param
	 * @return
	 */
	private static Map<String, String> getUrlParams(String param) {
		logger.info("transfer [" + param + "] to map");
		Map<String, String> map = new HashMap<String, String>(0);
		if (StringUtils.isBlank(param)) {
			return map;
		}
		String[] params = param.split("&");
		for (String param1 : params) {
			String[] p = param1.split("=");
			if (p.length == 2) {
				map.put(p[0], p[1]);
			}
		}
		return map;
	}

	/**
	 * 针对AES算法字符串进行解密
	 *
	 * @param sSrc
	 * @param sKey
	 * @return
	 */
	public static String decryptWithAES(String sSrc, String sKey) {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				logger.warn("decryptWithAES->Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				logger.warn("decryptWithAES->Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes(StandardCharsets.US_ASCII);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			// 先用bAES64解密
			byte[] encrypted1 = org.apache.commons.codec.binary.Base64.decodeBase64(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				return new String(original);
			} catch (Exception e) {
				logger.error("decryptWithAES", e);
				return null;
			}
		} catch (Exception ex) {
			logger.error("decryptWithAES", ex);
			return null;
		}
	}

}
