# 🚀 Apex Blog

一个现代化的全栈博客系统，基于 Spring Boot + Vue.js 构建，支持文章管理、用户认证、评论系统等功能。

## ✨ 特性

- 🎨 **现代化UI设计** - 基于Vue.js 3 + Element Plus
- 🔐 **安全认证** - JWT令牌认证，BCrypt密码加密
- 📝 **富文本编辑** - 支持Markdown编辑和预览
- 🏷️ **分类标签** - 灵活的文章分类和标签系统
- 📊 **数据统计** - 访问量统计和数据分析
- 📧 **邮件通知** - 邮箱订阅和通知功能
- 🔍 **全文搜索** - 支持文章内容全文搜索
- 📱 **响应式设计** - 完美适配移动端和桌面端
- 🐳 **Docker支持** - 一键部署，支持容器化
- 🛡️ **安全防护** - 防刷机制，XSS防护

## 🏗️ 技术栈

### 后端
- **框架**: Spring Boot 3.x
- **数据库**: MySQL 8.0+
- **缓存**: 内存缓存 (可扩展Redis)
- **安全**: Spring Security + JWT
- **文档**: Swagger/OpenAPI 3
- **构建**: Gradle

### 前端
- **框架**: Vue.js 3
- **UI库**: Element Plus
- **构建**: Vite
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP**: Axios

### 管理后台
- **框架**: Vue.js 3
- **UI库**: Element Plus
- **功能**: 文章管理、用户管理、系统配置

## 📦 项目结构

```
apex/
├── apex_blog_backend-main/   # 后端服务 (Spring Boot)
├── apex_ft-main/            # 前端项目 (Vue.js)
├── apex_admin-main/         # 管理后台 (Vue.js)
├── .vscode/                 # VSCode配置
├── docs/                    # 文档
├── LICENSE                  # MIT许可证
├── README.md               # 项目说明
└── DEFAULT_LOGIN.md        # 默认登录信息
```

## 🚀 快速开始

### 环境要求

- Java 17+
- Node.js 16+
- MySQL 8.0+
- Git

### 1. 克隆项目

```bash
git clone git@github.com:08820048/apex.git
cd apex
```

### 2. 后端部署

```bash
cd apex_blog_backend-main

# 配置数据库
# 创建数据库
mysql -u root -p
CREATE DATABASE apex_blog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 复制配置文件
cp .env.example .env
# 编辑 .env 文件，配置数据库连接信息

# 启动后端服务
./gradlew bootRun
```

### 3. 前端部署

```bash
# 前端项目
cd apex_ft-main
cp .env.development.example .env.development
cp .env.production.example .env.production
npm install
npm run dev

# 管理后台
cd ../apex_admin-main
cp .env.example .env.production
npm install
npm run dev
```

### 4. 访问应用

- **前端网站**: http://localhost:5173
- **管理后台**: http://localhost:5174
- **API文档**: http://localhost:8888/api/swagger-ui.html
- **健康检查**: http://localhost:8888/api/actuator/health

## 🔑 默认登录信息

- **用户名**: `admin`
- **密码**: `123456`

> ⚠️ **安全提醒**: 生产环境部署前请立即修改默认密码！

## 📚 文档

- [安全配置指南](SECURITY.md)
- [默认登录信息](DEFAULT_LOGIN.md)
- [API文档](http://localhost:8888/api/swagger-ui.html) (启动后访问)

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目基于 [MIT License](LICENSE) 开源。

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者！

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 [Issue](https://github.com/08820048/apex/issues)
- 发送邮件至: admin@example.com

---

⭐ 如果这个项目对您有帮助，请给个星标支持一下！
