<template>
  <footer class="border-t border-gray-200 bg-gray-50 mt-auto">
    <div class="max-w-7xl mx-auto px-4 py-4">
      <div class="flex flex-col items-center text-sm text-gray-600 space-y-2">
        <!-- 版权信息 -->
        <div class="flex items-center space-x-2">
          <img
            src="/apex.jpg"
            alt="ApexBlog Logo"
            class="w-6 h-6 rounded-full object-cover"
          />
          <p>© {{ currentYear }} ApexBlog. All rights reserved.</p>
        </div>

        <!-- 备案信息 -->
        <div
          class="flex flex-col md:flex-row items-center space-y-1 md:space-y-0 md:space-x-6 text-xs text-gray-500"
        >
          <!-- ICP备案 -->
          <a
            href="https://beian.miit.gov.cn/"
            target="_blank"
            rel="noopener noreferrer"
            class="hover:text-gray-700 transition-colors"
          >
            黔ICP备2021010295号
          </a>

          <!-- 公安备案 -->
          <a
            href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=52052402000190"
            target="_blank"
            class="flex items-center space-x-1"
          >
            <img src="/an.png" alt="公安备案图标" class="w-3 h-3" />
            <b
              class="font-extrabold border-b-2 border-gray-500 hover:text-gray-700 text-gray-500"
            >
              贵公安备52052402000190号
            </b>
          </a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { computed } from "vue";

const currentYear = computed(() => new Date().getFullYear());
</script>
