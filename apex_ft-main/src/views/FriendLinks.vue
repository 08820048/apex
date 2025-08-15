<template>
  <div class="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold text-gray-900 dark:text-white mb-4">友情链接</h1>
        <p class="text-xl text-gray-600 dark:text-white/80">与优秀的朋友们一起成长</p>
      </div>

      <!-- 友链申请说明 -->
      <div class="glass-effect rounded-2xl p-6 mb-8">
        <h2 class="text-xl font-bold text-gray-900 mb-4">友链申请</h2>
        <div class="text-gray-600 space-y-2">
          <p>欢迎与我交换友情链接！申请前请确保：</p>
          <ul class="list-disc list-inside space-y-1 ml-4">
            <li>网站内容积极向上，无违法违规内容</li>
            <li>网站能够正常访问，更新频率较高</li>
            <li>优先考虑技术类、学习类博客</li>
          </ul>
          <p class="mt-4">
            申请方式：请发送邮件至 
            <a href="mailto:ilikexff@gmail.com" class="text-blue-600 hover:text-blue-800">ilikexff@gmail.com</a>
            ，包含网站名称、网站地址、网站描述和站长信息。
          </p>
        </div>
      </div>

      <!-- 友链列表 -->
      <div>
        <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-6">友站推荐</h2>
        
        <!-- 加载状态 -->
        <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          <div v-for="i in 8" :key="i" class="glass-effect rounded-2xl p-6 animate-pulse">
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-gray-300 rounded-full mr-4"></div>
              <div class="flex-1">
                <div class="h-5 bg-gray-300 rounded mb-2"></div>
                <div class="h-4 bg-gray-300 rounded w-2/3"></div>
              </div>
            </div>
            <div class="h-4 bg-gray-300 rounded mb-2"></div>
            <div class="h-4 bg-gray-300 rounded w-3/4"></div>
          </div>
        </div>

        <!-- 友链网格 -->
        <div v-else-if="friendLinks.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          <a
            v-for="link in friendLinks"
            :key="link.id"
            :href="link.url"
            target="_blank"
            rel="noopener noreferrer"
            class="glass-effect rounded-2xl p-6 card-hover group block"
          >
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 flex-shrink-0 mr-4">
                <img
                  v-if="link.avatar"
                  :src="link.avatar"
                  :alt="link.name"
                  class="w-full h-full object-cover rounded-full"
                  @error="handleImageError"
                >
                <div
                  v-else
                  class="w-full h-full bg-blue-600 rounded-full flex items-center justify-center"
                >
                  <span class="text-white font-bold text-lg">
                    {{ link.name.charAt(0).toUpperCase() }}
                  </span>
                </div>
              </div>
              <div class="flex-1 min-w-0">
                <h3 class="text-lg font-bold text-gray-900 group-hover:text-blue-600 transition-colors truncate">
                  {{ link.name }}
                </h3>
                <p class="text-sm text-gray-500 truncate">
                  {{ getDomain(link.url) }}
                </p>
              </div>
            </div>
            
            <p class="text-gray-600 text-sm line-clamp-3 mb-4">
              {{ link.description || '暂无描述' }}
            </p>
            
            <div class="flex items-center justify-between text-xs text-gray-500">
              <span>{{ formatDate(link.createdAt) }}</span>
              <div class="flex items-center">
                <ExternalLinkIcon class="w-3 h-3 mr-1" />
                <span>访问</span>
              </div>
            </div>
          </a>
        </div>

        <!-- 空状态 -->
        <div v-else class="glass-effect rounded-2xl p-12 text-center">
          <div class="text-gray-400 mb-4">
            <LinkIcon class="w-16 h-16 mx-auto" />
          </div>
          <h3 class="text-lg font-semibold text-gray-900 mb-2">暂无友链</h3>
          <p class="text-gray-600">还没有添加任何友情链接，欢迎申请交换友链。</p>
        </div>
      </div>

      <!-- 友链统计 -->
      <div v-if="friendLinks.length > 0" class="mt-8 text-center">
        <p class="text-gray-600 dark:text-white/80">
          共有 <span class="font-bold text-gray-900 dark:text-white">{{ friendLinks.length }}</span> 个友站
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { friendLinkApi } from '../api'

// 图标组件
const ExternalLinkIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M19 19H5V5h7V3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2v-7h-2v7zM14 3v2h3.59l-9.83 9.83 1.41 1.41L19 6.41V10h2V3h-7z"/></svg>' }
const LinkIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M3.9 12c0-1.71 1.39-3.1 3.1-3.1h4V7H7c-2.76 0-5 2.24-5 5s2.24 5 5 5h4v-1.9H7c-1.71 0-3.1-1.39-3.1-3.1zM8 13h8v-2H8v2zm9-6h-4v1.9h4c1.71 0 3.1 1.39 3.1 3.1s-1.39 3.1-3.1 3.1h-4V17h4c2.76 0 5-2.24 5-5s-2.24-5-5-5z"/></svg>' }

// 响应式数据
const friendLinks = ref([])
const loading = ref(false)

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

// 获取域名
const getDomain = (url) => {
  try {
    const domain = new URL(url).hostname
    return domain.replace('www.', '')
  } catch {
    return url
  }
}

// 处理图片加载错误
const handleImageError = (event) => {
  event.target.style.display = 'none'
  const parent = event.target.parentElement
  if (parent) {
    const fallback = document.createElement('div')
    fallback.className = 'w-full h-full bg-blue-600 rounded-full flex items-center justify-center'
    fallback.innerHTML = `<span class="text-white font-bold text-lg">${event.target.alt.charAt(0).toUpperCase()}</span>`
    parent.appendChild(fallback)
  }
}

// 加载友链列表
const loadFriendLinks = async () => {
  try {
    loading.value = true
    const data = await friendLinkApi.getList()
    // 只显示激活的友链
    friendLinks.value = (data || []).filter(link => link.isActive)
  } catch (error) {
    console.error('加载友链失败:', error)
    friendLinks.value = []
  } finally {
    loading.value = false
  }
}

// 组件挂载时加载数据
onMounted(loadFriendLinks)
</script>

<style scoped>
.line-clamp-3 {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
