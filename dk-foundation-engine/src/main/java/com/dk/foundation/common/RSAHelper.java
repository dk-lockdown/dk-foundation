package com.dk.foundation.common;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Created by duguk on 2018/1/5.
 */
public class RSAHelper {
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * 对url 参数进行签名，生成sign
	 *
	 * @param paramMap   url参数map
	 * @param privatekey 私钥
	 * @param exclude    需要排除，不参与签名的参数
	 * @return
	 */
	public static String getSignWithParamMap(Map<String, String> paramMap, String privatekey, String... exclude) {
		//过滤不需要进行签名的参数
		Map<String, String> filterParamMap = paraFilter(paramMap);
		if (exclude != null) {
			for (String key : exclude) {
				filterParamMap.remove(key);
			}
		}
		//按照顺序
		String waitingForSign = createLinkString(filterParamMap);
		return sign(waitingForSign, privatekey, "utf-8");
	}

	/**
	 * 除去数组中的空值和签名参数
	 *
	 * @param sArray 签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || "".equals(value) || "sign".equalsIgnoreCase(key) || "sign_type".equalsIgnoreCase(key)
					|| "channel".equalsIgnoreCase(key) || "device".equalsIgnoreCase(key)
					|| "description".equalsIgnoreCase(key)) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 *
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		StringBuilder preStr = new StringBuilder();

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			// 拼接时，不包括最后一个&字符
			if (i == keys.size() - 1) {
				preStr.append(preStr + key + "=" + value);
			} else {
				preStr.append(preStr + key + "=" + value + "&");
			}
		}
		return preStr.toString();
	}

	/**
	 * 用rsa进行签名
	 *
	 * @param content
	 * @param privateKey
	 * @param inputCharset
	 * @return
	 */
	public static String sign(String content, String privateKey, String inputCharset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(inputCharset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * RSA验签名检查
	 *
	 * @param content       待签名数据
	 * @param sign          签名值
	 * @param publicKey    支付宝公钥
	 * @param inputCharset 编码格式
	 * @return 布尔值
	 * @throws Exception
	 */
	public static boolean verify(String content, String sign, String publicKey, String inputCharset) {

		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(inputCharset));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static class Base64 {

		static private final int BASELENGTH = 128;
		static private final int LOOKUPLENGTH = 64;
		static private final int TWENTYFOURBITGROUP = 24;
		static private final int EIGHTBIT = 8;
		static private final int SIXTEENBIT = 16;
		static private final int FOURBYTE = 4;
		static private final int SIGN = -128;
		static private final char PAD = '=';
		static private final boolean F_DEBUG = false;
		static final private byte[] BASE64_ALPHABET = new byte[BASELENGTH];
		static final private char[] LOOK_UP_BASE64_ALPHABET = new char[LOOKUPLENGTH];

		static {
			for (int i = 0; i < BASELENGTH; ++i) {
				BASE64_ALPHABET[i] = -1;
			}
			for (int i = 'Z'; i >= 'A'; i--) {
				BASE64_ALPHABET[i] = (byte) (i - 'A');
			}
			for (int i = 'z'; i >= 'a'; i--) {
				BASE64_ALPHABET[i] = (byte) (i - 'a' + 26);
			}

			for (int i = '9'; i >= '0'; i--) {
				BASE64_ALPHABET[i] = (byte) (i - '0' + 52);
			}

			BASE64_ALPHABET['+'] = 62;
			BASE64_ALPHABET['/'] = 63;

			for (int i = 0; i <= 25; i++) {
				LOOK_UP_BASE64_ALPHABET[i] = (char) ('A' + i);
			}

			for (int i = 26, j = 0; i <= 51; i++, j++) {
				LOOK_UP_BASE64_ALPHABET[i] = (char) ('a' + j);
			}

			for (int i = 52, j = 0; i <= 61; i++, j++) {
				LOOK_UP_BASE64_ALPHABET[i] = (char) ('0' + j);
			}
			LOOK_UP_BASE64_ALPHABET[62] = (char) '+';
			LOOK_UP_BASE64_ALPHABET[63] = (char) '/';

		}

		private static boolean isWhiteSpace(char octect) {
			return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
		}

		private static boolean isPad(char octect) {
			return (octect == PAD);
		}

		private static boolean isData(char octect) {
			return (octect < BASELENGTH && BASE64_ALPHABET[octect] != -1);
		}

		/**
		 * Encodes hex octects into Base64
		 *
		 * @param binaryData Array containing binaryData
		 * @return Encoded Base64 array
		 */
		public static String encode(byte[] binaryData) {

			if (binaryData == null) {
				return null;
			}

			int lengthDataBits = binaryData.length * EIGHTBIT;
			if (lengthDataBits == 0) {
				return "";
			}

			int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
			int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
			int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
			char[] encodedData = new char[numberQuartet * 4];

			byte k, l, b1, b2, b3;

			int encodedIndex = 0;
			int dataIndex = 0;
			if (F_DEBUG) {
				System.out.println("number of triplets = " + numberTriplets);
			}

			for (int i = 0; i < numberTriplets; i++) {
				b1 = binaryData[dataIndex++];
				b2 = binaryData[dataIndex++];
				b3 = binaryData[dataIndex++];

				if (F_DEBUG) {
					System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
				}

				l = (byte) (b2 & 0x0f);
				k = (byte) (b1 & 0x03);

				byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
				byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
				byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

				if (F_DEBUG) {
					System.out.println("val2 = " + val2);
					System.out.println("k4   = " + (k << 4));
					System.out.println("vak  = " + (val2 | (k << 4)));
				}

				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val1];
				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val2 | (k << 4)];
				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[(l << 2) | val3];
				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[b3 & 0x3f];
			}

			// form integral number of 6-bit groups
			if (fewerThan24bits == EIGHTBIT) {
				b1 = binaryData[dataIndex];
				k = (byte) (b1 & 0x03);
				if (F_DEBUG) {
					System.out.println("b1=" + b1);
					System.out.println("b1<<2 = " + (b1 >> 2));
				}
				byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val1];
				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[k << 4];
				encodedData[encodedIndex++] = PAD;
				encodedData[encodedIndex++] = PAD;
			} else if (fewerThan24bits == SIXTEENBIT) {
				b1 = binaryData[dataIndex];
				b2 = binaryData[dataIndex + 1];
				l = (byte) (b2 & 0x0f);
				k = (byte) (b1 & 0x03);

				byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
				byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val1];
				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[val2 | (k << 4)];
				encodedData[encodedIndex++] = LOOK_UP_BASE64_ALPHABET[l << 2];
				encodedData[encodedIndex++] = PAD;
			}

