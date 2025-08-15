<template>
  <div class="article-detail-container">
    <!-- 禅模式提示弹窗 -->
    <Transition name="modal">
      <div
        v-if="showZenTip"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
        @click="closeZenTip"
      >
        <div
          class="bg-white dark:bg-gray-800 rounded-2xl p-8 max-w-md mx-4 text-center shadow-2xl border border-gray-100 dark:border-gray-700"
          @click.stop
        >
          <div class="relative mb-6">
            <div class="text-6xl mb-2 animate-bounce">🧘‍♂️</div>
            <div
              class="absolute inset-0 bg-purple-100 dark:bg-purple-900 rounded-full opacity-20 blur-xl"
            ></div>
          </div>
          <h3 class="text-xl font-bold text-gray-900 dark:text-gray-100 mb-4">
            进入禅模式
          </h3>
          <p class="text-gray-600 dark:text-gray-300 mb-4">
            请输入"阿弥陀佛"，让心灵沉静下来，专注于文字的力量
          </p>

          <!-- 输入框 -->
          <div class="mb-4">
            <input
              v-model="zenInput"
              @keyup.enter="checkZenInput"
              type="text"
              placeholder="请输入：阿弥陀佛"
              class="w-full px-4 py-3 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400 focus:ring-2 focus:ring-purple-500 focus:border-transparent transition-all duration-200"
              :class="
                inputStatus === 'success'
                  ? 'border-green-500 bg-green-50 dark:bg-green-900/20'
                  : inputStatus === 'error'
                  ? 'border-red-500 bg-red-50 dark:bg-red-900/20'
                  : ''
              "
            />
          </div>

          <!-- 输入状态提示 -->
          <div
            v-if="inputMessage"
            class="mb-4 p-3 rounded-lg text-sm"
            :class="
              inputStatus === 'success'
                ? 'bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-300 border border-green-200 dark:border-green-800'
                : 'bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-300 border border-red-200 dark:border-red-800'
            "
          >
            <div class="flex items-center">
              <span class="mr-2">{{
                inputStatus === "success" ? "✅" : "❌"
              }}</span>
              <span>{{ inputMessage }}</span>
            </div>
          </div>

          <div class="flex gap-3">
            <button
              @click="closeZenTip"
              class="flex-1 px-4 py-2 text-gray-600 dark:text-gray-300 bg-gray-100 dark:bg-gray-700 border border-gray-200 dark:border-gray-600 rounded-lg hover:bg-gray-200 dark:hover:bg-gray-600 transition-all duration-200"
            >
              取消
            </button>
            <button
              @click="checkZenInput"
              :disabled="!zenInput.trim()"
              class="flex-1 px-4 py-2 bg-purple-600 hover:bg-purple-700 disabled:bg-purple-400 text-white rounded-lg transition-all duration-200 shadow-md hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed disabled:shadow-none"
            >
              <span class="flex items-center justify-center">
                <svg
                  class="w-4 h-4 mr-2"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                确认进入
              </span>
            </button>
          </div>
        </div>
      </div>
    </Transition>

    <!-- 主内容区域 -->
    <div
      :class="[
        'min-h-screen transition-all duration-500',
        zenMode ? 'zen-mode' : 'py-8 px-4 sm:px-6 lg:px-8',
      ]"
    >
      <div
        :class="[
          'mx-auto transition-all duration-500',
          zenMode ? 'max-w-none' : 'max-w-4xl',
        ]"
      >
        <!-- 加载状态 -->
        <div v-if="loading" class="glass-effect rounded-2xl p-8 animate-pulse">
          <div class="h-8 bg-gray-300 rounded mb-4"></div>
          <div class="h-4 bg-gray-300 rounded mb-2"></div>
          <div class="h-4 bg-gray-300 rounded w-3/4 mb-8"></div>
          <div class="h-64 bg-gray-300 rounded"></div>
        </div>

        <!-- 禅模式退出按钮 -->
        <div v-if="zenMode" class="fixed top-6 right-6 z-40">
          <button
            @click="exitZenMode"
            class="group p-3 bg-white dark:bg-gray-800 bg-opacity-90 dark:bg-opacity-90 hover:bg-opacity-100 dark:hover:bg-opacity-100 text-gray-700 dark:text-gray-300 rounded-full backdrop-blur-sm transition-all duration-300 shadow-lg hover:shadow-xl border border-gray-200 dark:border-gray-600"
            title="退出禅模式"
          >
            <svg
              class="w-5 h-5 group-hover:rotate-90 transition-transform duration-300"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        <!-- 禅模式进入按钮 -->
        <div
          v-if="!zenMode && !loading && article"
          class="fixed bottom-6 right-6 z-40"
        >
          <button
            @click="showZenTip = true"
            class="group relative w-14 h-14 bg-white dark:bg-gray-800 text-gray-700 dark:text-gray-300 rounded-full shadow-lg hover:shadow-xl border border-gray-200 dark:border-gray-600 hover:border-purple-300 dark:hover:border-purple-500 transform hover:scale-105 transition-all duration-300 flex items-center justify-center"
            title="进入禅模式"
          >
            <span
              class="text-lg font-bold group-hover:scale-110 transition-transform duration-300"
              style="font-family: 'STKaiti', 'KaiTi', '楷体', serif"
            >
              禅
            </span>
            <!-- 悬浮提示 -->
            <div
              class="absolute bottom-full right-0 mb-2 px-3 py-1 bg-gray-900 dark:bg-gray-700 text-white text-xs rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-300 whitespace-nowrap"
            >
              进入禅模式
              <div
                class="absolute top-full right-4 w-0 h-0 border-l-4 border-r-4 border-t-4 border-transparent border-t-gray-900 dark:border-t-gray-700"
              ></div>
            </div>
          </button>
        </div>

        <!-- 文章内容 -->
        <article
          v-if="!loading && article"
          :class="[
            'overflow-hidden transition-all duration-500',
            zenMode
              ? 'zen-article fixed inset-0 bg-gradient-to-br from-amber-50 via-white to-orange-50'
              : 'glass-effect rounded-2xl card-hover',
          ]"
        >
          <!-- 文章头部 -->
          <header
            :class="[
              'border-b border-gray-200 transition-all duration-500',
              zenMode ? 'p-12 text-center' : 'p-8',
            ]"
          >
            <h1
              :class="[
                'font-bold text-gray-900 mb-4 transition-all duration-500',
                zenMode
                  ? 'text-4xl md:text-6xl leading-tight'
                  : 'text-3xl md:text-4xl',
              ]"
            >
              {{ article.title }}
            </h1>

            <!-- 文章元信息 -->
            <div
              v-if="!zenMode"
              class="flex flex-wrap items-center gap-4 text-sm text-gray-600 mb-6"
            >
              <div class="flex items-center">
                <UserIcon class="w-4 h-4 mr-1" />
                {{ article.author?.nickname || article.author?.username }}
              </div>
              <div class="flex items-center">
                <CalendarIcon class="w-4 h-4 mr-1" />
                {{ formatDate(article.publishedAt) }}
              </div>
              <div class="flex items-center">
                <EyeIcon class="w-4 h-4 mr-1" />
                {{ article.viewCount }} 次阅读
              </div>
            </div>

            <!-- 标签 -->
            <div
              v-if="!zenMode && article.tags?.length"
              class="flex flex-wrap gap-2 mb-6"
            >
              <span
                v-for="tag in article.tags"
                :key="tag.id"
                class="px-3 py-1 text-sm rounded-full"
                :style="{ backgroundColor: tag.color + '20', color: tag.color }"
              >
                # {{ tag.name }}
              </span>
            </div>

            <!-- 封面图片 -->
            <div v-if="!zenMode && article.coverImage" class="mb-6">
              <img
                :src="article.coverImage"
                :alt="article.title"
                class="w-full h-64 md:h-80 object-cover rounded-xl"
              />
            </div>
          </header>

          <!-- 文章正文 -->
          <div
            :class="[
              'transition-all duration-500',
              zenMode ? 'zen-content p-12 overflow-y-auto h-full' : 'p-8',
            ]"
          >
            <div
              :class="[
                'prose prose-slate dark:prose-invert max-w-none transition-all duration-500',
                zenMode ? 'zen-prose prose-xl md:prose-2xl' : 'prose-lg',
              ]"
              v-html="renderedContent"
            ></div>
          </div>

          <!-- 文章底部 -->
          <footer
            v-if="!zenMode"
            class="p-8 border-t border-gray-200 bg-gray-50"
          >
            <div class="text-sm text-gray-500 text-center">
              最后更新于 {{ formatDate(article.updatedAt) }}
            </div>
          </footer>
        </article>

        <!-- 文章不存在 -->
        <div
          v-if="!loading && !article"
          class="glass-effect rounded-2xl p-12 text-center"
        >
          <div class="text-gray-400 mb-4">
            <svg
              class="w-16 h-16 mx-auto"
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"
              />
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-gray-900 mb-2">文章不存在</h3>
          <p class="text-gray-600 mb-6">抱歉，您访问的文章不存在或已被删除</p>
          <RouterLink
            to="/"
            class="inline-flex items-center px-6 py-3 bg-blue-500 hover:bg-blue-600 text-white rounded-full transition-colors"
          >
            返回首页
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  ref,
  computed,
  watch,
  onMounted,
  onUnmounted,
  onBeforeUnmount,
} from "vue";
import { useRoute } from "vue-router";
import { articleApi } from "../api";
import { renderMarkdown } from "../utils/markdown";

