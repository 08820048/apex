-- 测试数据初始化脚本
USE apex_blog;

-- 清理现有数据
DELETE FROM article_tags;
DELETE FROM articles;
DELETE FROM categories;
DELETE FROM tags;
DELETE FROM users;
DELETE FROM portfolios;
DELETE FROM friend_links;
DELETE FROM email_subscribers;
DELETE FROM system_configs;

-- 重置自增ID
ALTER TABLE articles AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;
ALTER TABLE tags AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE portfolios AUTO_INCREMENT = 1;
ALTER TABLE friend_links AUTO_INCREMENT = 1;
ALTER TABLE email_subscribers AUTO_INCREMENT = 1;
ALTER TABLE system_configs AUTO_INCREMENT = 1;

-- 插入用户数据 (默认用户名: admin, 密码: 123456)
INSERT INTO users (username, password, email, nickname, avatar, bio, created_at, updated_at) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@example.com', '管理员', 'https://avatars.githubusercontent.com/u/1?v=4', '系统管理员，负责博客系统管理', NOW(), NOW()),
('testuser', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'test@example.com', '测试用户', 'https://avatars.githubusercontent.com/u/2?v=4', '这是一个测试用户', NOW(), NOW());

-- 插入分类数据
INSERT INTO categories (name, description, sort_order, created_at, updated_at) VALUES
('技术分享', '分享各种技术文章和教程', 1, NOW(), NOW()),
('生活随笔', '记录生活中的点点滴滴', 2, NOW(), NOW()),
('项目经验', '分享项目开发经验和心得', 3, NOW(), NOW()),
('学习笔记', '学习过程中的笔记和总结', 4, NOW(), NOW());

-- 插入标签数据
INSERT INTO tags (name, color, created_at, updated_at) VALUES
('Java', '#f89820', NOW(), NOW()),
('Spring Boot', '#6db33f', NOW(), NOW()),
('Vue.js', '#4fc08d', NOW(), NOW()),
('MySQL', '#4479a1', NOW(), NOW()),
('Redis', '#dc382d', NOW(), NOW()),
('Docker', '#2496ed', NOW(), NOW()),
('前端', '#61dafb', NOW(), NOW()),
('后端', '#68217a', NOW(), NOW()),
('算法', '#ff6b6b', NOW(), NOW()),
('数据结构', '#4ecdc4', NOW(), NOW());

-- 插入文章数据
INSERT INTO articles (title, summary, content, cover_image, status, is_top, view_count, like_count, comment_count, category_id, author_id, published_at, created_at, updated_at) VALUES
('Spring Boot 3.0 新特性详解', 'Spring Boot 3.0 带来了许多令人兴奋的新特性，本文将详细介绍这些新功能。',
'# Spring Boot 3.0 新特性详解

Spring Boot 3.0 是一个重要的里程碑版本，带来了许多令人兴奋的新特性和改进。

## 主要新特性

### 1. Java 17 基线
Spring Boot 3.0 要求 Java 17 作为最低版本，这意味着你可以使用所有 Java 17 的新特性。

### 2. 原生镜像支持
通过 GraalVM，Spring Boot 3.0 提供了更好的原生镜像支持，可以显著减少启动时间和内存占用。

### 3. 可观测性改进
新版本在可观测性方面有了重大改进，包括更好的指标、追踪和日志记录。

## 总结
Spring Boot 3.0 是一个值得升级的版本，带来了许多实用的新特性。',
'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800', 'PUBLISHED', true, 156, 23, 8, 1, 1, NOW(), NOW(), NOW()),

