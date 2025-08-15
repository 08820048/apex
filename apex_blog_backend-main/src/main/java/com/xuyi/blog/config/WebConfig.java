package com.xuyi.blog.config;

import com.xuyi.blog.interceptor.VisitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 
 * @author xuyi
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final VisitInterceptor visitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                    "/admin/**",
                    "/auth/**",
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/error",
                    "/favicon.ico",
                    "/robots.txt",
                    "/sitemap.xml"
                );
    }
}