// 图标组件
const UserIcon = {
  template:
    '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>',
};
const CalendarIcon = {
  template:
    '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z"/></svg>',
};
const EyeIcon = {
  template:
    '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"/></svg>',
};

const route = useRoute();

// 响应式数据
const article = ref(null);
const loading = ref(false);
const zenMode = ref(false);
const showZenTip = ref(false);
const zenInput = ref("");
const inputStatus = ref(""); // 'success' | 'error' | ''
const inputMessage = ref("");

// 渲染内容
const renderedContent = computed(() => {
  if (!article.value?.content) return "";
  const rendered = renderMarkdown(article.value.content);
  console.log("Markdown rendered:", rendered.substring(0, 200) + "...");
  return rendered;
});

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return "";
  const date = new Date(dateString);
  return date.toLocaleDateString("zh-CN", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
};

// 禅模式相关方法
const closeZenTip = () => {
  showZenTip.value = false;
  zenInput.value = "";
  inputStatus.value = "";
  inputMessage.value = "";
};

// 检查输入的文本
const checkZenInput = () => {
  const input = zenInput.value.trim();

  if (!input) {
    inputStatus.value = "error";
    inputMessage.value = "请输入内容";
    return;
  }

  // 检查是否包含"阿弥陀佛" - 支持多种变体
  const keywords = ["阿弥陀佛", "阿弥陀", "南无阿弥陀佛", "南无阿弥陀"];
  const hasKeyword = keywords.some(
    (keyword) =>
      input.includes(keyword) || input.replace(/\s+/g, "").includes(keyword)
  );

  if (hasKeyword) {
    inputStatus.value = "success";
    inputMessage.value = "验证成功！正在进入禅模式...";

    // 延迟1秒后进入禅模式
    setTimeout(() => {
      enterZenMode();
    }, 1000);
  } else {
    inputStatus.value = "error";
    inputMessage.value = "请输入正确的内容：阿弥陀佛";
  }
};

