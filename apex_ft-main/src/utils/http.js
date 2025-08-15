import { globalQueryStats } from "../composables/useQueryStats.js";

// HTTP 请求工具
class HttpClient {
  constructor(baseURL = "") {
    this.baseURL = baseURL;
  }

  async request(url, options = {}) {
    const config = {
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(this.baseURL + url, config);

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const data = await response.json();

      // 处理查询统计信息
      if (data.queryStats) {
        globalQueryStats.addQueryStats(data.queryStats);
      }

      // 处理后端统一响应格式
      if (data.code !== undefined && data.code !== 200) {
        throw new Error(data.message || "请求失败");
      }

      return data.data || data;
    } catch (error) {
      console.error("API请求错误:", error);
      throw error;
    }
  }

  get(url, params = {}) {
    const searchParams = new URLSearchParams();

    // 自动添加查询统计参数（用于技术展示）
    if (globalQueryStats.isStatsEnabled.value) {
      params.includeStats = true;
    }

    Object.keys(params).forEach((key) => {
      if (params[key] !== undefined && params[key] !== null) {
        searchParams.append(key, params[key]);
      }
    });

    const queryString = searchParams.toString();
    const fullUrl = queryString ? `${url}?${queryString}` : url;

    return this.request(fullUrl);
  }

  post(url, data = {}) {
    return this.request(url, {
      method: "POST",
      body: JSON.stringify(data),
    });
  }

  put(url, data = {}) {
    return this.request(url, {
      method: "PUT",
      body: JSON.stringify(data),
    });
  }

  delete(url) {
    return this.request(url, {
      method: "DELETE",
    });
  }
}

// 创建实例
const http = new HttpClient("/api");

export default http;
