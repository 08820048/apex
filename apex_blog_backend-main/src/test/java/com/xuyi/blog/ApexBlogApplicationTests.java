package com.xuyi.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 应用程序测试类
 * 
 * @author xuyi
 */
@SpringBootTest
@ActiveProfiles("test")
class ApexBlogApplicationTests {

    @Test
    void contextLoads() {
        // 测试Spring上下文是否能正常加载
    }
}