const enterZenMode = () => {
  showZenTip.value = false;
  zenMode.value = true;
  // 隐藏页面滚动条
  document.body.style.overflow = "hidden";
  // 通知父组件隐藏导航栏
  document.body.classList.add("zen-mode-active");
  // 重置输入状态
  zenInput.value = "";
  inputStatus.value = "";
  inputMessage.value = "";
};

const exitZenMode = () => {
  zenMode.value = false;
  // 恢复页面滚动条
  document.body.style.overflow = "";
  // 通知父组件显示导航栏
  document.body.classList.remove("zen-mode-active");
};

// 键盘快捷键支持
const handleKeydown = (event) => {
  if (event.key === "Escape" && zenMode.value) {
    exitZenMode();
  }
  if (event.key === "z" && event.ctrlKey && !zenMode.value && article.value) {
    showZenTip.value = true;
  }
};

// 组件挂载时添加键盘监听

onMounted(() => {
  document.addEventListener("keydown", handleKeydown);
});

// 在组件卸载前清理状态
onBeforeUnmount(() => {
  // 立即清理禅模式状态
  if (zenMode.value) {
    exitZenMode();
  }
});

onUnmounted(() => {
  document.removeEventListener("keydown", handleKeydown);
  // 强制清理全局状态，防止影响其他页面
  document.body.classList.remove("zen-mode-active");
  document.body.style.overflow = "";
});

