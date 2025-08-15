package com.xuyi.blog.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 内存限流器测试
 *
 * @author xuyi
 */
class AntiSpamFilterTest {

    @Test
    void testRateLimiterBasicFunctionality() {
        InMemoryRateLimiter rateLimiter = new InMemoryRateLimiter();
        String clientId = "192.168.1.100";

        // 测试正常请求
        assertTrue(rateLimiter.isAllowed(clientId), "第一次请求应该被允许");

        // 获取限流信息
        InMemoryRateLimiter.RateLimitInfo info = rateLimiter.getRateLimitInfo(clientId);
        assertNotNull(info, "限流信息不应为空");
        assertEquals(59, info.getRemainingRequestsPerMinute(), "每分钟剩余请求数应为59");
        assertEquals(999, info.getRemainingRequestsPerHour(), "每小时剩余请求数应为999");
    }

    @Test
    void testMultipleRequests() {
        InMemoryRateLimiter rateLimiter = new InMemoryRateLimiter();
        String clientId = "192.168.1.101";

        // 发送多个请求
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimiter.isAllowed(clientId), "前10个请求都应该被允许");
        }

        // 检查限流信息
        InMemoryRateLimiter.RateLimitInfo info = rateLimiter.getRateLimitInfo(clientId);
        assertEquals(50, info.getRemainingRequestsPerMinute(), "每分钟剩余请求数应为50");
        assertEquals(990, info.getRemainingRequestsPerHour(), "每小时剩余请求数应为990");
    }

    @Test
    void testDifferentClients() {
        InMemoryRateLimiter rateLimiter = new InMemoryRateLimiter();
        String client1 = "192.168.1.100";
        String client2 = "192.168.1.101";

        // 不同客户端的请求应该独立计算
        assertTrue(rateLimiter.isAllowed(client1), "客户端1的请求应该被允许");
        assertTrue(rateLimiter.isAllowed(client2), "客户端2的请求应该被允许");

        // 检查各自的限流信息
        InMemoryRateLimiter.RateLimitInfo info1 = rateLimiter.getRateLimitInfo(client1);
        InMemoryRateLimiter.RateLimitInfo info2 = rateLimiter.getRateLimitInfo(client2);

        assertEquals(59, info1.getRemainingRequestsPerMinute(), "客户端1每分钟剩余请求数应为59");
        assertEquals(59, info2.getRemainingRequestsPerMinute(), "客户端2每分钟剩余请求数应为59");
    }
}
