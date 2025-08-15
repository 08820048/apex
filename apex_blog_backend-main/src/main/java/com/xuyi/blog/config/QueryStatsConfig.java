package com.xuyi.blog.config;

import com.xuyi.blog.interceptor.QueryStatsInterceptor;
import com.xuyi.blog.interceptor.QueryStatsWebInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

/**
 * 查询统计配置类
 * 
 * @author xuyi
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class QueryStatsConfig implements WebMvcConfigurer {
    
    private final QueryStatsWebInterceptor queryStatsWebInterceptor;
    private final QueryStatsInterceptor queryStatsInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册查询统计Web拦截器
        registry.addInterceptor(queryStatsWebInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico",
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/api-docs/**"
                );
        
        log.info("查询统计Web拦截器已注册");
    }
    
    /**
     * 配置Hibernate使用查询统计拦截器
     */
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return new HibernatePropertiesCustomizer() {
            @Override
            public void customize(Map<String, Object> hibernateProperties) {
                // 注册SQL拦截器
                hibernateProperties.put("hibernate.session_factory.statement_inspector", queryStatsInterceptor);
                
                // 启用统计信息收集
                hibernateProperties.put("hibernate.generate_statistics", true);
                
                log.info("Hibernate查询统计拦截器已配置");
            }
        };
    }
}
