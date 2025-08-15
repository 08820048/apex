package com.xuyi.blog.interceptor;

import com.xuyi.blog.dto.QueryStatsDTO;
import com.xuyi.blog.service.QueryStatsService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查询统计拦截器
 * 实现Hibernate的StatementInspector接口来拦截SQL查询
 * 
 * @author xuyi
 */
@Component
@Slf4j
public class QueryStatsInterceptor implements StatementInspector {
    
    @Autowired
    private QueryStatsService queryStatsService;
    
    // SQL类型匹配模式
    private static final Pattern SQL_TYPE_PATTERN = Pattern.compile("^\\s*(SELECT|INSERT|UPDATE|DELETE|CREATE|DROP|ALTER)", Pattern.CASE_INSENSITIVE);
    
    // 表名匹配模式
    private static final Pattern TABLE_PATTERN = Pattern.compile("(?:FROM|INTO|UPDATE|JOIN)\\s+([a-zA-Z_][a-zA-Z0-9_]*)", Pattern.CASE_INSENSITIVE);
    
    @Override
    public String inspect(String sql) {
        try {
            // 解析查询类型
            String queryType = extractQueryType(sql);

            // 添加到统计服务
            queryStatsService.addQuery(sql, queryType);

            log.debug("拦截到SQL查询: {}", sql);

        } catch (Exception e) {
            log.error("查询统计拦截器异常", e);
        }

        // 返回原始SQL，不做修改
        return sql;
    }
    
    /**
     * 解析SQL查询
     */
    private QueryStatsDTO.QueryDetailDTO parseQuery(String sql, LocalDateTime startTime) {
        QueryStatsDTO.QueryDetailDTO queryDetail = new QueryStatsDTO.QueryDetailDTO();
        
        queryDetail.setSql(sql);
        queryDetail.setFormattedSql(formatSql(sql));
        queryDetail.setQueryType(extractQueryType(sql));
        queryDetail.setTables(extractTables(sql));
        queryDetail.setStartTime(startTime);
        queryDetail.setParameters(new ArrayList<>());
        queryDetail.setIsSlowQuery(false);
        
        return queryDetail;
    }
    
    /**
     * 格式化SQL
     */
    private String formatSql(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return sql;
        }
        
        return sql.replaceAll("\\s+", " ")
                  .trim()
                  .replaceAll("(?i)\\b(SELECT|FROM|WHERE|JOIN|LEFT JOIN|RIGHT JOIN|INNER JOIN|ORDER BY|GROUP BY|HAVING|UNION|INSERT|UPDATE|DELETE|CREATE|DROP|ALTER)\\b", "\n$1")
                  .replaceAll("\\n\\s*", "\n")
                  .trim();
    }
    
    /**
     * 提取查询类型
     */
    private String extractQueryType(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "UNKNOWN";
        }
        
        Matcher matcher = SQL_TYPE_PATTERN.matcher(sql.trim());
        if (matcher.find()) {
            return matcher.group(1).toUpperCase();
        }
        
        return "UNKNOWN";
    }
    
    /**
     * 提取涉及的表名
     */
    private List<String> extractTables(String sql) {
        List<String> tables = new ArrayList<>();
        
        if (sql == null || sql.trim().isEmpty()) {
            return tables;
        }
        
        Matcher matcher = TABLE_PATTERN.matcher(sql);
        while (matcher.find()) {
            String tableName = matcher.group(1);
            if (!tables.contains(tableName)) {
                tables.add(tableName);
            }
        }
        
        return tables;
    }
}