('Vue 3 Composition API 实战指南', 'Vue 3 的 Composition API 为我们提供了更灵活的组件逻辑组织方式。',
'# Vue 3 Composition API 实战指南

Vue 3 引入的 Composition API 是一个重要的新特性，它为我们提供了更灵活的组件逻辑组织方式。

## 什么是 Composition API

Composition API 是一套基于函数的 API，允许我们更灵活地组织组件逻辑。

## 基本用法

```javascript
import { ref, computed, onMounted } from "vue"

export default {
  setup() {
    const count = ref(0)
    const doubleCount = computed(() => count.value * 2)

    onMounted(() => {
      console.log("组件已挂载")
    })

    return {
      count,
      doubleCount
    }
  }
}
```

## 优势
1. 更好的逻辑复用
2. 更清晰的代码组织
3. 更好的 TypeScript 支持',
'https://images.unsplash.com/photo-1627398242454-45a1465c2479?w=800', 'PUBLISHED', false, 89, 15, 5, 1, 1, NOW(), NOW(), NOW()),

('MySQL 性能优化实践', '分享一些 MySQL 性能优化的实用技巧和最佳实践。',
'# MySQL 性能优化实践

数据库性能优化是后端开发中的重要技能，本文分享一些 MySQL 优化的实用技巧。

## 索引优化

### 1. 选择合适的索引类型
- B-Tree 索引：适用于大多数查询场景
- Hash 索引：适用于等值查询
- 全文索引：适用于文本搜索

### 2. 复合索引的使用
创建复合索引时要注意字段的顺序，遵循最左前缀原则。

## 查询优化

### 1. 避免 SELECT *
只查询需要的字段，减少数据传输量。

### 2. 使用 LIMIT
对于大结果集，使用 LIMIT 限制返回的行数。

## 配置优化

### 1. 调整缓冲池大小
```sql
SET GLOBAL innodb_buffer_pool_size = 1073741824;
```

### 2. 优化连接数
```sql
SET GLOBAL max_connections = 200;
```

## 总结
性能优化是一个持续的过程，需要根据具体业务场景进行调整。',
'https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=800', 'PUBLISHED', false, 234, 31, 12, 1, 1, NOW(), NOW(), NOW()),

('我的编程学习之路', '回顾我从编程小白到全栈开发者的成长历程。',
'# 我的编程学习之路

时间过得真快，不知不觉已经在编程这条路上走了好几年。今天想和大家分享一下我的学习经历。

## 初入编程世界

还记得第一次接触编程是在大学的 C 语言课程上，那时候对着黑乎乎的命令行界面，感觉既神秘又困惑。

## 前端的魅力

后来接触到 HTML、CSS 和 JavaScript，看到自己写的代码能在浏览器中呈现出漂亮的页面，那种成就感是无法言喻的。

## 后端的深度

随着学习的深入，我开始接触后端开发，学习了 Java、Spring 框架，了解了数据库、服务器等概念。

## 全栈之路

现在的我可以独立完成一个完整的 Web 应用，从前端界面到后端 API，从数据库设计到服务器部署。

## 感悟

编程不仅仅是写代码，更是一种思维方式。它教会了我如何分析问题、解决问题，如何用逻辑思维去思考。

## 给新手的建议

1. 保持好奇心和学习热情
2. 多动手实践，理论结合实际
3. 不要害怕犯错，错误是最好的老师
4. 加入技术社区，与他人交流学习

希望我的经历能给正在学习编程的朋友们一些启发和鼓励！',
'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=800', 'PUBLISHED', false, 67, 8, 3, 2, 1, NOW(), NOW(), NOW()),

('Docker 容器化部署实战', '详细介绍如何使用 Docker 进行应用容器化部署。',
'# Docker 容器化部署实战

Docker 已经成为现代应用部署的标准工具，本文将详细介绍如何使用 Docker 进行应用容器化部署。

## Docker 基础概念

### 镜像 (Image)
镜像是一个只读的模板，用来创建容器。

### 容器 (Container)
容器是镜像的运行实例。

### Dockerfile
Dockerfile 是用来构建镜像的文本文件。

## 编写 Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/app.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

## Docker Compose

对于多服务应用，可以使用 Docker Compose：

```yaml
version: "3.8"
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql

  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
```

## 最佳实践

1. 使用多阶段构建减小镜像大小
2. 合理使用 .dockerignore
3. 避免在容器中存储数据
4. 使用健康检查

## 总结

Docker 大大简化了应用的部署和运维，是现代开发必备的技能。',
'https://images.unsplash.com/photo-1605745341112-85968b19335b?w=800', 'PUBLISHED', false, 145, 19, 7, 3, 1, NOW(), NOW(), NOW());

