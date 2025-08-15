<template>
  <div class="min-h-screen">
    <!-- 主要内容区域 -->
    <div class="max-w-7xl mx-auto px-4 py-8">
      <div class="flex flex-col lg:flex-row gap-8 lg:items-start">
        <!-- 主内容区 -->
        <div class="flex-1 lg:max-w-3xl">
          <!-- 文章列表 -->
          <div class="lg:min-h-[750px] flex flex-col">
            <!-- 加载状态 -->
            <Transition name="loading">
              <div v-if="loading" class="space-y-6">
                <div
                  v-for="i in 3"
                  :key="i"
                  class="glass-effect p-6 animate-pulse"
                >
                  <div class="flex gap-4">
                    <div class="w-32 h-24 bg-gray-300 rounded"></div>
                    <div class="flex-1">
                      <div class="h-6 bg-gray-300 rounded mb-3"></div>
                      <div class="h-4 bg-gray-300 rounded mb-2"></div>
                      <div class="h-4 bg-gray-300 rounded w-3/4"></div>
                    </div>
                  </div>
                </div>
              </div>
            </Transition>

            <!-- 文章列表 -->
            <TransitionGroup
              v-if="articles.length > 0"
              name="list"
              tag="div"
              class="flex-1 flex flex-col gap-4"
            >
              <article
                v-for="article in articles"
                :key="article.id"
                :class="[
                  'glass-effect p-5 cursor-pointer card-hover flex-1 min-h-[160px] flex flex-col justify-between relative group',
                  article.isTop ? 'top-article' : '',
                ]"
                :style="getArticleStyle(article)"
                @click="goToArticle(article.id)"
              >
                <!-- 置顶提示 - 仅在悬停时显示 -->
                <div
                  v-if="article.isTop"
                  class="absolute top-2 right-2 z-10 top-tooltip opacity-0 group-hover:opacity-100 transition-all duration-300 transform translate-x-2 group-hover:translate-x-0"
                >
                  <div
                    class="bg-red-500 text-white px-2 py-1 text-xs font-medium rounded shadow-lg"
                  >
                    置顶
                  </div>
                </div>
                <div class="flex gap-4 h-full">
                  <div
                    v-if="article.coverImage"
                    class="w-32 h-28 flex-shrink-0"
                  >
                    <img
                      :src="article.coverImage"
                      :alt="article.title"
                      class="w-full h-full object-cover rounded"
                    />
                  </div>
                  <div class="flex-1 flex flex-col justify-between">
                    <div>
                      <h3
                        class="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2 hover:text-blue-600 dark:hover:text-blue-400 transition-colors line-clamp-2"
                      >
                        {{ article.title }}
                      </h3>
                      <p
                        class="text-gray-600 dark:text-gray-300 line-clamp-3 mb-3 flex-1"
                      >
                        {{ article.summary }}
                      </p>
                    </div>
                    <div class="flex items-center justify-between mt-auto">
                      <div
                        class="flex items-center space-x-4 text-sm text-gray-500 dark:text-gray-400"
                      >
                        <span>{{ formatDate(article.publishedAt) }}</span>
                        <span>{{ article.viewCount }} 阅读</span>
                        <span
                          v-if="article.authorName"
                          class="flex items-center"
                        >
                          <svg
                            class="w-4 h-4 mr-1"
                            fill="currentColor"
                            viewBox="0 0 24 24"
                          >
                            <path
                              d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"
                            />
                          </svg>
                          {{ article.authorName }}
                        </span>
                      </div>
                      <div class="flex items-center gap-2">
                        <!-- 文章分类 -->
                        <span
                          v-if="article.category"
                          class="px-2 py-1 text-xs bg-gradient-to-r from-purple-100 to-purple-200 text-purple-700 dark:from-purple-900 dark:to-purple-800 dark:text-purple-300 rounded-full font-medium border border-purple-200 dark:border-purple-700"
                        >
                          <svg
                            class="w-3 h-3 inline mr-1"
                            fill="currentColor"
                            viewBox="0 0 24 24"
                          >
                            <path d="M4 6h16v2H4zm0 5h16v2H4zm0 5h16v2H4z" />
                          </svg>
                          {{ article.category.name }}
                        </span>
                        <!-- 标签 -->
                        <div class="flex flex-wrap gap-2">
                          <span
                            v-for="tag in article.tags?.slice(0, 6)"
                            :key="tag.id"
                            class="px-2 py-1 text-xs rounded"
                            :style="{
                              backgroundColor: tag.color + '20',
                              color: tag.color,
                            }"
                          >
                            #{{ tag.name }}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </article>
            </TransitionGroup>

            <!-- 空状态 -->
            <div
              v-else-if="!loading && articles.length === 0"
              class="glass-effect p-12 text-center"
            >
              <div class="text-gray-400 dark:text-gray-500 mb-4">
                <svg
                  class="w-16 h-16 mx-auto"
                  fill="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"
                  />
                </svg>
              </div>
              <h3
                class="text-lg font-semibold text-gray-900 dark:text-gray-100 mb-2"
              >
                暂无文章
              </h3>
              <p class="text-gray-600 dark:text-gray-300">
                还没有发布任何文章，请稍后再来查看。
              </p>
            </div>
          </div>
        </div>

        <!-- 侧边栏 -->
        <div
          class="w-full lg:w-80 flex-shrink-0 lg:min-h-[850px] flex flex-col"
        >
          <!-- 热门标签 -->
          <div class="glass-effect p-6 mb-6">
            <h3
              class="text-lg font-semibold text-gray-900 dark:text-gray-100 mb-4 flex items-center"
            >
              <TagIcon class="w-5 h-5 mr-2 text-blue-600 dark:text-blue-400" />
              热门标签
            </h3>
            <TransitionGroup
              v-if="popularTags.length > 0"
              name="list"
              tag="div"
              class="flex flex-wrap gap-2"
            >
              <span
                v-for="tag in popularTags"
                :key="tag.id"
                @click="goToTag(tag.id)"
                class="px-3 py-1 text-sm rounded-full cursor-pointer tag-hover"
                :style="{ backgroundColor: tag.color + '20', color: tag.color }"
              >
                {{ tag.name }}
              </span>
            </TransitionGroup>
            <div v-else class="text-gray-500 dark:text-gray-400">暂无标签</div>
          </div>

          <!-- 博客统计 -->
          <div class="glass-effect p-6 mb-6">
            <h3
              class="text-lg font-semibold text-gray-900 mb-4 flex items-center"
            >
              <img
                src="/apex.jpg"
                alt="ApexBlog Logo"
                class="w-6 h-6 rounded-full object-cover mr-2 shadow-sm"
              />
              博客统计
            </h3>
            <div class="grid grid-cols-2 gap-3">
              <div
                class="text-center p-4 bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg border border-blue-200 hover:shadow-md transition-shadow"
              >
                <div class="text-2xl font-bold text-blue-600 mb-1">
                  {{ stats.articleCount }}
                </div>
                <div class="text-xs text-gray-600">文章总数</div>
              </div>
              <div
                class="text-center p-4 bg-gradient-to-br from-green-50 to-green-100 rounded-lg border border-green-200 hover:shadow-md transition-shadow"
              >
                <div class="text-2xl font-bold text-green-600 mb-1">
                  {{ stats.viewCount }}
                </div>
                <div class="text-xs text-gray-600">总访问量</div>
              </div>
              <div
                class="text-center p-4 bg-gradient-to-br from-purple-50 to-purple-100 rounded-lg border border-purple-200 hover:shadow-md transition-shadow"
              >
                <div class="text-2xl font-bold text-purple-600 mb-1">
                  {{ stats.subscriberCount }}
                </div>
                <div class="text-xs text-gray-600">订阅用户</div>
              </div>
              <div
                class="text-center p-4 bg-gradient-to-br from-red-50 to-red-100 rounded-lg border border-red-200 hover:shadow-md transition-shadow"
              >
                <div class="text-2xl font-bold text-red-600 mb-1">
                  {{ stats.friendLinkCount }}
                </div>
                <div class="text-xs text-gray-600">友链数量</div>
              </div>
            </div>
          </div>

          <!-- 分页 -->
          <Transition name="fade">
            <div v-if="totalPages > 1" class="glass-effect p-6">
              <h3
                class="text-lg font-semibold text-gray-900 dark:text-gray-100 mb-4 flex items-center"
              >
                <svg
                  class="w-5 h-5 mr-2 text-blue-600 dark:text-blue-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M7 4V2a1 1 0 011-1h8a1 1 0 011 1v2h4a1 1 0 011 1v2a1 1 0 01-1 1h-1v12a2 2 0 01-2 2H6a2 2 0 01-2-2V8H3a1 1 0 01-1-1V5a1 1 0 011-1h4zM9 3v1h6V3H9zm0 5v10h6V8H9z"
                  />
                </svg>
                页面导航
              </h3>

              <!-- 分页信息 -->
              <div
                class="text-center p-4 bg-gradient-to-br from-blue-50 to-blue-100 rounded-lg border border-blue-200 mb-4"
              >
                <div class="text-sm font-medium text-blue-800 mb-1">
                  第 {{ currentPage }} / {{ totalPages }} 页
                </div>
                <div class="text-xs text-blue-600">
                  共 {{ totalElements }} 篇文章
                </div>
              </div>

              <!-- 分页按钮 -->
              <div class="space-y-3">
                <!-- 上一页 -->
                <button
                  @click="changePage(currentPage - 1)"
                  :disabled="currentPage <= 1"
                  class="w-full px-4 py-2 text-sm font-medium text-white bg-gradient-to-r from-blue-500 to-blue-600 rounded-lg hover:from-blue-600 hover:to-blue-700 disabled:from-gray-300 disabled:to-gray-400 disabled:cursor-not-allowed transition-all duration-200 shadow-md hover:shadow-lg disabled:shadow-none"
                >
                  <svg
                    class="w-4 h-4 inline mr-2"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M15 19l-7-7 7-7"
                    />
                  </svg>
                  上一页
                </button>

                <!-- 页码按钮 -->
                <div v-if="totalPages <= 4" class="grid grid-cols-4 gap-2">
                  <!-- 简单分页：页数少于等于4页时显示所有页码 -->
                  <button
                    v-for="page in totalPages"
                    :key="page"
                    @click="changePage(page)"
                    :class="[
                      'px-2 py-2 text-xs font-medium rounded-lg transition-all duration-200',
                      page === currentPage
                        ? 'bg-gradient-to-r from-blue-500 to-blue-600 text-white shadow-md'
                        : 'text-gray-700 bg-white border border-gray-200 hover:bg-gradient-to-r hover:from-blue-50 hover:to-blue-100 hover:border-blue-300',
                    ]"
                  >
                    {{ page }}
                  </button>
                </div>

                <!-- 复杂分页：页数大于4页时使用跳转输入框 -->
                <div v-else class="space-y-3">
                  <div class="flex items-center gap-2">
                    <!-- 第一页按钮 -->
                    <button
                      @click="changePage(1)"
                      :class="[
                        'px-3 py-2 text-xs font-medium rounded-lg transition-all duration-200 flex-shrink-0',
                        currentPage === 1
                          ? 'bg-gradient-to-r from-blue-500 to-blue-600 text-white shadow-md'
                          : 'text-gray-700 bg-white border border-gray-200 hover:bg-gradient-to-r hover:from-blue-50 hover:to-blue-100 hover:border-blue-300',
                      ]"
                    >
                      1
                    </button>

                    <!-- 跳转输入框和按钮 -->
                    <div class="flex items-center gap-1 flex-1">
                      <input
                        v-model="jumpToPage"
                        @keyup.enter="handleJumpToPage"
                        type="number"
                        :min="1"
                        :max="totalPages"
                        placeholder="页码"
                        class="flex-1 px-2 py-1 text-xs border border-gray-200 rounded focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500"
                      />
                      <button
                        @click="handleJumpToPage"
                        class="px-2 py-1 text-xs font-medium text-white bg-gradient-to-r from-green-500 to-green-600 rounded hover:from-green-600 hover:to-green-700 transition-all duration-200 flex-shrink-0"
                      >
                        跳转
                      </button>
                    </div>

                    <!-- 最后一页按钮 -->
                    <button
                      @click="changePage(totalPages)"
                      :class="[
                        'px-3 py-2 text-xs font-medium rounded-lg transition-all duration-200 flex-shrink-0',
                        currentPage === totalPages
                          ? 'bg-gradient-to-r from-blue-500 to-blue-600 text-white shadow-md'
                          : 'text-gray-700 bg-white border border-gray-200 hover:bg-gradient-to-r hover:from-blue-50 hover:to-blue-100 hover:border-blue-300',
                      ]"
                    >
                      {{ totalPages }}
                    </button>
                  </div>

                  <!-- 当前页面提示 -->
                  <div class="text-center text-xs text-gray-500">
                    当前第 {{ currentPage }} 页
                  </div>
                </div>

                <!-- 下一页 -->
                <button
                  @click="changePage(currentPage + 1)"
                  :disabled="currentPage >= totalPages"
                  class="w-full px-4 py-2 text-sm font-medium text-white bg-gradient-to-r from-blue-500 to-blue-600 rounded-lg hover:from-blue-600 hover:to-blue-700 disabled:from-gray-300 disabled:to-gray-400 disabled:cursor-not-allowed transition-all duration-200 shadow-md hover:shadow-lg disabled:shadow-none"
                >
                  下一页
                  <svg
                    class="w-4 h-4 inline ml-2"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M9 5l7 7-7 7"
                    />
                  </svg>
                </button>
              </div>
            </div>
          </Transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { articleApi, tagApi, statsApi } from "../api";

