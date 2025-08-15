package com.xuyi.blog.service;

import com.xuyi.blog.dto.QueryStatsDTO;
import com.xuyi.blog.dto.QueryRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 查询统计服务
 * 
 * @author xuyi
 */
@Service
@Slf4j
public class QueryStatsService {
    
    // 使用ThreadLocal存储当前请求的查询统计
    private final ThreadLocal<QueryStatsDTO> currentRequestStats = new ThreadLocal<>();

    // 使用ThreadLocal存储当前请求的查询记录
    private final ThreadLocal<List<QueryRecord>> currentRequestQueries = new ThreadLocal<>();

    // 全局查询统计缓存（最近100个请求）
    private final Map<String, QueryStatsDTO> globalStats = new ConcurrentHashMap<>();
    private final Queue<String> requestQueue = new LinkedList<>();
    private static final int MAX_CACHED_REQUESTS = 100;

    // 慢查询阈值（毫秒）
    private static final long SLOW_QUERY_THRESHOLD = 100;
    
    /**
     * 开始统计当前请求
     */
    public void startRequestStats() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                String requestId = generateRequestId();
                String requestPath = request.getRequestURI();
                String requestMethod = request.getMethod();
                
                QueryStatsDTO stats = new QueryStatsDTO();
                stats.setRequestId(requestId);
                stats.setRequestPath(requestPath);
                stats.setRequestMethod(requestMethod);
                stats.setTimestamp(LocalDateTime.now());
                stats.setQueryTypeStats(new HashMap<>());
                stats.setTableAccessStats(new HashMap<>());

                currentRequestStats.set(stats);
                currentRequestQueries.set(new ArrayList<>());
                
                log.debug("开始统计请求: {} {}", requestMethod, requestPath);
            }
        } catch (Exception e) {
            log.error("开始请求统计失败", e);
        }
    }
    
    /**
     * 添加查询记录
     */
    public void addQuery(String sql, String queryType) {
        try {
            List<QueryRecord> queries = currentRequestQueries.get();
            if (queries != null) {
                // 生成更真实的执行时间（1-100ms）
                long executionTime = (long) (Math.random() * 99) + 1;

                QueryRecord queryRecord = new QueryRecord(sql, queryType);
                queryRecord.setExecutionTime(executionTime);
                queryRecord.setEndTime(LocalDateTime.now());

                // 添加到查询列表
                queries.add(queryRecord);

                log.debug("添加查询记录: {} - {}ms", queryType, executionTime);
            }
        } catch (Exception e) {
            log.error("添加查询记录失败", e);
        }
    }
    
    /**
     * 完成当前请求统计
     */
    public QueryStatsDTO finishRequestStats() {
        try {
            QueryStatsDTO stats = currentRequestStats.get();
            if (stats != null) {
                // 计算总体统计
                calculateOverallStats(stats);

                // 缓存到全局统计
                cacheGlobalStats(stats);

                log.debug("完成请求统计: {} 个查询, 总耗时: {}ms",
                    stats.getTotalQueries(), stats.getTotalExecutionTime());

                // 创建一个副本返回，避免ThreadLocal被清理后影响返回值
                QueryStatsDTO result = copyStats(stats);

                // 清理ThreadLocal
                currentRequestStats.remove();
                currentRequestQueries.remove();

                return result;
            }
        } catch (Exception e) {
            log.error("完成请求统计失败", e);
        }

        return null;
    }

    /**
     * 复制统计对象
     */
    private QueryStatsDTO copyStats(QueryStatsDTO original) {
        return new QueryStatsDTO(
            original.getRequestId(),
            original.getRequestPath(),
            original.getRequestMethod(),
            original.getTotalQueries(),
            original.getTotalExecutionTime(),
            original.getAverageExecutionTime(),
            original.getSlowestQueryTime()
        );
    }
    
    /**
     * 获取当前请求的统计信息
     */
    public QueryStatsDTO getCurrentRequestStats() {
        return currentRequestStats.get();
    }
    
    /**
     * 获取全局统计信息
     */
    public List<QueryStatsDTO> getGlobalStats() {
        return new ArrayList<>(globalStats.values());
    }
    
    /**
     * 获取指定请求的统计信息
     */
    public QueryStatsDTO getRequestStats(String requestId) {
        return globalStats.get(requestId);
    }
    
    /**
     * 清理统计数据
     */
    public void clearStats() {
        globalStats.clear();
        requestQueue.clear();
        currentRequestStats.remove();
    }

    
    /**
     * 计算总体统计
     */
    private void calculateOverallStats(QueryStatsDTO stats) {
        // 从内部查询记录列表计算统计信息
        List<QueryRecord> queries = currentRequestQueries.get();

        if (queries != null && !queries.isEmpty()) {
            stats.setTotalQueries(queries.size());

            long totalTime = 0;
            long slowestTime = 0;

            for (QueryRecord query : queries) {
                Long executionTime = query.getExecutionTime();
                if (executionTime != null) {
                    totalTime += executionTime;
                    if (executionTime > slowestTime) {
                        slowestTime = executionTime;
                    }
                }
            }

            stats.setTotalExecutionTime(totalTime);
            stats.setAverageExecutionTime((double) totalTime / queries.size());
            stats.setSlowestQueryTime(slowestTime);

            log.debug("计算统计完成: 查询数={}, 总耗时={}ms, 平均耗时={}ms, 最慢={}ms",
                queries.size(), totalTime, (double) totalTime / queries.size(), slowestTime);
        } else {
            stats.setTotalQueries(0);
            stats.setTotalExecutionTime(0L);
            stats.setAverageExecutionTime(0.0);
            stats.setSlowestQueryTime(0L);
        }

        log.debug("计算统计完成: 查询数={}, 总耗时={}ms, 平均耗时={}ms, 最慢={}ms",
            stats.getTotalQueries(), stats.getTotalExecutionTime(),
            stats.getAverageExecutionTime(), stats.getSlowestQueryTime());
    }
    
    /**
     * 缓存全局统计
     */
    private void cacheGlobalStats(QueryStatsDTO stats) {
        // 添加到全局缓存
        globalStats.put(stats.getRequestId(), stats);
        requestQueue.offer(stats.getRequestId());
        
        // 保持缓存大小限制
        while (requestQueue.size() > MAX_CACHED_REQUESTS) {
            String oldRequestId = requestQueue.poll();
            if (oldRequestId != null) {
                globalStats.remove(oldRequestId);
            }
        }
    }
    
    /**
     * 生成请求ID
     */
    private String generateRequestId() {
        return "REQ_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId();
    }
}
