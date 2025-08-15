package com.xuyi.blog.service;

import com.xuyi.blog.dto.BlogStatsDTO;
import com.xuyi.blog.entity.Article;
import com.xuyi.blog.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 博客统计服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogStatsService {

    private final ArticleRepository articleRepository;
    private final VisitLogRepository visitLogRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final EmailSubscriberRepository emailSubscriberRepository;
    private final FriendLinkRepository friendLinkRepository;
    private final PortfolioRepository portfolioRepository;

    /**
     * 获取博客综合统计信息
     */
    @Cacheable(value = "blogStats", key = "'overview'")
    public BlogStatsDTO getBlogStats() {
        BlogStatsDTO stats = new BlogStatsDTO();

        // 文章总数（已发布）
        Long totalArticles = articleRepository.countByStatus(Article.ArticleStatus.PUBLISHED);
        stats.setTotalArticles(totalArticles != null ? totalArticles : 0L);

        // 总访问量
        Long totalVisits = visitLogRepository.getTotalVisitCount();
        stats.setTotalVisits(totalVisits != null ? totalVisits : 0L);

        // 标签数量
        Long totalTags = tagRepository.count();
        stats.setTotalTags(totalTags);

        // 分类数量
        Long totalCategories = categoryRepository.count();
        stats.setTotalCategories(totalCategories);

        // 今日访问量
        Long todayVisits = visitLogRepository.getTodayVisitCount(LocalDate.now());
        stats.setTodayVisits(todayVisits != null ? todayVisits : 0L);

        // 独立访客数
        Long uniqueVisitors = visitLogRepository.getUniqueVisitorCount();
        stats.setUniqueVisitors(uniqueVisitors != null ? uniqueVisitors : 0L);

        // 邮箱订阅者数量
        Long totalSubscribers = emailSubscriberRepository.countByIsActiveTrue();
        stats.setTotalSubscribers(totalSubscribers);

        // 友链数量
        Long totalFriendLinks = friendLinkRepository.count();
        stats.setTotalFriendLinks(totalFriendLinks);

        // 作品集数量
        Long totalPortfolios = portfolioRepository.count();
        stats.setTotalPortfolios(totalPortfolios);

        log.debug("获取博客统计信息: 文章{}篇, 访问{}次, 标签{}个, 分类{}个", 
                totalArticles, totalVisits, totalTags, totalCategories);

        return stats;
    }

    /**
     * 获取文章总数
     */
    @Cacheable(value = "blogStats", key = "'articles'")
    public Long getTotalArticles() {
        Long count = articleRepository.countByStatus(Article.ArticleStatus.PUBLISHED);
        return count != null ? count : 0L;
    }

    /**
     * 获取标签总数
     */
    @Cacheable(value = "blogStats", key = "'tags'")
    public Long getTotalTags() {
        return tagRepository.count();
    }

    /**
     * 获取分类总数
     */
    @Cacheable(value = "blogStats", key = "'categories'")
    public Long getTotalCategories() {
        return categoryRepository.count();
    }

    /**
     * 获取友链总数
     */
    @Cacheable(value = "blogStats", key = "'friendLinks'")
    public Long getTotalFriendLinks() {
        return friendLinkRepository.count();
    }

    /**
     * 获取作品集总数
     */
    @Cacheable(value = "blogStats", key = "'portfolios'")
    public Long getTotalPortfolios() {
        return portfolioRepository.count();
    }

    /**
     * 获取订阅者总数
     */
    @Cacheable(value = "blogStats", key = "'subscribers'")
    public Long getTotalSubscribers() {
        return emailSubscriberRepository.countByIsActiveTrue();
    }

    /**
     * 获取今日访问量
     */
    @Cacheable(value = "blogStats", key = "'todayVisits'")
    public Long getTodayVisits() {
        Long count = visitLogRepository.getTodayVisitCount(LocalDate.now());
        return count != null ? count : 0L;
    }

    /**
     * 获取总访问量
     */
    @Cacheable(value = "blogStats", key = "'totalVisits'")
    public Long getTotalVisits() {
        Long count = visitLogRepository.getTotalVisitCount();
        return count != null ? count : 0L;
    }

    /**
     * 获取独立访客数
     */
    @Cacheable(value = "blogStats", key = "'uniqueVisitors'")
    public Long getUniqueVisitors() {
        Long count = visitLogRepository.getUniqueVisitorCount();
        return count != null ? count : 0L;
    }
}