// 图标组件
const TagIcon = {
  template: `<svg viewBox="0 0 24 24" fill="currentColor" class="w-5 h-5">
    <path d="M17.63,5.84C17.27,5.33 16.67,5 16,5L5,5.01C3.9,5.01 3,5.9 3,7V17C3,18.1 3.9,18.99 5,18.99L16,19C16.67,19 17.27,18.67 17.63,18.16L22,12L17.63,5.84Z" />
  </svg>`,
};

const router = useRouter();

// 响应式数据
const articles = ref([]);
const popularTags = ref([]);
const stats = ref({
  articleCount: 0,
  viewCount: 0,
  tagCount: 0,
  categoryCount: 0,
});
const loading = ref(false);
const currentPage = ref(1);
const totalPages = ref(0);
const totalElements = ref(0);
const pageSize = 5;
const jumpToPage = ref(""); // 跳转页面输入框

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

// 生成文章的个性化边框样式
const getArticleStyle = (article) => {
  if (!article.tags || article.tags.length === 0) {
    // 如果没有标签，使用默认的灰色边框
    const bgColor = "white";
    return {
      background: `
        linear-gradient(${bgColor}, ${bgColor}) padding-box,
        linear-gradient(135deg, #6b7280, #9ca3af) border-box
      `,
      border: "2px solid transparent",
      boxShadow: `0 0 10px rgba(107, 114, 128, 0.1)`,
    };
  }

  // 提取标签颜色
  const tagColors = article.tags
    .slice(0, 3)
    .map((tag) => tag.color || "#6b7280");

  const bgColor = "white";

  // 如果只有一个标签，创建该颜色的渐变
  if (tagColors.length === 1) {
    const color = tagColors[0];
    return {
      background: `
        linear-gradient(${bgColor}, ${bgColor}) padding-box,
        linear-gradient(135deg, ${color}, ${color}88, ${color}44) border-box
      `,
      border: "2px solid transparent",
      boxShadow: `0 0 15px ${color}15`,
    };
  }

  // 多个标签时，创建彩色渐变
  const gradientColors = tagColors.join(", ");
  const shadowColor = tagColors[0]; // 使用第一个标签颜色作为阴影

  return {
    background: `
      linear-gradient(${bgColor}, ${bgColor}) padding-box,
      linear-gradient(135deg, ${gradientColors}) border-box
    `,
    border: "2px solid transparent",
    boxShadow: `0 0 15px ${shadowColor}15`,
  };
};

