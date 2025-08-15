package com.xuyi.blog.interceptor;

import com.xuyi.blog.dto.QueryStatsDTO;
import com.xuyi.blog.service.QueryStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 查询统计Web拦截器
 * 在请求开始和结束时进行统计
 * 
 * @author xuyi
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QueryStatsWebInterceptor implements HandlerInterceptor {
    
    private final QueryStatsService queryStatsService;
    
    // 查询统计属性名
    private static final String QUERY_STATS_ATTR = "queryStats";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跳过静态资源和健康检查
        String requestURI = request.getRequestURI();
        if (shouldSkipStats(requestURI)) {
            return true;
        }
        
        try {
            // 开始统计
            queryStatsService.startRequestStats();
            log.debug("开始查询统计: {} {}", request.getMethod(), requestURI);
        } catch (Exception e) {
            log.error("启动查询统计失败", e);
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 跳过静态资源和健康检查
        String requestURI = request.getRequestURI();
        if (shouldSkipStats(requestURI)) {
            return;
        }
        
        try {
            // 完成统计
            QueryStatsDTO stats = queryStatsService.finishRequestStats();
            
            if (stats != null) {
                // 将统计信息添加到响应头（可选）
                response.setHeader("X-Query-Count", String.valueOf(stats.getTotalQueries()));
                response.setHeader("X-Query-Time", String.valueOf(stats.getTotalExecutionTime()));
                
                // 将统计信息存储到请求属性中，供响应包装器使用
                request.setAttribute(QUERY_STATS_ATTR, stats);
                
                log.debug("完成查询统计: {} {}, 查询数: {}, 总耗时: {}ms", 
                    request.getMethod(), requestURI, stats.getTotalQueries(), stats.getTotalExecutionTime());
            }
        } catch (Exception e) {
            log.error("完成查询统计失败", e);
        }
    }
    
    /**
     * 判断是否应该跳过统计
     */
    private boolean shouldSkipStats(String requestURI) {
        return requestURI.contains("/static/") ||
               requestURI.contains("/css/") ||
               requestURI.contains("/js/") ||
               requestURI.contains("/images/") ||
               requestURI.contains("/favicon.ico") ||
               requestURI.contains("/actuator/") ||
               requestURI.contains("/swagger-ui/") ||
               requestURI.contains("/api-docs") ||
               requestURI.endsWith("/query-stats/health");
    }
}
