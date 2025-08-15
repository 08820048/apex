<template>
  <div class="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold text-gray-900 dark:text-white mb-4">我的作品集</h1>
        <p class="text-xl text-gray-600 dark:text-white/80">展示我的项目和技术成果</p>
      </div>

      <!-- 精选作品 -->
      <div v-if="featuredPortfolios.length > 0" class="mb-12">
        <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-6">精选作品</h2>
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
          <div
            v-for="portfolio in featuredPortfolios"
            :key="portfolio.id"
            class="glass-effect rounded-2xl overflow-hidden card-hover group"
          >
            <div v-if="portfolio.coverImage" class="relative h-48 overflow-hidden">
              <img
                :src="portfolio.coverImage"
                :alt="portfolio.name"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
              >
              <div class="absolute top-4 right-4">
                <span class="bg-yellow-500 text-white px-3 py-1 rounded-full text-sm font-medium">
                  精选
                </span>
              </div>
            </div>
            <div class="p-6">
              <h3 class="text-xl font-bold text-gray-900 mb-3 group-hover:text-blue-600 transition-colors">
                {{ portfolio.name }}
              </h3>
              <p class="text-gray-600 mb-4 line-clamp-3">
                {{ portfolio.description }}
              </p>
              <div v-if="portfolio.technologies" class="mb-4">
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="tech in portfolio.technologies.split(',')"
                    :key="tech.trim()"
                    class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full"
                  >
                    {{ tech.trim() }}
                  </span>
                </div>
              </div>
              <div class="flex items-center justify-between">
                <span class="text-sm text-gray-500">
                  {{ formatDate(portfolio.createdAt) }}
                </span>
                <a
                  v-if="portfolio.url"
                  :href="portfolio.url"
                  target="_blank"
                  rel="noopener noreferrer"
                  class="flex items-center text-blue-600 hover:text-blue-800 transition-colors"
                  @click.stop
                >
                  <span class="mr-1">查看项目</span>
                  <ExternalLinkIcon class="w-4 h-4" />
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 所有作品 -->
      <div>
        <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-6">所有作品</h2>
        
        <!-- 加载状态 -->
        <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div v-for="i in 6" :key="i" class="glass-effect rounded-2xl p-6 animate-pulse">
            <div class="h-32 bg-gray-300 rounded mb-4"></div>
            <div class="h-6 bg-gray-300 rounded mb-3"></div>
            <div class="h-4 bg-gray-300 rounded mb-2"></div>
            <div class="h-4 bg-gray-300 rounded w-2/3"></div>
          </div>
        </div>

        <!-- 作品网格 -->
        <div v-else-if="portfolios.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="portfolio in portfolios"
            :key="portfolio.id"
            class="glass-effect rounded-2xl overflow-hidden card-hover group"
          >
            <div v-if="portfolio.coverImage" class="relative h-32 overflow-hidden">
              <img
                :src="portfolio.coverImage"
                :alt="portfolio.name"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
              >
              <div v-if="portfolio.isFeatured" class="absolute top-2 right-2">
                <span class="bg-yellow-500 text-white px-2 py-1 rounded-full text-xs font-medium">
                  精选
                </span>
              </div>
            </div>
            <div class="p-4">
              <h3 class="text-lg font-bold text-gray-900 mb-2 group-hover:text-blue-600 transition-colors">
                {{ portfolio.name }}
              </h3>
              <p class="text-gray-600 text-sm mb-3 line-clamp-2">
                {{ portfolio.description }}
              </p>
              <div v-if="portfolio.technologies" class="mb-3">
                <div class="flex flex-wrap gap-1">
                  <span
                    v-for="tech in portfolio.technologies.split(',').slice(0, 3)"
                    :key="tech.trim()"
                    class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full"
                  >
                    {{ tech.trim() }}
                  </span>
                  <span
                    v-if="portfolio.technologies.split(',').length > 3"
                    class="px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded-full"
                  >
                    +{{ portfolio.technologies.split(',').length - 3 }}
                  </span>
                </div>
              </div>
              <div class="flex items-center justify-between">
                <span class="text-xs text-gray-500">
                  {{ formatDate(portfolio.createdAt) }}
                </span>
                <a
                  v-if="portfolio.url"
                  :href="portfolio.url"
                  target="_blank"
                  rel="noopener noreferrer"
                  class="flex items-center text-blue-600 hover:text-blue-800 transition-colors text-sm"
                  @click.stop
                >
                  <span class="mr-1">查看</span>
                  <ExternalLinkIcon class="w-3 h-3" />
                </a>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else class="glass-effect rounded-2xl p-12 text-center">
          <div class="text-gray-400 mb-4">
            <FolderIcon class="w-16 h-16 mx-auto" />
          </div>
          <h3 class="text-lg font-semibold text-gray-900 mb-2">暂无作品</h3>
          <p class="text-gray-600">还没有添加任何作品，请稍后再来查看。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { portfolioApi } from '../api'

// 图标组件
const ExternalLinkIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M19 19H5V5h7V3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2v-7h-2v7zM14 3v2h3.59l-9.83 9.83 1.41 1.41L19 6.41V10h2V3h-7z"/></svg>' }
const FolderIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M10 4H4c-1.11 0-2 .89-2 2v12c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V8c0-1.11-.89-2-2-2h-8l-2-2z"/></svg>' }

// 响应式数据
const portfolios = ref([])
const featuredPortfolios = ref([])
const loading = ref(false)

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 加载所有作品集
const loadPortfolios = async () => {
  try {
    loading.value = true
    const data = await portfolioApi.getList()
    portfolios.value = data || []
  } catch (error) {
    console.error('加载作品集失败:', error)
    portfolios.value = []
  } finally {
    loading.value = false
  }
}

// 加载精选作品集
const loadFeaturedPortfolios = async () => {
  try {
    const data = await portfolioApi.getFeatured()
    featuredPortfolios.value = data || []
  } catch (error) {
    console.error('加载精选作品集失败:', error)
    featuredPortfolios.value = []
  }
}

// 组件挂载时加载数据
onMounted(async () => {
  try {
    await Promise.all([
      loadPortfolios(),
      loadFeaturedPortfolios()
    ])
  } catch (error) {
    console.error('加载页面数据失败:', error)
  }
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
