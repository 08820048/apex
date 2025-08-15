import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import { resolve } from "path";

// https://vite.dev/config/
export default defineConfig({
  // 设置基础路径为相对路径，解决部署后资源加载问题
  base: "./",
  plugins: [
    vue({
      template: {
        compilerOptions: {
          isCustomElement: (tag) => tag.includes("-"),
        },
      },
    }),
    AutoImport({ resolvers: [ElementPlusResolver()] }),
    Components({ resolvers: [ElementPlusResolver()] }),
  ],
  define: {
    __VUE_OPTIONS_API__: true,
    __VUE_PROD_DEVTOOLS__: false,
    __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: false,
  },
  resolve: {
    alias: {
      "@": resolve(__dirname, "src"),
      vue: "vue/dist/vue.esm-bundler.js",
    },
  },
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8888",
        changeOrigin: true,
      },
    },
  },
  build: {
    // 构建输出目录
    outDir: "dist",
    // 静态资源目录
    assetsDir: "assets",
    // 生成源码映射
    sourcemap: false,
    // 构建后清理输出目录
    emptyOutDir: true,
    // 资源内联阈值
    assetsInlineLimit: 4096,
    // Rollup配置
    rollupOptions: {
      output: {
        // 分包策略
        manualChunks: {
          // Vue相关
          vue: ["vue", "vue-router"],
          // Element Plus相关
          "element-plus": ["element-plus", "@element-plus/icons-vue"],
          // 工具库
          utils: ["marked", "highlight.js"],
        },
      },
    },
  },
});
