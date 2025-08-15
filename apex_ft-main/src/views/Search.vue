<template>
  <div class="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- 搜索结果标题 -->
      <div class="glass-effect rounded-2xl p-6 mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-2xl font-bold text-gray-900 mb-2">
              {{ getSearchTitle() }}
            </h1>
            <p class="text-gray-600">
              {{ loading ? '搜索中...' : `共找到 ${totalElements} 篇文章` }}
            </p>
          </div>
          <div v-if="searchQuery" class="text-right">
            <div class="px-4 py-2 bg-blue-100 text-blue-800 rounded-full text-sm">
              "{{ searchQuery }}"
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索结果列表 -->
      <div v-if="loading" class="space-y-6">
        <div v-for="i in 5" :key="i" class="glass-effect rounded-2xl p-6 animate-pulse">
          <div class="flex gap-6">
            <div class="w-32 h-24 bg-gray-300 rounded-xl"></div>
            <div class="flex-1">
              <div class="h-6 bg-gray-300 rounded mb-3"></div>
              <div class="h-4 bg-gray-300 rounded mb-2"></div>
              <div class="h-4 bg-gray-300 rounded w-3/4"></div>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="articles.length > 0" class="space-y-6">
        <article 
          v-for="article in articles" 
          :key="article.id"
          class="glass-effect rounded-2xl p-6 card-hover cursor-pointer"
          @click="goToArticle(article.id)"
        >
          <div class="flex gap-6">
            <!-- 文章封面 -->
            <div v-if="article.coverImage" class="flex-shrink-0">
              <img 
                :src="article.coverImage" 
                :alt="article.title"
                class="w-32 h-24 object-cover rounded-xl"
              >
            </div>
            
            <!-- 文章内容 -->
            <div class="flex-1">
              <h3 class="text-xl font-bold text-gray-900 mb-3 hover:text-blue-600 transition-colors">
                {{ article.title }}
              </h3>
              
              <p class="text-gray-600 mb-4 line-clamp-2">
                {{ article.summary }}
              </p>
              
              <!-- 文章标签 -->
              <div class="flex flex-wrap gap-2 mb-3">
                <span 
                  v-for="tag in article.tags?.slice(0, 3)" 
                  :key="tag.id"
                  class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full"
                >
                  {{ tag.name }}
                </span>
              </div>
              
              <!-- 文章元信息 -->
              <div class="flex items-center justify-between text-xs text-gray-500">
                <div class="flex items-center gap-4">
                  <span>{{ article.authorName }}</span>
                  <span>{{ formatDate(article.publishedAt) }}</span>
                  <span v-if="article.category" class="px-2 py-1 bg-gray-100 text-gray-700 rounded">
                    {{ article.category.name }}
                  </span>
                </div>
                <div class="flex items-center gap-4">
                  <span>{{ article.viewCount }} 阅读</span>

                </div>
              </div>
            </div>
          </div>
        </article>
      </div>

      <!-- 空状态 -->
      <div v-else class="glass-effect rounded-2xl p-12 text-center">
        <div class="text-gray-400 mb-4">
          <SearchIcon class="w-16 h-16 mx-auto" />
        </div>
        <h3 class="text-lg font-semibold text-gray-900 mb-2">没有找到相关文章</h3>
        <p class="text-gray-600 mb-6">
          {{ searchQuery ? `没有找到包含 "${searchQuery}" 的文章` : '没有找到相关文章' }}
        </p>
        <RouterLink 
          to="/"
          class="inline-flex items-center px-6 py-3 bg-blue-500 hover:bg-blue-600 text-white rounded-full transition-colors"
        >
          返回首页
        </RouterLink>
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="flex justify-center mt-8">
        <div class="flex items-center space-x-2">
          <button 
            @click="goToPage(currentPage - 1)"
            :disabled="currentPage <= 1"
            class="px-4 py-2 bg-white/20 hover:bg-white/30 text-white rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            上一页
          </button>
          
          <div class="flex space-x-1">
            <button 
              v-for="page in getPageNumbers()" 
              :key="page"
              @click="goToPage(page)"
              :class="[
                'px-3 py-2 rounded-lg transition-colors',
                page === currentPage 
                  ? 'bg-white text-gray-900' 
                  : 'bg-white/20 hover:bg-white/30 text-white'
              ]"
            >
              {{ page }}
            </button>
          </div>
          
          <button 
            @click="goToPage(currentPage + 1)"
            :disabled="currentPage >= totalPages"
            class="px-4 py-2 bg-white/20 hover:bg-white/30 text-white rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            下一页
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { articleApi } from '../api'

// 图标组件
const SearchIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/></svg>' }

const route = useRoute()
const router = useRouter()

// 响应式数据
const articles = ref([])
const loading = ref(false)
const currentPage = ref(1)
const totalPages = ref(0)
const totalElements = ref(0)
const size = 10

// 计算属性
const searchQuery = computed(() => route.query.q || '')

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

// 获取搜索标题
const getSearchTitle = () => {
  if (route.query.q) {
    return '搜索结果'
  } else if (route.query.category) {
    return '分类文章'
  } else if (route.query.tag) {
    return '标签文章'
  }
  return '搜索结果'
}

// 跳转到文章详情
const goToArticle = (id) => {
  router.push({ name: 'article', params: { id } })
}

// 跳转到指定页面
const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    loadResults(page)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

// 获取页码数组
const getPageNumbers = () => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, currentPage.value + 2)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  
  return pages
}

// 加载搜索结果
const loadResults = async (page = 1) => {
  try {
    loading.value = true
    let data
    
    if (route.query.q) {
      // 关键词搜索
      data = await articleApi.search(route.query.q, { page: page - 1, size })
    } else if (route.query.category) {
      // 分类搜索
      data = await articleApi.getByCategory(route.query.category, { page: page - 1, size })
    } else if (route.query.tag) {
      // 标签搜索
      data = await articleApi.getByTag(route.query.tag, { page: page - 1, size })
    }
    
    if (data) {
      articles.value = data.content || []
      currentPage.value = (data.page || 0) + 1
      totalPages.value = data.totalPages || 0
      totalElements.value = data.totalElements || 0
    }
  } catch (error) {
    console.error('获取搜索结果失败:', error)
    articles.value = []
  } finally {
    loading.value = false
  }
}

// 监听路由变化
watch(() => route.fullPath, () => {
  currentPage.value = 1
  loadResults()
})

// 组件挂载时加载数据
onMounted(() => loadResults())
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
