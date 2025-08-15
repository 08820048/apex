package com.xuyi.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 性能配置类
 * 
 * @author xuyi
 */
@Configuration
@Slf4j
public class PerformanceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源缓存配置
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600 * 24 * 7); // 7天缓存

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(3600 * 24 * 30); // 30天缓存

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(3600 * 24 * 7); // 7天缓存

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod(3600 * 24 * 7); // 7天缓存
    }

    @Bean
    public HealthIndicator customHealthIndicator() {
        return new HealthIndicator() {
            @Override
            public Health health() {
                // 自定义健康检查
                try {
                    // 检查应用状态
                    long freeMemory = Runtime.getRuntime().freeMemory();
                    long totalMemory = Runtime.getRuntime().totalMemory();
                    long usedMemory = totalMemory - freeMemory;
                    double memoryUsage = (double) usedMemory / totalMemory;

                    if (memoryUsage > 0.9) {
                        return Health.down()
                                .withDetail("memory", "Memory usage too high: " + String.format("%.2f%%", memoryUsage * 100))
                                .build();
                    }

                    return Health.up()
                            .withDetail("memory", String.format("%.2f%%", memoryUsage * 100))
                            .withDetail("freeMemory", freeMemory)
                            .withDetail("totalMemory", totalMemory)
                            .build();
                } catch (Exception e) {
                    return Health.down()
                            .withDetail("error", e.getMessage())
                            .build();
                }
            }
        };
    }
}
