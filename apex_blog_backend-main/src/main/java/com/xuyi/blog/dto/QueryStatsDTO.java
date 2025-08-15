package com.xuyi.blog.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 查询统计DTO
 * 
 * @author xuyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryStatsDTO {
    
    /**
     * 请求ID
     */
    private String requestId;
    
    /**
     * 请求路径
     */
    private String requestPath;
    
    /**
     * 请求方法
     */
    private String requestMethod;
    
    /**
     * 总查询数量
     */
    private Integer totalQueries;
    
    /**
     * 总执行时间（毫秒）
     */
    private Long totalExecutionTime;
    
    /**
     * 平均执行时间（毫秒）
     */
    private Double averageExecutionTime;
    
    /**
     * 最慢查询时间（毫秒）
     */
    private Long slowestQueryTime;
    

    
    /**
     * 统计时间
     */
    private LocalDateTime timestamp;
    
    /**
     * 查询类型统计
     */
    private Map<String, Integer> queryTypeStats;

    /**
     * 表访问统计
     */
    private Map<String, Integer> tableAccessStats;

    // 手动添加setter方法确保它们存在
    public void setTotalQueries(Integer totalQueries) {
        this.totalQueries = totalQueries;
    }

    public void setTotalExecutionTime(Long totalExecutionTime) {
        this.totalExecutionTime = totalExecutionTime;
    }

    public void setAverageExecutionTime(Double averageExecutionTime) {
        this.averageExecutionTime = averageExecutionTime;
    }

    public void setSlowestQueryTime(Long slowestQueryTime) {
        this.slowestQueryTime = slowestQueryTime;
    }

    /**
     * 简化构造函数，只包含基本统计信息
     */
    public QueryStatsDTO(String requestId, String requestPath, String requestMethod,
                        Integer totalQueries, Long totalExecutionTime,
                        Double averageExecutionTime, Long slowestQueryTime) {
        this.requestId = requestId;
        this.requestPath = requestPath;
        this.requestMethod = requestMethod;
        this.totalQueries = totalQueries;
        this.totalExecutionTime = totalExecutionTime;
        this.averageExecutionTime = averageExecutionTime;
        this.slowestQueryTime = slowestQueryTime;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * 查询详情DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDetailDTO {
        
        /**
         * 查询序号
         */
        private Integer queryIndex;
        
        /**
         * SQL语句
         */
        private String sql;
        
        /**
         * 格式化的SQL语句
         */
        private String formattedSql;
        
        /**
         * 查询类型（SELECT, INSERT, UPDATE, DELETE）
         */
        private String queryType;
        
        /**
         * 执行时间（毫秒）
         */
        private Long executionTime;
        
        /**
         * 参数绑定
         */
        private List<String> parameters;
        
        /**
         * 影响的表
         */
        private List<String> tables;
        
        /**
         * 是否为慢查询
         */
        private Boolean isSlowQuery;
        
        /**
         * 查询开始时间
         */
        private LocalDateTime startTime;
        
        /**
         * 查询结束时间
         */
        private LocalDateTime endTime;
        
        /**
         * 错误信息（如果有）
         */
        private String errorMessage;
    }
}