-- 插入文章标签关联
INSERT INTO article_tags (article_id, tag_id) VALUES
(1, 1), (1, 2), (1, 8),  -- Spring Boot 文章
(2, 3), (2, 7),          -- Vue 文章
(3, 4), (3, 8),          -- MySQL 文章
(5, 6), (5, 8);          -- Docker 文章

-- 插入作品集数据
INSERT INTO portfolios (name, description, cover_image, url, technologies, is_featured, sort_order, created_at, updated_at) VALUES
('ApexBlog 博客系统', '基于 Spring Boot 和 Vue.js 的现代化博客系统，支持文章管理、用户认证、评论系统等功能。', 'https://images.unsplash.com/photo-1432888622747-4eb9a8efeb07?w=800', 'https://blog.xuyi.com', 'Spring Boot,Vue.js,MySQL,Redis', true, 1, NOW(), NOW()),
('在线代码编辑器', '支持多种编程语言的在线代码编辑器，具有语法高亮、代码补全、实时预览等功能。', 'https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=800', 'https://editor.xuyi.com', 'React,Node.js,Monaco Editor', true, 2, NOW(), NOW()),
('任务管理系统', '团队协作的任务管理系统，支持项目管理、任务分配、进度跟踪等功能。', 'https://images.unsplash.com/photo-1611224923853-80b023f02d71?w=800', 'https://tasks.xuyi.com', 'Vue.js,Spring Boot,PostgreSQL', false, 3, NOW(), NOW());

-- 插入友情链接数据
INSERT INTO friend_links (name, url, description, avatar, is_active, sort_order, created_at, updated_at) VALUES
('GitHub', 'https://github.com', '全球最大的代码托管平台', 'https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png', true, 1, NOW(), NOW()),
('Stack Overflow', 'https://stackoverflow.com', '程序员问答社区', 'https://cdn.sstatic.net/Sites/stackoverflow/Img/apple-touch-icon.png', true, 2, NOW(), NOW()),
('MDN Web Docs', 'https://developer.mozilla.org', 'Web 开发文档', 'https://developer.mozilla.org/favicon-48x48.cbbd161b5b0b.png', true, 3, NOW(), NOW()),
('Vue.js', 'https://vuejs.org', '渐进式 JavaScript 框架', 'https://vuejs.org/logo.svg', true, 4, NOW(), NOW()),
('Spring', 'https://spring.io', 'Java 企业级开发框架', 'https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg', true, 5, NOW(), NOW());

-- 插入邮箱订阅数据
INSERT INTO email_subscribers (email, token, is_active, subscribed_at) VALUES
('subscriber1@example.com', 'token1', true, NOW()),
('subscriber2@example.com', 'token2', true, NOW()),
('subscriber3@example.com', 'token3', false, NOW());

-- 插入系统配置数据
INSERT INTO system_configs (config_key, config_value, description, created_at, updated_at) VALUES
('site.title', 'ApexBlog', '网站标题', NOW(), NOW()),
('site.description', '一个现代化的个人博客系统', '网站描述', NOW(), NOW()),
('site.keywords', 'blog,技术,编程,Java,Vue.js', '网站关键词', NOW(), NOW()),
('site.author', '管理员', '网站作者', NOW(), NOW()),
('site.email', 'admin@example.com', '联系邮箱', NOW(), NOW()),
('site.icp', '京ICP备12345678号', 'ICP备案号', NOW(), NOW()),
('comment.enable', 'true', '是否启用评论', NOW(), NOW()),
('rss.enable', 'true', '是否启用RSS', NOW(), NOW());
