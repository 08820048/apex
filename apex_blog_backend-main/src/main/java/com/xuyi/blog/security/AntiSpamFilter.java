package com.xuyi.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.util.IpUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 防刷过滤器
 * 
 * @author xuyi
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AntiSpamFilter extends OncePerRequestFilter {

    private final InMemoryRateLimiter rateLimiter;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String clientIp = IpUtils.getClientIp(request);
        String requestUri = request.getRequestURI();
        
        // 跳过静态资源和健康检查
        if (shouldSkipRateLimit(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 检查限流
        if (!rateLimiter.isAllowed(clientIp)) {
            handleRateLimitExceeded(response, clientIp);
            return;
        }

        // 添加限流信息到响应头
        addRateLimitHeaders(response, clientIp);
        
        filterChain.doFilter(request, response);
    }

    /**
     * 判断是否跳过限流检查
     */
    private boolean shouldSkipRateLimit(String requestUri) {
        return requestUri.startsWith("/api/actuator") ||
               requestUri.startsWith("/api/swagger") ||
               requestUri.startsWith("/api/api-docs") ||
               requestUri.endsWith(".css") ||
               requestUri.endsWith(".js") ||
               requestUri.endsWith(".png") ||
               requestUri.endsWith(".jpg") ||
               requestUri.endsWith(".jpeg") ||
               requestUri.endsWith(".gif") ||
               requestUri.endsWith(".ico") ||
               requestUri.endsWith(".svg");
    }

    /**
     * 处理限流超出的情况
     */
    private void handleRateLimitExceeded(HttpServletResponse response, String clientIp) throws IOException {
        log.warn("Rate limit exceeded for IP: {}", clientIp);
        
        response.setStatus(429); // Too Many Requests
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        ApiResponse<Void> apiResponse = ApiResponse.error(429, "请求过于频繁，请稍后再试");
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    /**
     * 添加限流信息到响应头
     */
    private void addRateLimitHeaders(HttpServletResponse response, String clientIp) {
        InMemoryRateLimiter.RateLimitInfo info = rateLimiter.getRateLimitInfo(clientIp);
        
        response.setHeader("X-RateLimit-Remaining-Minute", String.valueOf(info.getRemainingRequestsPerMinute()));
        response.setHeader("X-RateLimit-Remaining-Hour", String.valueOf(info.getRemainingRequestsPerHour()));
        response.setHeader("X-RateLimit-Limit-Minute", "60");
        response.setHeader("X-RateLimit-Limit-Hour", "1000");
    }
}
