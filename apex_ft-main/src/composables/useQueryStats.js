import { ref, computed } from "vue";

// 全局查询统计状态
const queryStatsHistory = ref([]);
const isStatsEnabled = ref(true); // 默认启用，用于技术展示
const onStatsAddedCallbacks = ref([]);

export function useQueryStats() {
  // 添加查询统计记录
  const addQueryStats = (stats) => {
    if (stats && isStatsEnabled.value) {
      const newStats = {
        ...stats,
        timestamp: Date.now(),
      };
      queryStatsHistory.value.unshift(newStats);

      // 只保留最近50条记录
      if (queryStatsHistory.value.length > 50) {
        queryStatsHistory.value = queryStatsHistory.value.slice(0, 50);
      }

      // 在控制台输出统计信息（开发模式）
      if (process.env.NODE_ENV === "development") {
        console.group("🔍 数据库查询统计");
        console.log(`请求: ${stats.requestMethod} ${stats.requestPath}`);
        console.log(
          `执行了 ${stats.totalQueries} 个查询，总耗时 ${stats.totalExecutionTime}ms`
        );
        console.log(`平均耗时: ${stats.averageExecutionTime}ms`);
        console.log("查询类型分布:", stats.queryTypeStats);
        console.log("表访问统计:", stats.tableAccessStats);

        // 显示慢查询警告
        if (stats.slowestQueryTime > 100) {
          console.warn(`⚠️ 发现慢查询: ${stats.slowestQueryTime}ms`);
        }

        console.groupEnd();
      }

      // 触发回调
      onStatsAddedCallbacks.value.forEach((callback) => {
        try {
          callback(newStats);
        } catch (error) {
          console.error("查询统计回调执行失败:", error);
        }
      });
    }
  };

  // 计算统计摘要
  const statsSummary = computed(() => {
    if (queryStatsHistory.value.length === 0) return null;

    const totalRequests = queryStatsHistory.value.length;
    const totalQueries = queryStatsHistory.value.reduce(
      (sum, stat) => sum + stat.totalQueries,
      0
    );
    const totalExecutionTime = queryStatsHistory.value.reduce(
      (sum, stat) => sum + stat.totalExecutionTime,
      0
    );

    // 查询类型统计
    const queryTypeStats = {};
    queryStatsHistory.value.forEach((stat) => {
      Object.entries(stat.queryTypeStats || {}).forEach(([type, count]) => {
        queryTypeStats[type] = (queryTypeStats[type] || 0) + count;
      });
    });

    // 表访问统计
    const tableAccessStats = {};
    queryStatsHistory.value.forEach((stat) => {
      Object.entries(stat.tableAccessStats || {}).forEach(([table, count]) => {
        tableAccessStats[table] = (tableAccessStats[table] || 0) + count;
      });
    });

    return {
      totalRequests,
      totalQueries,
      averageQueriesPerRequest: totalQueries / totalRequests,
      totalExecutionTime,
      averageExecutionTime: totalExecutionTime / totalRequests,
      slowestQuery: Math.max(
        ...queryStatsHistory.value.map((s) => s.slowestQueryTime || 0)
      ),
      queryTypeStats,
      tableAccessStats,
      recentRequests: queryStatsHistory.value.slice(0, 10),
    };
  });

  // 获取性能等级
  const getPerformanceGrade = (avgTime) => {
    if (avgTime < 10)
      return { grade: "A+", color: "text-green-400", desc: "极快" };
    if (avgTime < 25)
      return { grade: "A", color: "text-green-400", desc: "很快" };
    if (avgTime < 50)
      return { grade: "B", color: "text-yellow-400", desc: "良好" };
    if (avgTime < 100)
      return { grade: "C", color: "text-orange-400", desc: "一般" };
    return { grade: "D", color: "text-red-400", desc: "需优化" };
  };

  // 清空统计数据
  const clearStats = () => {
    queryStatsHistory.value = [];
  };

  // 启用/禁用统计
  const toggleStats = () => {
    isStatsEnabled.value = !isStatsEnabled.value;
  };

  // 获取最近的查询统计
  const getRecentStats = (limit = 10) => {
    return queryStatsHistory.value.slice(0, limit);
  };

  // 检查是否有慢查询
  const hasSlowQueries = computed(() => {
    return queryStatsHistory.value.some((stat) => stat.slowestQueryTime > 100);
  });

  // 获取查询效率指标
  const getEfficiencyMetrics = computed(() => {
    if (queryStatsHistory.value.length === 0) return null;

    const avgQueriesPerRequest = statsSummary.value.averageQueriesPerRequest;
    const avgExecutionTime = statsSummary.value.averageExecutionTime;

    return {
      efficiency:
        avgQueriesPerRequest < 5
          ? "高效"
          : avgQueriesPerRequest < 10
          ? "中等"
          : "需优化",
      performance: getPerformanceGrade(avgExecutionTime),
      healthScore: Math.max(
        0,
        100 - avgExecutionTime * 2 - avgQueriesPerRequest * 5
      ),
    };
  });

  // 注册统计添加回调
  const onStatsAdded = (callback) => {
    onStatsAddedCallbacks.value.push(callback);

    // 返回取消注册的函数
    return () => {
      const index = onStatsAddedCallbacks.value.indexOf(callback);
      if (index > -1) {
        onStatsAddedCallbacks.value.splice(index, 1);
      }
    };
  };

  return {
    queryStatsHistory,
    isStatsEnabled,
    statsSummary,
    hasSlowQueries,
    getEfficiencyMetrics,
    addQueryStats,
    clearStats,
    toggleStats,
    getRecentStats,
    getPerformanceGrade,
    onStatsAdded,
  };
}

// 全局查询统计实例
export const globalQueryStats = useQueryStats();
