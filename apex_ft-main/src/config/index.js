// 环境配置
const config = {
  // 开发环境配置
  development: {
    // API基础URL
    apiBaseURL: import.meta.env.VITE_API_BASE_URL || "/api",
    // 服务器地址
    serverHost: import.meta.env.VITE_SERVER_HOST || "localhost:8888",
    // 应用标题
    appTitle: import.meta.env.VITE_APP_TITLE || "八尺妖剑 - 开发环境",
    // 是否启用调试模式
    debug: import.meta.env.VITE_DEBUG === "true" || true,
    // 是否启用查询统计
    enableQueryStats: import.meta.env.VITE_ENABLE_QUERY_STATS !== "false",
  },

  // 生产环境配置
  production: {
    // API基础URL
    apiBaseURL: import.meta.env.VITE_API_BASE_URL || "https://ilikexff.cn/api",
    // 服务器地址
    serverHost: import.meta.env.VITE_SERVER_HOST || "ilikexff.cn",
    // 应用标题
    appTitle: import.meta.env.VITE_APP_TITLE || "八尺妖剑",
    // 是否启用调试模式
    debug: import.meta.env.VITE_DEBUG === "true" || false,
    // 是否启用查询统计
    enableQueryStats: import.meta.env.VITE_ENABLE_QUERY_STATS !== "false",
  },

  // 测试环境配置
  test: {
    // API基础URL
    apiBaseURL: import.meta.env.VITE_API_BASE_URL || "/api",
    // 服务器地址
    serverHost: import.meta.env.VITE_SERVER_HOST || "localhost:8888",
    // 应用标题
    appTitle: import.meta.env.VITE_APP_TITLE || "八尺妖剑 - 测试环境",
    // 是否启用调试模式
    debug: import.meta.env.VITE_DEBUG === "true" || true,
    // 是否启用查询统计
    enableQueryStats:
      import.meta.env.VITE_ENABLE_QUERY_STATS === "true" || false,
  },
};

// 获取当前环境
const getEnvironment = () => {
  // 优先使用环境变量
  if (import.meta.env.MODE) {
    return import.meta.env.MODE;
  }

  // 根据hostname判断环境
  const hostname = window.location.hostname;
  if (hostname === "localhost" || hostname === "127.0.0.1") {
    return "development";
  } else {
    return "production";
  }
};

// 获取当前环境配置
const getCurrentConfig = () => {
  const env = getEnvironment();
  return config[env] || config.development;
};

// 导出配置
export default getCurrentConfig();

// 导出环境判断函数
export { getEnvironment, getCurrentConfig };

// 导出所有配置（用于调试）
export { config };
