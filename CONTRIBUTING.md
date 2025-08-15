# 贡献指南

感谢您对 Apex Blog 项目的关注！我们欢迎任何形式的贡献。

## 🤝 如何贡献

### 报告问题

如果您发现了 bug 或有功能建议：

1. 查看 [Issues](https://github.com/08820048/apex/issues) 确认问题是否已存在
2. 如果是新问题，请创建新的 Issue
3. 详细描述问题，包括：
   - 问题描述
   - 复现步骤
   - 期望行为
   - 实际行为
   - 环境信息（操作系统、浏览器、版本等）

### 提交代码

1. **Fork 仓库**
   ```bash
   # 点击 GitHub 页面右上角的 Fork 按钮
   ```

2. **克隆您的 Fork**
   ```bash
   git clone git@github.com:YOUR_USERNAME/apex.git
   cd apex
   ```

3. **创建特性分支**
   ```bash
   git checkout -b feature/your-feature-name
   # 或者修复bug
   git checkout -b fix/your-bug-fix
   ```

4. **进行开发**
   - 遵循现有的代码风格
   - 添加必要的测试
   - 确保所有测试通过

5. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   # 或者
   git commit -m "fix: fix your bug description"
   ```

6. **推送到您的 Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

7. **创建 Pull Request**
   - 在 GitHub 上打开 Pull Request
   - 详细描述您的更改
   - 关联相关的 Issue

## 📝 提交信息规范

请使用以下格式的提交信息：

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type 类型
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

### 示例
```
feat(auth): add JWT token refresh functionality

- Add refresh token endpoint
- Update authentication middleware
- Add token expiration handling

Closes #123
```

## 🧪 测试

在提交代码前，请确保：

1. **后端测试**
   ```bash
   cd apex_blog_backend-main
   ./gradlew test
   ```

2. **前端测试**
   ```bash
   cd apex_ft-main
   npm run test
   
   cd ../apex_admin-main
   npm run test
   ```

3. **代码格式检查**
   ```bash
   # 后端
   ./gradlew checkstyleMain
   
   # 前端
   npm run lint
   ```

## 🎨 代码风格

### Java (后端)
- 使用 4 个空格缩进
- 遵循 Google Java Style Guide
- 使用有意义的变量和方法名
- 添加适当的注释和文档

### JavaScript/Vue (前端)
- 使用 2 个空格缩进
- 遵循 ESLint 配置
- 使用 Prettier 格式化代码
- 组件名使用 PascalCase

## 🔍 代码审查

所有的 Pull Request 都需要经过代码审查：

1. 至少一个维护者的批准
2. 所有测试必须通过
3. 代码风格检查通过
4. 没有合并冲突

## 📋 开发环境设置

1. **安装依赖**
   - Java 17+
   - Node.js 16+
   - MySQL 8.0+

2. **配置开发环境**
   ```bash
   # 后端
   cd apex_blog_backend-main
   cp .env.example .env
   # 编辑 .env 文件
   
   # 前端
   cd apex_ft-main
   cp .env.development.example .env.development
   
   # 管理后台
   cd apex_admin-main
   cp .env.example .env.production
   ```

3. **启动开发服务器**
   ```bash
   # 后端
   ./gradlew bootRun
   
   # 前端
   npm run dev
   ```

## 🚀 发布流程

1. 更新版本号
2. 更新 CHANGELOG.md
3. 创建 Release Tag
4. 发布到 GitHub Releases

## 📞 获取帮助

如果您在贡献过程中遇到问题：

1. 查看现有的 [Issues](https://github.com/08820048/apex/issues)
2. 在 [Discussions](https://github.com/08820048/apex/discussions) 中提问
3. 发送邮件至: admin@example.com

## 🙏 感谢

感谢所有贡献者的努力！您的贡献让这个项目变得更好。

---

再次感谢您的贡献！🎉
