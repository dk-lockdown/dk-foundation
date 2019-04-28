package com.dk.foundation.common; /**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by duguk on 2018/1/5.
 */
public class IPUtils {
	private static Logger logger = LoggerFactory.getLogger(IPUtils.class);

	/**
	 * 获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
    	String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
        	logger.error("com.dk.foundation.common.IPUtils ERROR ", e);
        }
        
//        //使用代理，则获取第一个IP地址
//        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
//			if(ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}
        
        return ip;
    }

    public static String getLocalIp() {
        InetAddress address = null;
        try {
            // 如果是Windows操作系统
            if (isWindowsOS()) {
                address = InetAddress.getLocalHost();
            }
            // 如果是Linux操作系统
            else {
                //执行shell命令获取正确ip
                String linuxLocalIp = getLinuxLocalIp();
                if (StringUtils.isNotBlank(linuxLocalIp)) {
                    logger.info("通过shell命令获取linux本地ip成功：{}", linuxLocalIp);
                    return linuxLocalIp;
                }
                //获取linuxlocalip方式二
                boolean findIP = false;
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    if (findIP) {
                        break;
                    }
                    NetworkInterface ni = netInterfaces.nextElement();
                    // ----------特定情况，可以考虑用ni.getName判断
                    // 遍历所有ip
                    Enumeration<InetAddress> addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        address = addresses.nextElement();
                        /**
                         * 127.开头的都是lookback地址
                         */
                        if (address.isSiteLocalAddress() && !address.isLoopbackAddress()
                                && !address.getHostAddress().contains(":")) {
                            findIP = true;
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException("获取局域网ip失败:" + e.getMessage());
        }
        if (null == address) {
            throw new RuntimeException("获取局域网ip失败:InetAddress == null");
        }
        logger.info("方式二获取linux本地ip成功：{}", address.getHostAddress());
        return address.getHostAddress();

    }

    public static Long ipToLong(String ipStr) {
        Long ip = 0L;
        String[] numbers = ipStr.split("\\.");
        for (int i = 0; i < numbers.length; i++) {
            ip = ip << 8 | Integer.parseInt(numbers[i]);
        }
        return ip;

    }

    public static boolean isWindowsOS() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") > -1;
    }

    private static final String GET_LOCAL_IP_CMD = "/sbin/ifconfig -a|grep inet|grep -v 127.0.0.1|grep -v 0.0.0.0|grep -v inet6|awk '{print $2}'|tr -d addr:";

    private static String getLinuxLocalIp() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"sh","-c",GET_LOCAL_IP_CMD});
            proc.waitFor(); //阻塞，直到上述命令执行完
            String temp;
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((temp = br.readLine()) != null){
                break;
            }
            br.close();
            return temp;
        } catch (IOException e1) {
            throw new RuntimeException("获取linux本地ip出错：IOException",e1);
        } catch (InterruptedException e2) {
            throw new RuntimeException("获取linux本地ip出错：InterruptedException",e2);
        }
    }
}
