<template>
  <div class="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-4xl mx-auto">
      <!-- 页面标题 -->
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold text-gray-900 dark:text-white mb-4">邮箱订阅</h1>
        <p class="text-xl text-gray-600 dark:text-white/80">订阅我的博客，第一时间获取最新文章</p>
      </div>

      <!-- 订阅表单 -->
      <div class="glass-effect rounded-2xl p-8 mb-8">
        <div class="max-w-md mx-auto">
          <div class="text-center mb-6">
            <div class="w-16 h-16 bg-blue-600 rounded-full flex items-center justify-center mx-auto mb-4">
              <EmailIcon class="w-8 h-8 text-white" />
            </div>
            <h2 class="text-2xl font-bold text-gray-900 mb-2">订阅更新</h2>
            <p class="text-gray-600">输入您的邮箱地址，我们会在发布新文章时通知您</p>
          </div>

          <form @submit.prevent="handleSubscribe" class="space-y-4">
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                邮箱地址
              </label>
              <input
                id="email"
                v-model="email"
                type="email"
                required
                placeholder="请输入您的邮箱地址"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                :disabled="subscribing"
              >
            </div>

            <button
              type="submit"
              :disabled="subscribing || !email"
              class="w-full bg-blue-600 text-white py-3 px-6 rounded-lg font-medium hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
            >
              <span v-if="subscribing" class="flex items-center justify-center">
                <LoadingIcon class="w-5 h-5 mr-2 animate-spin" />
                订阅中...
              </span>
              <span v-else>立即订阅</span>
            </button>
          </form>

          <!-- 订阅状态提示 -->
          <div v-if="message" class="mt-4 p-4 rounded-lg" :class="messageClass">
            <div class="flex items-center">
              <CheckIcon v-if="messageType === 'success'" class="w-5 h-5 mr-2" />
              <ExclamationIcon v-else class="w-5 h-5 mr-2" />
              <span>{{ message }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 订阅说明 -->
      <div class="glass-effect rounded-2xl p-8 mb-8">
        <h3 class="text-xl font-bold text-gray-900 mb-4">订阅说明</h3>
        <div class="space-y-3 text-gray-600">
          <div class="flex items-start">
            <CheckIcon class="w-5 h-5 text-green-500 mr-3 mt-0.5 flex-shrink-0" />
            <span>我们只会在发布新文章时发送邮件通知</span>
          </div>
          <div class="flex items-start">
            <CheckIcon class="w-5 h-5 text-green-500 mr-3 mt-0.5 flex-shrink-0" />
            <span>不会发送任何垃圾邮件或广告内容</span>
          </div>
          <div class="flex items-start">
            <CheckIcon class="w-5 h-5 text-green-500 mr-3 mt-0.5 flex-shrink-0" />
            <span>您可以随时取消订阅</span>
          </div>
          <div class="flex items-start">
            <CheckIcon class="w-5 h-5 text-green-500 mr-3 mt-0.5 flex-shrink-0" />
            <span>我们严格保护您的隐私信息</span>
          </div>
        </div>
      </div>

      <!-- RSS 订阅 -->
      <div class="glass-effect rounded-2xl p-8">
        <h3 class="text-xl font-bold text-gray-900 mb-4">RSS 订阅</h3>
        <p class="text-gray-600 mb-4">
          如果您更喜欢使用 RSS 阅读器，可以订阅我们的 RSS 源：
        </p>
        <div class="space-y-3">
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <div class="flex items-center">
              <RssIcon class="w-5 h-5 text-orange-500 mr-3" />
              <span class="font-medium">RSS 2.0</span>
            </div>
            <a
              href="/api/rss/feed.xml"
              target="_blank"
              class="text-blue-600 hover:text-blue-800 transition-colors"
            >
              订阅
            </a>
          </div>
          <div class="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
            <div class="flex items-center">
              <RssIcon class="w-5 h-5 text-orange-500 mr-3" />
              <span class="font-medium">Atom</span>
            </div>
            <a
              href="/api/rss/atom.xml"
              target="_blank"
              class="text-blue-600 hover:text-blue-800 transition-colors"
            >
              订阅
            </a>
          </div>
        </div>
      </div>

      <!-- 订阅统计 -->
      <div v-if="subscriberCount > 0" class="mt-8 text-center">
        <p class="text-gray-600 dark:text-white/80">
          已有 <span class="font-bold text-gray-900 dark:text-white">{{ subscriberCount }}</span> 位朋友订阅了我们的更新
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { emailSubscriberApi } from '../api'

// 图标组件
const EmailIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/></svg>' }
const CheckIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>' }
const ExclamationIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/></svg>' }
const LoadingIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 6v3l4-4-4-4v3c-4.42 0-8 3.58-8 8 0 1.57.46 3.03 1.24 4.26L6.7 14.8c-.45-.83-.7-1.79-.7-2.8 0-3.31 2.69-6 6-6zm6.76 1.74L17.3 9.2c.44.84.7 1.79.7 2.8 0 3.31-2.69 6-6 6v-3l-4 4 4 4v-3c4.42 0 8-3.58 8-8 0-1.57-.46-3.03-1.24-4.26z"/></svg>' }
const RssIcon = { template: '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M6.503 20.752c0 1.794-1.456 3.248-3.251 3.248S0 22.546 0 20.752s1.456-3.248 3.252-3.248 3.251 1.454 3.251 3.248zm-6.503-12.572v4.811c6.05.062 10.96 4.966 11.022 11.009h4.817c-.062-8.71-7.118-15.758-15.839-15.82zm0-3.368c10.58.046 19.152 8.594 19.183 19.188h4.817c-.03-13.231-10.755-23.954-24-24v4.812z"/></svg>' }

// 响应式数据
const email = ref('')
const subscribing = ref(false)
const message = ref('')
const messageType = ref('')
const subscriberCount = ref(0)

// 计算属性
const messageClass = computed(() => {
  return messageType.value === 'success' 
    ? 'bg-green-50 text-green-800 border border-green-200'
    : 'bg-red-50 text-red-800 border border-red-200'
})

// 处理订阅
const handleSubscribe = async () => {
  if (!email.value) return

  try {
    subscribing.value = true
    message.value = ''

    await emailSubscriberApi.subscribe(email.value)

    message.value = '订阅成功！我们会在发布新文章时通知您。'
    messageType.value = 'success'
    email.value = ''

    // 重新加载订阅者数量
    loadSubscriberCount()
  } catch (error) {
    console.error('订阅失败:', error)
    // 根据错误类型显示不同的消息
    if (error.response?.status === 500) {
      message.value = '服务暂时不可用，请稍后重试。'
    } else if (error.response?.status === 400) {
      message.value = '邮箱格式不正确，请检查后重试。'
    } else {
      message.value = error.message || '订阅失败，请稍后重试。'
    }
    messageType.value = 'error'
  } finally {
    subscribing.value = false
  }
}

// 加载订阅者数量
const loadSubscriberCount = async () => {
  try {
    const count = await emailSubscriberApi.getCount()
    subscriberCount.value = count || 0
  } catch (error) {
    console.error('加载订阅者数量失败:', error)
    // 如果API失败，设置一个默认值，避免显示错误
    subscriberCount.value = 0
  }
}

// 组件挂载时加载数据
onMounted(loadSubscriberCount)
</script>
