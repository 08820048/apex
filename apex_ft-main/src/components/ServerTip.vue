<template>
  <div
    v-if="showTip"
    class="bg-gradient-to-r from-blue-50 to-indigo-50 border-b border-blue-200 relative"
  >
    <div class="max-w-7xl mx-auto px-4 py-2.5">
      <div class="flex items-center justify-between">
        <div class="flex items-center">
          <div class="flex-shrink-0">
            <svg
              class="h-5 w-5 text-blue-400"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fill-rule="evenodd"
                d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z"
                clip-rule="evenodd"
              />
            </svg>
          </div>
          <div class="ml-3">
            <p class="text-sm text-blue-700">
              <span class="font-medium">💡 提示：</span>
              本博客部署在海外服务器，开启科学上网可提高访问速度
            </p>
          </div>
        </div>
        <div class="flex-shrink-0">
          <button
            @click="closeTip"
            class="inline-flex text-blue-400 hover:text-blue-600 focus:outline-none focus:text-blue-600 transition-colors"
          >
            <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
              <path
                fill-rule="evenodd"
                d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                clip-rule="evenodd"
              />
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const showTip = ref(false);

onMounted(() => {
  // 检查是否已经关闭过提示
  const tipClosed = localStorage.getItem("serverTipClosed");
  if (!tipClosed) {
    showTip.value = true;
  }
});

const closeTip = () => {
  showTip.value = false;
  // 记住用户已关闭提示
  localStorage.setItem("serverTipClosed", "true");
};
</script>
