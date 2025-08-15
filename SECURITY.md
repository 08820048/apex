# 安全配置指南

## 🔒 开源前安全检查清单

### ✅ 已清理的敏感信息
- [x] 数据库密码
- [x] Redis密码  
- [x] 邮箱账号和授权码
- [x] JWT密钥
- [x] 个人邮箱地址
- [x] 日志文件

### 🚨 部署前必须配置的敏感信息

#### 1. 数据库配置
```yaml
spring:
  datasource:
    username: root
    password: 你的数据库密码  # 请更换为实际密码
```

#### 2. 邮件服务配置
```yaml
spring:
  mail:
    username: 你的邮箱地址      # 请更换为实际邮箱
    password: 你的邮箱授权码    # 请更换为实际授权码
```

#### 3. JWT密钥配置
```yaml
jwt:
  secret: 请生成一个强密钥用于JWT签名  # 建议使用64位随机字符串
```

#### 4. 博客个人信息
```yaml
blog:
  author: "你的名字"
  email: 你的邮箱@example.com
  url: https://你的域名
```

### 🛡️ 安全建议

#### 密码强度要求
- 数据库密码：至少12位，包含大小写字母、数字和特殊字符
- JWT密钥：建议使用64位随机字符串
- 邮箱授权码：使用邮箱服务商提供的应用专用密码

#### 环境变量配置
推荐使用环境变量或外部配置文件管理敏感信息：

1. 复制环境变量示例文件：
```bash
cp .env.example .env
```

2. 编辑 `.env` 文件，填入实际配置信息

3. 确保 `.env` 文件已添加到 `.gitignore`

#### 生产环境部署
- 使用HTTPS协议
- 定期更换密钥
- 启用防火墙
- 定期备份数据库
- 监控异常访问

### 📝 配置文件说明

#### 开发环境
- `application-dev.yml`: 开发环境配置
- 可以使用相对宽松的安全设置

#### 生产环境  
- `application-prod.yml`: 生产环境配置
- 必须使用强密码和安全配置
- 关闭调试模式和详细日志

### 🔍 安全检查命令

部署前运行以下命令检查是否还有敏感信息：

```bash
# 检查是否有硬编码的密码
grep -r "password.*=" src/ --exclude-dir=target
grep -r "secret.*=" src/ --exclude-dir=target

# 检查是否有邮箱地址
grep -r "@.*\.com" src/ --exclude-dir=target

# 检查配置文件
find . -name "*.yml" -o -name "*.properties" | xargs grep -l "password\|secret\|key"
```

### ⚠️ 注意事项

1. **永远不要**将包含真实密码的配置文件提交到版本控制系统
2. **定期检查**提交历史中是否有敏感信息泄露
3. **使用**专门的密钥管理服务（如AWS Secrets Manager、Azure Key Vault等）
4. **启用**两步验证保护重要账户
5. **定期更新**依赖包以修复安全漏洞

### 📞 安全问题报告

如果发现安全问题，请通过以下方式联系：
- 邮箱：security@example.com
- 不要在公开的Issue中讨论安全问题
