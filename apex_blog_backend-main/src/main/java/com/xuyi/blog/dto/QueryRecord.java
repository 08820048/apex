package com.xuyi.blog.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 查询记录
 * 
 * @author xuyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRecord {
    
    /**
     * SQL语句
     */
    private String sql;
    
    /**
     * 查询类型（SELECT, INSERT, UPDATE, DELETE）
     */
    private String queryType;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 查询开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 查询结束时间
     */
    private LocalDateTime endTime;
    
    public QueryRecord(String sql, String queryType) {
        this.sql = sql;
        this.queryType = queryType;
        this.startTime = LocalDateTime.now();
    }
}
