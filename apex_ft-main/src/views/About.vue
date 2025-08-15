<template>
  <div class="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-4xl mx-auto">
      <!-- 个人介绍 -->
      <div class="glass-effect rounded-2xl p-8 mb-8">
        <div class="text-center mb-8">
          <div
            class="w-32 h-32 mx-auto mb-6 rounded-full overflow-hidden shadow-lg"
          >
            <img
              src="/apex.jpg"
              alt="ApexBlog Logo"
              class="w-full h-full object-cover"
            />
          </div>
          <h1 class="text-3xl font-bold text-gray-900 mb-4">关于 Apex博客</h1>
          <p class="text-xl text-gray-600">一个专注于技术分享的个人博客</p>
        </div>

        <div class="prose prose-lg max-w-none text-gray-700">
          <p class="text-center mb-8">
            欢迎来到 ApexBlog！这里是我分享技术心得、记录学习历程的地方。
          </p>

          <h2 class="text-2xl font-bold text-gray-900 mb-4">博客简介</h2>
          <p class="mb-6">
            ApexBlog
            是一个现代化的技术博客平台，专注于全栈开发、语言教程、技术分享、业内资讯、游戏开发等领域的知识分享。
            我希望通过这个平台，能够与更多的开发者交流学习，共同进步。
          </p>

          <h2 class="text-2xl font-bold text-gray-900 mb-4">技术栈</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
            <div>
              <h3 class="text-lg font-semibold text-gray-900 mb-3">前端技术</h3>
              <ul class="space-y-2">
                <li class="flex items-center">
                  <span class="w-2 h-2 bg-blue-500 rounded-full mr-3"></span>
                  Vue 3 + Composition API
                </li>
                <li class="flex items-center">
                  <span class="w-2 h-2 bg-blue-500 rounded-full mr-3"></span>
                  Vite 构建工具
                </li>
                <li class="flex items-center">
                  <span class="w-2 h-2 bg-blue-500 rounded-full mr-3"></span>
                  Tailwind CSS
                </li>
                <li class="flex items-center">
                  <span class="w-2 h-2 bg-blue-500 rounded-full mr-3"></span>
                  Vue Router
                </li>
              </ul>
            </div>
            <div>
              <h3 class="text-lg font-semibold text-gray-900 mb-3">后端技术</h3>
              <ul class="space-y-2">
                <li class="flex items-center">
                  <span class="w-2 h-2 bg-green-500 rounded-full mr-3"></span>
                  Spring Boot
                </li>
                <li class="flex items-center">
                  <span class="w-2 h-2 bg-green-500 rounded-full mr-3"></span>
                  Spring Data JPA
                </li>
                <li class="flex items-center">
                  <span class="w-2 h-2 bg-green-500 rounded-full mr-3"></span>
                  MySQL 数据库
                </li>
                <li class="flex items-center">
                  <span class="w-2 h-2 bg-green-500 rounded-full mr-3"></span>
                  RESTful API
                </li>
              </ul>
            </div>
          </div>

          <h2 class="text-2xl font-bold text-gray-900 mb-4">联系方式</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
            <a href="mailto:contact@apexblog.com" class="contact-card">
              <EmailIcon class="w-8 h-8 text-blue-500 mb-3" />
              <h3 class="font-semibold text-gray-900 mb-1">邮箱</h3>
              <p class="text-gray-600 text-sm">ilikexff@gmail.com</p>
            </a>
            <a
              href="https://github.com/08820048"
              target="_blank"
              rel="noopener noreferrer"
              class="contact-card"
            >
              <GithubIcon class="w-8 h-8 text-gray-700 mb-3" />
              <h3 class="font-semibold text-gray-900 mb-1">GitHub</h3>
              <p class="text-gray-600 text-sm">@08820048</p>
            </a>
            <a
              href="https://x.com/_Peter_You"
              target="_blank"
              rel="noopener noreferrer"
              class="contact-card"
            >
              <TwitterIcon class="w-8 h-8 text-blue-400 mb-3" />
              <h3 class="font-semibold text-gray-900 mb-1">Twitter</h3>
              <p class="text-gray-600 text-sm">@_Peter_You</p>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 图标组件
const EmailIcon = {
  template:
    '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z"/></svg>',
};
const GithubIcon = {
  template:
    '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z"/></svg>',
};
const TwitterIcon = {
  template:
    '<svg viewBox="0 0 24 24" fill="currentColor"><path d="M23.953 4.57a10 10 0 01-2.825.775 4.958 4.958 0 002.163-2.723c-.951.555-2.005.959-3.127 1.184a4.92 4.92 0 00-8.384 4.482C7.69 8.095 4.067 6.13 1.64 3.162a4.822 4.822 0 00-.666 2.475c0 1.71.87 3.213 2.188 4.096a4.904 4.904 0 01-2.228-.616v.06a4.923 4.923 0 003.946 4.827 4.996 4.996 0 01-2.212.085 4.936 4.936 0 004.604 3.417 9.867 9.867 0 01-6.102 2.105c-.39 0-.779-.023-1.17-.067a13.995 13.995 0 007.557 2.209c9.053 0 13.998-7.496 13.998-13.985 0-.21 0-.42-.015-.63A9.935 9.935 0 0024 4.59z"/></svg>',
};
</script>

<style scoped>
.contact-card {
  @apply flex flex-col items-center p-6 bg-gray-50 rounded-xl hover:bg-gray-100 transition-colors text-center;
}

.prose h2 {
  @apply text-2xl font-bold text-gray-900 mb-4;
}

.prose h3 {
  @apply text-lg font-semibold text-gray-900 mb-2;
}

.prose p {
  @apply mb-4 leading-relaxed;
}

.prose ul {
  @apply space-y-2;
}

.prose li {
  @apply flex items-center;
}
</style>