// 跳转到文章详情
const goToArticle = (id) => {
  router.push({ name: "article", params: { id } });
};

// 跳转到标签页面
const goToTag = (tagId) => {
  router.push({ name: "search", query: { tag: tagId } });
};

// 加载文章列表
const loadArticles = async (page = 1) => {
  try {
    loading.value = true;
    const data = await articleApi.getList({
      page: page - 1,
      size: pageSize,
    });

    articles.value = data.content || [];
    currentPage.value = page;
    totalPages.value = data.totalPages || 0;
    totalElements.value = data.totalElements || 0;
  } catch (error) {
    console.error("加载文章失败:", error);
    articles.value = [];
  } finally {
    loading.value = false;
  }
};

// 切换页面
const changePage = (page) => {
  if (page !== currentPage.value && page >= 1 && page <= totalPages.value) {
    loadArticles(page);
    // 滚动到顶部
    window.scrollTo({ top: 0, behavior: "smooth" });
  }
};

// 跳转到指定页面
const handleJumpToPage = () => {
  const page = parseInt(jumpToPage.value);
  if (page && page >= 1 && page <= totalPages.value) {
    changePage(page);
    jumpToPage.value = ""; // 清空输入框
  } else {
    // 输入无效时清空输入框
    jumpToPage.value = "";
  }
};

// 加载热门标签
const loadPopularTags = async () => {
  try {
    const data = await tagApi.getPopular();
    popularTags.value = data || [];
  } catch (error) {
    console.error("加载热门标签失败:", error);
    popularTags.value = [];
  }
};

// 加载博客统计
const loadStats = async () => {
  try {
    // 使用新的综合统计接口
    const statsData = await statsApi.getOverview();

    stats.value = {
      articleCount: statsData.totalArticles || 0,
      viewCount: statsData.totalVisits || 0,
      subscriberCount: statsData.totalSubscribers || 0,
      friendLinkCount: statsData.totalFriendLinks || 0,
    };
  } catch (error) {
    console.error("加载统计数据失败:", error);
    // 失败时使用静态数据作为后备
    stats.value = {
      articleCount: 5,
      viewCount: 1250,
      subscriberCount: 12,
      friendLinkCount: 6,
    };
  }
};

// 组件挂载时加载数据
onMounted(async () => {
  try {
    await Promise.all([loadArticles(), loadPopularTags(), loadStats()]);
  } catch (error) {
    console.error("加载页面数据失败:", error);
  }
});
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
