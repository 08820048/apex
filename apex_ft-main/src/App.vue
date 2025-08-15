<template>
  <div id="app">
    <AppHeader />
    <ServerTip />
    <main class="flex-1">
      <RouterView v-slot="{ Component }">
        <Transition name="page" mode="out-in">
          <component :is="Component" />
        </Transition>
      </RouterView>
    </main>
    <AppFooter />

    <!-- 查询统计文本条 -->
    <QueryStatsToast />
  </div>
</template>

<script setup>
import AppHeader from "./components/AppHeader.vue";
import AppFooter from "./components/AppFooter.vue";
import QueryStatsToast from "./components/QueryStatsToast.vue";
import ServerTip from "./components/ServerTip.vue";
</script>
