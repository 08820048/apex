package com.xuyi.blog.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 缓存配置类 - 使用内存缓存替代Redis
 *
 * @author xuyi
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置内存缓存管理器
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();

        // 设置缓存名称
        cacheManager.setCacheNames(Arrays.asList(
            "articles",
            "article",
            "categories",
            "tags",
            "portfolios",
            "portfolio",
            "friendLinks",
            "friendLink",
            "search",
            "visitStats",
            "rss",
            "emailSubscribers",
            "blogStats"
        ));

        // 允许运行时创建新的缓存
        cacheManager.setAllowNullValues(false);

        return cacheManager;
    }
}
