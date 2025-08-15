<template>
  <div class="sidebar">
    <div class="logo" :class="{ collapsed: props.collapsed }">
      <el-icon><Document /></el-icon>
      <span v-show="!props.collapsed" class="logo-text">ApexBlog</span>
    </div>
    
    <el-menu
      :default-active="$route.path"
      class="sidebar-menu"
      router
      :collapse="props.collapsed"
    >
      <el-menu-item index="/dashboard">
        <el-icon><Odometer /></el-icon>
        <span>仪表盘</span>
      </el-menu-item>
      
      <el-sub-menu index="content">
        <template #title>
          <el-icon><Document /></el-icon>
          <span>内容管理</span>
        </template>
        <el-menu-item index="/articles">
          <el-icon><EditPen /></el-icon>
          <span>文章管理</span>
        </el-menu-item>
        <el-menu-item index="/categories">
          <el-icon><Folder /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/tags">
          <el-icon><PriceTag /></el-icon>
          <span>标签管理</span>
        </el-menu-item>
      </el-sub-menu>
      
      <el-sub-menu index="other">
        <template #title>
          <el-icon><Grid /></el-icon>
          <span>其他管理</span>
        </template>
        <el-menu-item index="/portfolios">
          <el-icon><Briefcase /></el-icon>
          <span>作品集管理</span>
        </el-menu-item>
        <el-menu-item index="/friend-links">
          <el-icon><Link /></el-icon>
          <span>友链管理</span>
        </el-menu-item>
        <el-menu-item index="/subscribers">
          <el-icon><Message /></el-icon>
          <span>订阅管理</span>
        </el-menu-item>
      </el-sub-menu>
      
      <el-menu-item index="/stats">
        <el-icon><TrendCharts /></el-icon>
        <span>访问统计</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()

// 接收props
const props = defineProps({
  collapsed: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['toggle'])
</script>

<style scoped>
.sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
  background-color: #fff;
  transition: all 0.3s ease;
}

.logo.collapsed {
  justify-content: center;
  padding: 20px 10px;
}

.logo .el-icon {
  font-size: 24px;
  color: #409eff;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.sidebar-menu {
  flex: 1;
  border-right: none;
}

.sidebar-menu .el-menu-item,
.sidebar-menu .el-sub-menu__title {
  height: 48px;
  line-height: 48px;
  padding-left: 20px !important;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: #ecf5ff;
  color: #409eff;
  border-right: 3px solid #409eff;
}

.sidebar-menu .el-sub-menu .el-menu-item {
  padding-left: 40px !important;
  background-color: #fafafa;
}

.sidebar-menu .el-sub-menu .el-menu-item.is-active {
  background-color: #ecf5ff;
}

/* 收起状态下的样式优化 */
.sidebar-menu.el-menu--collapse {
  width: 64px;
}

.sidebar-menu.el-menu--collapse .el-menu-item,
.sidebar-menu.el-menu--collapse .el-sub-menu__title {
  padding: 0 !important;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sidebar-menu.el-menu--collapse .el-menu-item .el-icon,
.sidebar-menu.el-menu--collapse .el-sub-menu__title .el-icon {
  margin-right: 0 !important;
  font-size: 18px;
}

.sidebar-menu.el-menu--collapse .el-menu-item.is-active {
  border-right: none;
  background-color: #ecf5ff;
}

.sidebar-menu.el-menu--collapse .el-sub-menu__title:hover,
.sidebar-menu.el-menu--collapse .el-menu-item:hover {
  background-color: #f5f7fa;
}

/* 收起状态下的样式优化 */
.sidebar-menu.el-menu--collapse {
  width: 64px;
}

.sidebar-menu.el-menu--collapse .el-menu-item,
.sidebar-menu.el-menu--collapse .el-sub-menu__title {
  padding: 0 !important;
  text-align: center;
}

.sidebar-menu.el-menu--collapse .el-menu-item.is-active {
  border-right: none;
  background-color: #ecf5ff;
}

.sidebar-menu.el-menu--collapse .el-menu-item .el-icon,
.sidebar-menu.el-menu--collapse .el-sub-menu__title .el-icon {
  margin-right: 0;
}
</style>
