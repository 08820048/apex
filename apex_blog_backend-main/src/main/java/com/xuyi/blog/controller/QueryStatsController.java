package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.QueryStatsDTO;
import com.xuyi.blog.service.QueryStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询统计控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/query-stats")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "查询统计", description = "数据库查询统计相关接口")
public class QueryStatsController {
    
    private final QueryStatsService queryStatsService;
    
    @GetMapping("/current")
    @Operation(summary = "获取当前请求的查询统计")
    public ApiResponse<QueryStatsDTO> getCurrentRequestStats() {
        QueryStatsDTO stats = queryStatsService.getCurrentRequestStats();
        return ApiResponse.success(stats);
    }
    
    @GetMapping("/global")
    @Operation(summary = "获取全局查询统计")
    public ApiResponse<List<QueryStatsDTO>> getGlobalStats() {
        List<QueryStatsDTO> stats = queryStatsService.getGlobalStats();
        return ApiResponse.success(stats);
    }
    
    @GetMapping("/request/{requestId}")
    @Operation(summary = "获取指定请求的查询统计")
    public ApiResponse<QueryStatsDTO> getRequestStats(
            @Parameter(description = "请求ID") @PathVariable String requestId) {
        QueryStatsDTO stats = queryStatsService.getRequestStats(requestId);
        if (stats != null) {
            return ApiResponse.success(stats);
        } else {
            return ApiResponse.error("请求统计不存在");
        }
    }
    
    @GetMapping("/summary")
    @Operation(summary = "获取查询统计摘要")
    public ApiResponse<Map<String, Object>> getStatsSummary() {
        List<QueryStatsDTO> allStats = queryStatsService.getGlobalStats();
        
        Map<String, Object> summary = new HashMap<>();
        
        if (allStats.isEmpty()) {
            summary.put("totalRequests", 0);
            summary.put("totalQueries", 0);
            summary.put("averageQueriesPerRequest", 0.0);
            summary.put("totalExecutionTime", 0L);
            summary.put("averageExecutionTime", 0.0);
            summary.put("slowestQuery", 0L);
            summary.put("queryTypeDistribution", new HashMap<>());
            summary.put("tableAccessDistribution", new HashMap<>());
            summary.put("recentRequests", allStats);
        } else {
            // 总请求数
            summary.put("totalRequests", allStats.size());
            
            // 总查询数
            int totalQueries = allStats.stream()
                .mapToInt(QueryStatsDTO::getTotalQueries)
                .sum();
            summary.put("totalQueries", totalQueries);
            
            // 平均每请求查询数
            double avgQueriesPerRequest = (double) totalQueries / allStats.size();
            summary.put("averageQueriesPerRequest", Math.round(avgQueriesPerRequest * 100.0) / 100.0);
            
            // 总执行时间
            long totalExecutionTime = allStats.stream()
                .mapToLong(QueryStatsDTO::getTotalExecutionTime)
                .sum();
            summary.put("totalExecutionTime", totalExecutionTime);
            
            // 平均执行时间
            double avgExecutionTime = allStats.stream()
                .mapToDouble(QueryStatsDTO::getAverageExecutionTime)
                .average()
                .orElse(0.0);
            summary.put("averageExecutionTime", Math.round(avgExecutionTime * 100.0) / 100.0);
            
            // 最慢查询
            long slowestQuery = allStats.stream()
                .mapToLong(QueryStatsDTO::getSlowestQueryTime)
                .max()
                .orElse(0L);
            summary.put("slowestQuery", slowestQuery);
            
            // 查询类型分布
            Map<String, Integer> queryTypeDistribution = new HashMap<>();
            allStats.forEach(stats -> {
                if (stats.getQueryTypeStats() != null) {
                    stats.getQueryTypeStats().forEach((type, count) -> 
                        queryTypeDistribution.merge(type, count, Integer::sum));
                }
            });
            summary.put("queryTypeDistribution", queryTypeDistribution);
            
            // 表访问分布
            Map<String, Integer> tableAccessDistribution = new HashMap<>();
            allStats.forEach(stats -> {
                if (stats.getTableAccessStats() != null) {
                    stats.getTableAccessStats().forEach((table, count) -> 
                        tableAccessDistribution.merge(table, count, Integer::sum));
                }
            });
            summary.put("tableAccessDistribution", tableAccessDistribution);
            
            // 最近的请求（最多10个）
            List<QueryStatsDTO> recentRequests = allStats.stream()
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .limit(10)
                .collect(Collectors.toList());
            summary.put("recentRequests", recentRequests);
        }
        
        return ApiResponse.success(summary);
    }
    
    @DeleteMapping("/clear")
    @Operation(summary = "清理查询统计数据")
    public ApiResponse<Void> clearStats() {
        queryStatsService.clearStats();
        return ApiResponse.success("查询统计数据已清理");
    }
    
    @GetMapping("/health")
    @Operation(summary = "查询统计服务健康检查")
    public ApiResponse<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            List<QueryStatsDTO> stats = queryStatsService.getGlobalStats();
            health.put("status", "UP");
            health.put("cachedRequests", stats.size());
            health.put("currentRequest", queryStatsService.getCurrentRequestStats() != null);
            health.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
        }
        
        return ApiResponse.success(health);
    }
}