			return new String(encodedData);
		}

		/**
		 * Decodes Base64 data into octects
		 *
		 * @param encoded string containing Base64 data
		 * @return Array containind decoded data.
		 */
		public static byte[] decode(String encoded) {

			if (encoded == null) {
				return null;
			}

			char[] base64Data = encoded.toCharArray();
			// remove white spaces
			int len = removeWhiteSpace(base64Data);

			if (len % FOURBYTE != 0) {
				// should be divisible by four
				return null;
			}

			int numberQuadruple = (len / FOURBYTE);

			if (numberQuadruple == 0) {
				return new byte[0];
			}

			byte b1, b2, b3, b4;
			char d1, d2, d3, d4;

			int i = 0;
			int encodedIndex = 0;
			int dataIndex = 0;
			byte[] decodedData = new byte[(numberQuadruple) * 3];

			for (; i < numberQuadruple - 1; i++) {

				if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))
						|| !isData((d3 = base64Data[dataIndex++])) || !isData((d4 = base64Data[dataIndex++]))) {
					return null;
				} // if found "no data" just return null

				b1 = BASE64_ALPHABET[d1];
				b2 = BASE64_ALPHABET[d2];
				b3 = BASE64_ALPHABET[d3];
				b4 = BASE64_ALPHABET[d4];

				decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
				decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
			}

			if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))) {
				return null;// if found "no data" just return null
			}

			b1 = BASE64_ALPHABET[d1];
			b2 = BASE64_ALPHABET[d2];

			d3 = base64Data[dataIndex++];
			d4 = base64Data[dataIndex++];
			if (!isData((d3)) || !isData((d4))) {// Check if they are PAD
				// characters
				if (isPad(d3) && isPad(d4)) {
					if ((b2 & 0xf) != 0) // last 4 bits should be zero
					{
						return null;
					}
					byte[] tmp = new byte[i * 3 + 1];
					System.arraycopy(decodedData, 0, tmp, 0, i * 3);
					tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
					return tmp;
				} else if (!isPad(d3) && isPad(d4)) {
					b3 = BASE64_ALPHABET[d3];
					if ((b3 & 0x3) != 0) // last 2 bits should be zero
					{
						return null;
					}
					byte[] tmp = new byte[i * 3 + 2];
					System.arraycopy(decodedData, 0, tmp, 0, i * 3);
					tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
					tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
					return tmp;
				} else {
					return null;
				}
			} else { // No PAD e.g 3cQl
				b3 = BASE64_ALPHABET[d3];
				b4 = BASE64_ALPHABET[d4];
				decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
				decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
				decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

			}

			return decodedData;
		}

		/**
		 * remove WhiteSpace from MIME containing encoded Base64 data.
		 *
		 * @param data the byte array of base64 data (with WS)
		 * @return the new length
		 */
		private static int removeWhiteSpace(char[] data) {
			if (data == null) {
				return 0;
			}

			// count characters that's not whitespace
			int newSize = 0;
			int len = data.length;
			for (int i = 0; i < len; i++) {
				if (!isWhiteSpace(data[i])) {
					data[newSize++] = data[i];
				}
			}
			return newSize;
		}
	}
}
