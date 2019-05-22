package com.dk.foundation.common;

import java.nio.charset.Charset;

public class DesUtil {
    /**
     * 加密
     * @param srcStr
     * @param charset
     * @param sKey
     * @return
     */
    public static String encrypt(String srcStr, Charset charset, String sKey) {
        byte[] src = srcStr.getBytes(charset);
        byte[] buf = Des.encrypt(src, sKey);
        return Des.parseByte2HexStr(buf);
    }

    /**
     * 解密
     *
     * @param hexStr
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String hexStr, Charset charset, String sKey) throws Exception {
        byte[] src = Des.parseHexStr2Byte(hexStr);
        byte[] buf = Des.decrypt(src, sKey);
        return new String(buf, charset);
    }
}
