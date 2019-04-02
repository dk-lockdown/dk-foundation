package com.dk.foundation.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 62进制数字
 */
public class Num62 {
	
	final static Map<Character, Integer> digitMap = new HashMap<Character, Integer>(); 
	/**
	 * 62个字母和数字，含大小写 
	 */
	public static final char[] N62_CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };
	/**
	 * 36个小写字母和数字
	 */
	public static final char[] N36_CHARS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z' };
	/**
	 * 长整型用N36表示的最大长度
	 */
	public static final int LONG_N36_LEN = 13;
	/**
	 * 长整型用N62表示的最大长度
	 */
	public static final int LONG_N62_LEN = 11;
	
	/** 
     * 支持的最大进制数 
     */  
    public static final int MAX_RADIX = N62_CHARS.length;  
  
    /** 
     * 支持的最小进制数 
     */  
    public static final int MIN_RADIX = 2;  
	
	static {  
        for (int i = 0; i < N62_CHARS.length; i++) {  
            digitMap.put(N62_CHARS[i], (int) i);  
        }  
    }  
	/**
	 * 长整型转换成字符串
	 * 
	 * @param l
	 * @param chars
	 * @return
	 */
	private static StringBuilder longToNBuf(long l, char[] chars) {
		int upgrade = chars.length;
		StringBuilder result = new StringBuilder();
		int last;
		while (l > 0) {
			last = (int) (l % upgrade);
			result.append(chars[last]);
			l /= upgrade;
		}
		return result;
	}

	/**
	 * 长整数转换成N62
	 * 
	 * @param l
	 * @return
	 */
	public static String longToN62(long l) {
		return longToNBuf(l, N62_CHARS).reverse().toString();
	}

	/**
	 * 长整型转换成N36
	 * 
	 * @param l
	 * @return
	 */
	public static String longToN36(long l) {
		return longToNBuf(l, N36_CHARS).reverse().toString();
	}

	/**
	 * 长整数转换成N62
	 * 
	 * @param l
	 * @param length
	 *            如不足length长度，则补足0。
	 * @return
	 */
	public static String longToN62(long l, int length) {
		StringBuilder sb = longToNBuf(l, N62_CHARS);
		for (int i = sb.length(); i < length; i++) {
			sb.append('0');
		}
		return sb.reverse().toString();
	}

	/**
	 * 长整型转换成N36
	 * 
	 * @param l
	 * @param length
	 *            如不足length长度，则补足0。
	 * @return
	 */
	public static String longToN36(long l, int length) {
		StringBuilder sb = longToNBuf(l, N36_CHARS);
		for (int i = sb.length(); i < length; i++) {
			sb.append('0');
		}
		return sb.reverse().toString();
	}

	/**
	 * N62转换成整数
	 * 
	 * @param n62
	 * @return
	 */
	public static long n62ToLong(String n62) {
		return nToLong(n62, N62_CHARS);
	}

	/**
	 * N36转换成整数
	 * 
	 * @param n36
	 * @return
	 */
	public static long n36ToLong(String n36) {
		return nToLong(n36, N36_CHARS);
	}

	private static long nToLong(String s, char[] chars) {
		char[] nc = s.toCharArray();
		long result = 0;
		long pow = 1;
		for (int i = nc.length - 1; i >= 0; i--, pow *= chars.length) {
			int n = findNIndex(nc[i], chars);
			result += n * pow;
		}
		return result;
	}

	private static int findNIndex(char c, char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			if (c == chars[i]) {
				return i;
			}
		}
		throw new RuntimeException("N62(N36)非法字符：" + c);
	}
	
	/** 
     * 将长整型数值转换为指定的进制数（最大支持62进制，字母数字已经用尽） 
     *  
     * @param i 
     * @param radix 
     * @return 
     */  
    public static String toString(long i, int radix) {  
        if (radix < MIN_RADIX || radix > MAX_RADIX)  
            radix = 10;  
        if (radix == 10)  
            return Long.toString(i);  
  
        final int size = 65;  
        int charPos = 64;  
  
        char[] buf = new char[size];  
        boolean negative = (i < 0);  
  
        if (!negative) {  
            i = -i;  
        }  
  
        while (i <= -radix) {  
            buf[charPos--] = N62_CHARS[(int) (-(i % radix))];  
            i = i / radix;  
        }  
        buf[charPos] = N62_CHARS[(int) (-i)];  
  
        if (negative) {  
            buf[--charPos] = '-';  
        }  
  
        return new String(buf, charPos, (size - charPos));  
    }  
  
    static NumberFormatException forInputString(String s) {  
        return new NumberFormatException("For input string: \"" + s + "\"");  
    }  
  
    /** 
     * 将字符串转换为长整型数字 
     *  
     * @param s 
     *            数字字符串 
     * @param radix 
     *            进制数 
     * @return 
     */  
    public static long toNumber(String s, int radix) {  
        if (s == null) {  
            throw new NumberFormatException("null");  
        }  
  
        if (radix < MIN_RADIX) {  
            throw new NumberFormatException("radix " + radix  
                    + " less than Numbers.MIN_RADIX");  
        }  
        if (radix > MAX_RADIX) {  
            throw new NumberFormatException("radix " + radix  
                    + " greater than Numbers.MAX_RADIX");  
        }  
  
        long result = 0;  
        boolean negative = false;  
        int i = 0, len = s.length();  
        long limit = -Long.MAX_VALUE;  
        long multmin;  
        Integer digit;  
  
        if (len > 0) {  
            char firstChar = s.charAt(0);  
            if (firstChar < '0') {  
                if (firstChar == '-') {  
                    negative = true;  
                    limit = Long.MIN_VALUE;  
                } else if (firstChar != '+')  
                    throw forInputString(s);  
  
                if (len == 1) {  
                    throw forInputString(s);  
                }  
                i++;  
            }  
            multmin = limit / radix;  
            while (i < len) {  
                digit = digitMap.get(s.charAt(i++));  
                if (digit == null) {  
                    throw forInputString(s);  
                }  
                if (digit < 0) {  
                    throw forInputString(s);  
                }  
                if (result < multmin) {  
                    throw forInputString(s);  
                }  
                result *= radix;  
                if (result < limit + digit) {  
                    throw forInputString(s);  
                }  
                result -= digit;  
            }  
        } else {  
            throw forInputString(s);  
        }  
        return negative ? result : -result;  
    } 
	
	
	public static void main(String[] args) {
		System.out.println(longToN62(Long.MAX_VALUE));
	}
}
