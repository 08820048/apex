package com.xuyi.blog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 博客统计DTO
 * 
 * @author xuyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogStatsDTO {
    
    /**
     * 文章总数
     */
    private Long totalArticles;
    
    /**
     * 总访问量
     */
    private Long totalVisits;
    
    /**
     * 标签数量
     */
    private Long totalTags;
    
    /**
     * 分类数量
     */
    private Long totalCategories;
    
    /**
     * 今日访问量
     */
    private Long todayVisits;
    
    /**
     * 独立访客数
     */
    private Long uniqueVisitors;
    
    /**
     * 邮箱订阅者数量
     */
    private Long totalSubscribers;
    
    /**
     * 友链数量
     */
    private Long totalFriendLinks;
    
    /**
     * 作品集数量
     */
    private Long totalPortfolios;
}
