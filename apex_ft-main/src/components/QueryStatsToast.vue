<template>
  <!-- 查询统计文本条 - 从右侧飘出 -->
  <div class="fixed bottom-20 right-4 space-y-1" style="z-index: 9999">
    <div
      v-for="toast in visibleToasts"
      :key="toast.id"
      class="text-sm transform transition-all duration-300 slide-in-right"
      :class="toast.isTitle ? 'text-green-500' : 'text-gray-600'"
    >
      {{ toast.text }}
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onUnmounted } from "vue";
import { globalQueryStats } from "../composables/useQueryStats.js";

// 响应式数据
const visibleToasts = ref([]);
const toastId = ref(0);
const lastRequestId = ref(null);
const lastStatsHash = ref(null);

// 从全局状态获取查询统计
const { onStatsAdded } = globalQueryStats;

// 显示文本条
const showToasts = (stats) => {
  // 防重复显示：生成统计数据的哈希值
  const statsHash = `${stats.totalExecutionTime}-${stats.averageExecutionTime}-${stats.slowestQueryTime}-${stats.requestPath}`;

  // 检查是否是相同的请求（基于requestId或统计数据哈希）
  if (
    (stats.requestId && stats.requestId === lastRequestId.value) ||
    statsHash === lastStatsHash.value
  ) {
    return;
  }

  lastRequestId.value = stats.requestId;
  lastStatsHash.value = statsHash;

  // 清除之前的文本条
  visibleToasts.value = [];

  // 检查是否走缓存（所有时间字段都是0）
  const isFromCache =
    stats.totalExecutionTime === 0 &&
    stats.averageExecutionTime === 0 &&
    stats.slowestQueryTime === 0;

  // 创建文本条数据
  const toastData = [
    {
      id: ++toastId.value,
      text: "查询完毕",
      isTitle: true,
    },
  ];

  if (isFromCache) {
    // 缓存情况
    toastData.push({
      id: ++toastId.value,
      text: "缓存命中！",
    });
  } else {
    // 数据库查询情况
    toastData.push(
      {
        id: ++toastId.value,
        text: `总耗时：${stats.totalExecutionTime} 毫秒`,
      },
      {
        id: ++toastId.value,
        text: `平均耗时：${
          Math.round(stats.averageExecutionTime * 100) / 100
        } 毫秒`,
      },
      {
        id: ++toastId.value,
        text: `最慢查询：${stats.slowestQueryTime} 毫秒`,
      }
    );
  }

  // 逐个显示文本条，每个间隔200ms
  toastData.forEach((toast, index) => {
    setTimeout(() => {
      visibleToasts.value.push(toast);

      // 4秒后自动移除这个文本条
      setTimeout(() => {
        const toastIndex = visibleToasts.value.findIndex(
          (t) => t.id === toast.id
        );
        if (toastIndex > -1) {
          visibleToasts.value.splice(toastIndex, 1);
        }
      }, 4000);
    }, index * 200);
  });
};

// 注册查询统计回调
const unregisterCallback = onStatsAdded((newStats) => {
  showToasts(newStats);
});

// 清理定时器和回调
onUnmounted(() => {
  visibleToasts.value = [];
  unregisterCallback();
});
</script>

<style scoped>
/* 滑入动画 */
@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(100%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.slide-in-right {
  animation: slideInRight 0.4s ease-out;
}
</style>
