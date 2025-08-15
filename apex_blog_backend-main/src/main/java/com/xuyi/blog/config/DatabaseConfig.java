package com.xuyi.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据库配置类
 * 
 * @author xuyi
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.xuyi.blog.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {
    
    // JPA配置已在application.yml中完成
    // 这里主要是启用相关功能
}
