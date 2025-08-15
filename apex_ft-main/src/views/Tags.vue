<template>
  <div class="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold text-gray-900 dark:text-white mb-4">文章标签</h1>
        <p class="text-xl text-gray-600 dark:text-white/80">通过标签快速找到相关文章</p>
      </div>

      <!-- 标签云 -->
      <Transition name="loading">
        <div v-if="loading" class="glass-effect rounded-2xl p-8">
          <div class="flex flex-wrap gap-3">
            <div v-for="i in 20" :key="i" class="h-8 bg-gray-300 rounded-full animate-pulse" :style="{ width: Math.random() * 60 + 40 + 'px' }"></div>
          </div>
        </div>
      </Transition>

      <Transition name="fade">
        <div v-if="!loading && tags.length > 0" class="glass-effect rounded-2xl p-8">
        <div class="flex flex-wrap gap-3">
          <span 
            v-for="tag in tags" 
            :key="tag.id"
            @click="goToTag(tag.id)"
            class="tag-item cursor-pointer tag-hover"
            :style="{
              fontSize: '1rem',
              backgroundColor: tag.color + '20' || '#3b82f620',
              color: tag.color || '#3b82f6'
            }"
          >
            {{ tag.name }}
          </span>
        </div>
      </div>
      </Transition>

      <!-- 空状态 -->
      <Transition name="fade">
        <div v-if="!loading && tags.length === 0" class="glass-effect rounded-2xl p-12 text-center">
        <div class="text-gray-400 mb-4">
          <TagIcon class="w-16 h-16 mx-auto" />
        </div>
        <h3 class="text-lg font-semibold text-gray-900 mb-2">暂无标签</h3>
        <p class="text-gray-600">还没有创建任何标签，请稍后再来查看。</p>
      </div>
      </Transition>

      <!-- 标签列表 -->
      <Transition name="fade">
        <div v-if="!loading && tags.length > 0" class="mt-8">
        <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-6">所有标签</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div 
            v-for="tag in tags" 
            :key="tag.id"
            @click="goToTag(tag.id)"
            class="glass-effect rounded-xl p-4 card-hover cursor-pointer group"
          >
            <div class="flex items-center justify-between">
              <div class="flex items-center">
                <div 
                  class="w-4 h-4 rounded-full mr-3"
                  :style="{ backgroundColor: tag.color || '#3b82f6' }"
                ></div>
                <span class="font-medium text-gray-900 group-hover:text-blue-600 transition-colors">
                  {{ tag.name }}
                </span>
              </div>
              <span class="text-sm text-gray-500">
                {{ formatDate(tag.createdAt) }}
              </span>
            </div>
          </div>
        </div>
      </div>
      </Transition>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { tagApi } from '../api'

// 图标组件
const TagIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M17.63,5.84C17.27,5.33 16.67,5 16,5L5,5.01C3.9,5.01 3,5.9 3,7V17C3,18.1 3.9,18.99 5,18.99L16,19C16.67,19 17.27,18.67 17.63,18.16L22,12L17.63,5.84Z" /></svg>' }

const router = useRouter()

// 响应式数据
const tags = ref([])
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

// 跳转到标签页面
const goToTag = (tagId) => {
  router.push({ name: 'search', query: { tag: tagId } })
}

// 加载标签列表
const loadTags = async () => {
  try {
    loading.value = true
    const data = await tagApi.getList()
    tags.value = data || []
  } catch (error) {
    console.error('加载标签失败:', error)
    tags.value = []
  } finally {
    loading.value = false
  }
}

// 组件挂载时加载数据
onMounted(loadTags)
</script>

<style scoped>
.tag-item {
  @apply px-4 py-2 text-white rounded-full font-medium shadow-md;
}
</style>