// 加载文章数据
const loadArticle = async () => {
  try {
    loading.value = true;
    article.value = null;
    const data = await articleApi.getDetail(route.params.id);
    article.value = data;
  } catch (error) {
    console.error("加载文章失败:", error);
    article.value = null;
  } finally {
    loading.value = false;
  }
};

// 监听路由参数变化，重新加载文章
watch(
  () => route.params.id,
  (newId) => {
    // 路由切换时，先清理禅模式状态
    if (zenMode.value) {
      exitZenMode();
    }
    if (newId) {
      loadArticle();
    }
  },
  { immediate: true }
);
</script>

<style scoped>
/* 自定义 prose 样式覆盖 */
.prose {
  @apply text-gray-800;
}

/* 标题锚点样式 */
.prose :deep(.header-anchor) {
  @apply text-gray-400 hover:text-blue-500 no-underline transition-all duration-200;
}

/* 禅模式样式 */
.zen-mode {
  @apply p-0 bg-gradient-to-br from-amber-50 via-white to-orange-50;
}

.zen-article {
  @apply z-30;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: rgba(156, 163, 175, 0.5) transparent;
}

.zen-article::-webkit-scrollbar {
  width: 8px;
}

.zen-article::-webkit-scrollbar-track {
  background: transparent;
}

.zen-article::-webkit-scrollbar-thumb {
  background-color: rgba(156, 163, 175, 0.5);
  border-radius: 4px;
}

.zen-article::-webkit-scrollbar-thumb:hover {
  background-color: rgba(156, 163, 175, 0.7);
}

.zen-content {
  padding-top: 2rem;
}

.zen-prose {
  @apply text-gray-800 dark:text-gray-200 leading-relaxed;
  line-height: 1.8;
  font-family: "Georgia", "Times New Roman", serif;
}

.zen-prose :deep(h1),
.zen-prose :deep(h2),
.zen-prose :deep(h3) {
  @apply text-gray-900 dark:text-gray-100 mb-6 mt-8;
  font-family: "Georgia", "Times New Roman", serif;
}

.zen-prose :deep(p) {
  @apply mb-6 text-lg leading-relaxed;
}

.zen-prose :deep(blockquote) {
  @apply border-l-4 border-amber-400 pl-6 italic text-gray-700 dark:text-gray-300;
}

/* 弹窗动画 */
.modal-enter-active,
.modal-leave-active {
  transition: all 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
  transform: scale(0.9);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .prose {
    @apply text-sm;
  }

  .zen-prose {
    @apply text-base;
  }
}
</style>

<style>
/* 全局样式：禅模式时隐藏导航栏和页脚 */
body.zen-mode-active header,
body.zen-mode-active footer {
  display: none !important;
}

body.zen-mode-active {
  overflow: hidden !important;
}

/* 确保禅模式下main占满全屏 */
body.zen-mode-active #app {
  height: 100vh !important;
}

body.zen-mode-active #app > main {
  height: 100vh !important;
  padding: 0 !important;
  margin: 0 !important;
}

/* 确保禅模式不影响其他交互 */
body.zen-mode-active a,
body.zen-mode-active button:not(.zen-mode button) {
  pointer-events: auto;
}
</style>
