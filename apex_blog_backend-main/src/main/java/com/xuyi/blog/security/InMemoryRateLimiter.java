package com.xuyi.blog.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 基于内存的限流器
 * 
 * @author xuyi
 */
@Component
@Slf4j
public class InMemoryRateLimiter {

    private final ConcurrentHashMap<String, RequestRecord> requestRecords = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

    // 限流配置
    private static final int MAX_REQUESTS_PER_MINUTE = 60;  // 每分钟最大请求数
    private static final int MAX_REQUESTS_PER_HOUR = 1000;  // 每小时最大请求数
    private static final int CLEANUP_INTERVAL_MINUTES = 5;  // 清理间隔（分钟）

    public InMemoryRateLimiter() {
        // 定期清理过期记录
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredRecords, 
            CLEANUP_INTERVAL_MINUTES, CLEANUP_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 检查是否允许请求
     * 
     * @param clientId 客户端标识（IP地址）
     * @return true表示允许，false表示被限流
     */
    public boolean isAllowed(String clientId) {
        LocalDateTime now = LocalDateTime.now();
        RequestRecord record = requestRecords.computeIfAbsent(clientId, k -> new RequestRecord());

        synchronized (record) {
            // 清理过期的请求记录
            record.cleanupExpiredRequests(now);

            // 检查每分钟限制
            long requestsInLastMinute = record.getRequestsInLastMinute(now);
            if (requestsInLastMinute >= MAX_REQUESTS_PER_MINUTE) {
                log.warn("Rate limit exceeded for client {}: {} requests in last minute", 
                    clientId, requestsInLastMinute);
                return false;
            }

            // 检查每小时限制
            long requestsInLastHour = record.getRequestsInLastHour(now);
            if (requestsInLastHour >= MAX_REQUESTS_PER_HOUR) {
                log.warn("Rate limit exceeded for client {}: {} requests in last hour", 
                    clientId, requestsInLastHour);
                return false;
            }

            // 记录本次请求
            record.addRequest(now);
            return true;
        }
    }

    /**
     * 获取剩余请求次数
     */
    public RateLimitInfo getRateLimitInfo(String clientId) {
        LocalDateTime now = LocalDateTime.now();
        RequestRecord record = requestRecords.get(clientId);
        
        if (record == null) {
            return new RateLimitInfo(MAX_REQUESTS_PER_MINUTE, MAX_REQUESTS_PER_HOUR, 0, 0);
        }

        synchronized (record) {
            record.cleanupExpiredRequests(now);
            long requestsInLastMinute = record.getRequestsInLastMinute(now);
            long requestsInLastHour = record.getRequestsInLastHour(now);
            
            return new RateLimitInfo(
                MAX_REQUESTS_PER_MINUTE - requestsInLastMinute,
                MAX_REQUESTS_PER_HOUR - requestsInLastHour,
                requestsInLastMinute,
                requestsInLastHour
            );
        }
    }

    /**
     * 清理过期的记录
     */
    private void cleanupExpiredRecords() {
        LocalDateTime cutoff = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
        requestRecords.entrySet().removeIf(entry -> {
            RequestRecord record = entry.getValue();
            synchronized (record) {
                record.cleanupExpiredRequests(LocalDateTime.now());
                return record.isEmpty();
            }
        });
        
        log.debug("Cleaned up expired rate limit records. Current size: {}", requestRecords.size());
    }

    /**
     * 请求记录类
     */
    private static class RequestRecord {
        private final ConcurrentHashMap<LocalDateTime, Integer> requests = new ConcurrentHashMap<>();

        public void addRequest(LocalDateTime timestamp) {
            // 精确到秒
            LocalDateTime secondTimestamp = timestamp.truncatedTo(ChronoUnit.SECONDS);
            requests.merge(secondTimestamp, 1, Integer::sum);
        }

        public long getRequestsInLastMinute(LocalDateTime now) {
            LocalDateTime cutoff = now.minus(1, ChronoUnit.MINUTES);
            return requests.entrySet().stream()
                .filter(entry -> entry.getKey().isAfter(cutoff))
                .mapToLong(entry -> entry.getValue())
                .sum();
        }

        public long getRequestsInLastHour(LocalDateTime now) {
            LocalDateTime cutoff = now.minus(1, ChronoUnit.HOURS);
            return requests.entrySet().stream()
                .filter(entry -> entry.getKey().isAfter(cutoff))
                .mapToLong(entry -> entry.getValue())
                .sum();
        }

        public void cleanupExpiredRequests(LocalDateTime now) {
            LocalDateTime cutoff = now.minus(1, ChronoUnit.HOURS);
            requests.entrySet().removeIf(entry -> entry.getKey().isBefore(cutoff));
        }

        public boolean isEmpty() {
            return requests.isEmpty();
        }
    }

    /**
     * 限流信息
     */
    public static class RateLimitInfo {
        private final long remainingRequestsPerMinute;
        private final long remainingRequestsPerHour;
        private final long currentRequestsPerMinute;
        private final long currentRequestsPerHour;

        public RateLimitInfo(long remainingRequestsPerMinute, long remainingRequestsPerHour,
                           long currentRequestsPerMinute, long currentRequestsPerHour) {
            this.remainingRequestsPerMinute = Math.max(0, remainingRequestsPerMinute);
            this.remainingRequestsPerHour = Math.max(0, remainingRequestsPerHour);
            this.currentRequestsPerMinute = currentRequestsPerMinute;
            this.currentRequestsPerHour = currentRequestsPerHour;
        }

        public long getRemainingRequestsPerMinute() { return remainingRequestsPerMinute; }
        public long getRemainingRequestsPerHour() { return remainingRequestsPerHour; }
        public long getCurrentRequestsPerMinute() { return currentRequestsPerMinute; }
        public long getCurrentRequestsPerHour() { return currentRequestsPerHour; }
    }
}
