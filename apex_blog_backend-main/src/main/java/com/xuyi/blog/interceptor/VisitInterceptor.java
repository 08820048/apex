package com.xuyi.blog.interceptor;

import com.xuyi.blog.service.VisitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 访问统计拦截器
 * 
 * @author xuyi
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VisitInterceptor implements HandlerInterceptor {

    private final VisitService visitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 只统计GET请求
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String requestUri = request.getRequestURI();
            
            // 过滤掉不需要统计的请求
            if (shouldRecordVisit(requestUri)) {
                visitService.recordVisit(request);
            }
        }
        
        return true;
    }

    /**
     * 判断是否需要记录访问
     */
    private boolean shouldRecordVisit(String requestUri) {
        // 不统计的路径
        String[] excludePaths = {
            "/favicon.ico",
            "/robots.txt",
            "/sitemap.xml",
            "/admin/",
            "/auth/",
            "/actuator/",
            "/swagger-ui/",
            "/api-docs/",
            "/error",
            "/static/",
            "/css/",
            "/js/",
            "/images/",
            "/fonts/",
            "/assets/"
        };

        for (String excludePath : excludePaths) {
            if (requestUri.startsWith(excludePath)) {
                return false;
            }
        }

        // 统计所有前台页面访问（包括API接口）
        return requestUri.startsWith("/articles") ||
               requestUri.startsWith("/portfolios") ||
               requestUri.startsWith("/friend-links") ||
               requestUri.startsWith("/categories") ||
               requestUri.startsWith("/tags") ||
               requestUri.startsWith("/search") ||
               requestUri.startsWith("/rss") ||
               requestUri.startsWith("/stats") ||
               requestUri.equals("/") ||
               requestUri.equals("/api") ||
               // 统计所有公开API访问
               (requestUri.startsWith("/api/") && !requestUri.startsWith("/api/admin/"));
    }
}
