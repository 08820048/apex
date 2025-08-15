package com.xuyi.blog.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * IP工具类
 * 
 * @author xuyi
 */
@Slf4j
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 获取客户端真实IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = null;
        
        // 1. 检查X-Forwarded-For头（代理服务器会设置）
        ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            // X-Forwarded-For可能包含多个IP，取第一个
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
            return ip.trim();
        }
        
        // 2. 检查X-Real-IP头（Nginx代理会设置）
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }
        
        // 3. 检查Proxy-Client-IP头
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }
        
        // 4. 检查WL-Proxy-Client-IP头（WebLogic代理会设置）
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }
        
        // 5. 检查HTTP_CLIENT_IP头
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip.trim();
        }
        
        // 6. 检查HTTP_X_FORWARDED_FOR头
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            return ip.trim();
        }
        
        // 7. 最后使用request.getRemoteAddr()
        ip = request.getRemoteAddr();
        
        // 处理本地地址
        if (LOCALHOST_IPV6.equals(ip)) {
            ip = LOCALHOST_IPV4;
        }
        
        return ip;
    }

    /**
     * 检查IP是否有效
     */
    private static boolean isValidIp(String ip) {
        return ip != null && 
               !ip.isEmpty() && 
               !UNKNOWN.equalsIgnoreCase(ip);
    }

    /**
     * 检查是否为内网IP
     */
    public static boolean isInternalIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        
        // 本地回环地址
        if (LOCALHOST_IPV4.equals(ip) || LOCALHOST_IPV6.equals(ip) || "localhost".equals(ip)) {
            return true;
        }
        
        try {
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            
            int firstOctet = Integer.parseInt(parts[0]);
            int secondOctet = Integer.parseInt(parts[1]);
            
            // 10.0.0.0 - 10.255.255.255
            if (firstOctet == 10) {
                return true;
            }
            
            // 172.16.0.0 - 172.31.255.255
            if (firstOctet == 172 && secondOctet >= 16 && secondOctet <= 31) {
                return true;
            }
            
            // 192.168.0.0 - 192.168.255.255
            if (firstOctet == 192 && secondOctet == 168) {
                return true;
            }
            
            // 169.254.0.0 - 169.254.255.255 (链路本地地址)
            if (firstOctet == 169 && secondOctet == 254) {
                return true;
            }
            
        } catch (NumberFormatException e) {
            log.warn("Invalid IP format: {}", ip);
            return false;
        }
        
        return false;
    }

    /**
     * 获取IP地址的地理位置信息（简单实现）
     */
    public static String getIpLocation(String ip) {
        if (isInternalIp(ip)) {
            return "内网";
        }
        
        // 这里可以集成第三方IP地理位置服务
        // 比如：高德地图API、百度地图API、IP2Location等
        return "未知";
    }

    /**
     * 掩码IP地址（用于日志记录，保护隐私）
     */
    public static String maskIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return ip;
        }
        
        try {
            String[] parts = ip.split("\\.");
            if (parts.length == 4) {
                // 掩码最后一段：192.168.1.100 -> 192.168.1.***
                return parts[0] + "." + parts[1] + "." + parts[2] + ".***";
            }
        } catch (Exception e) {
            log.warn("Failed to mask IP: {}", ip);
        }
        
        return ip;
    }
}
