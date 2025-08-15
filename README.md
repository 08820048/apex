<div align="center">
  <img src="https://images.waer.ltd/notes/202508151240669.jpg" alt="Apex Blog Logo" width="120" height="120" style="border-radius: 50%;">

# 🚀 Apex Blog

一个现代化的全栈博客系统，基于 Spring Boot + Vue.js 构建，支持文章管理、用户认证、邮箱订阅等功能。

> 🌐 **线上案例**: [https://www.ilikexff.cn/](https://www.ilikexff.cn/) - 基于本项目部署的实际博客，可参考功能效果

</div>

## ✨ 特性

- 🎨 **现代化 UI 设计** - 基于 Vue.js 3 + Element Plus
- 🔐 **安全认证** - JWT 令牌认证，BCrypt 密码加密
- 📝 **富文本编辑** - 支持 Markdown 编辑和预览
- 🏷️ **分类标签** - 灵活的文章分类和标签系统
- 📊 **数据统计** - 访问量统计和数据分析
- 📧 **邮件订阅** - 邮箱订阅和通知功能
- 🔍 **全文搜索** - 支持文章内容全文搜索
- 📱 **响应式设计** - 完美适配移动端和桌面端
- 🛡️ **安全防护** - 防刷机制，XSS 防护
- 📄 **RSS 订阅** - 支持 RSS 订阅功能

## 🏗️ 技术栈

### 后端

- **框架**: Spring Boot 3.x
- **数据库**: MySQL 8.0+
- **缓存**: 内存缓存 (Simple Cache)
- **安全**: Spring Security + JWT
- **文档**: Swagger/OpenAPI 3
- **构建**: Gradle

### 前端

- **框架**: Vue.js 3
- **UI 库**: Element Plus
- **构建**: Vite
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP**: Axios

### 管理后台

- **框架**: Vue.js 3
- **UI 库**: Element Plus
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

### 部署指南

详细的部署说明请参考：[部署文档.md](部署文档.md)

该文档包含：

- 开发环境搭建
- 生产环境部署
- 数据库配置
- 环境变量配置
- Nginx 配置
- 常见问题解决

### 快速体验

```bash
# 1. 克隆项目
git clone git@github.com:08820048/apex.git
cd apex

# 2. 按照部署文档配置环境和数据库

# 3. 启动服务（具体步骤见部署文档）
```

### 访问地址

- **前端网站**: http://localhost:5173
- **管理后台**: http://localhost:5174
- **API 文档**: http://localhost:8888/api/swagger-ui.html
- **健康检查**: http://localhost:8888/api/actuator/health

## 🔑 默认登录信息

- **用户名**: `admin`
- **密码**: `123456`

> ⚠️ **安全提醒**: 生产环境部署前请立即修改默认密码！

## 📚 文档

- [部署文档](部署文档.md) - 详细的部署和配置指南
- [安全配置指南](SECURITY.md) - 安全配置和最佳实践
- [默认登录信息](DEFAULT_LOGIN.md) - 默认账户信息
- [贡献指南](CONTRIBUTING.md) - 如何参与项目贡献
- [更新日志](CHANGELOG.md) - 版本更新记录
- [API 文档](http://localhost:8888/api/swagger-ui.html) - 在线 API 文档 (启动后访问)

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

## 🌐 线上案例

- **参考博客**: https://www.ilikexff.cn/
- **说明**: 基于本项目部署的实际博客系统，可参考其功能和界面效果

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 [Issue](https://github.com/08820048/apex/issues)
- 发送邮件至: ilikexff@gmail.com
- 访问博客: https://www.ilikexff.cn/

---

⭐ 如果这个项目对您有帮助，请给个星标支持一下！
