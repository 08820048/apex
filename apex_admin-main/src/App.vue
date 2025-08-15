<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
// 主应用组件，简洁的路由视图容器
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #f5f5f5;
}

#app {
  height: 100vh;
  overflow: hidden;
}
</style>
