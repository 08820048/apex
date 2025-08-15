package com.xuyi.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ApexBlog 主应用程序入口
 * 
 * @author xuyi
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
public class ApexBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApexBlogApplication.class, args);
    }
}
