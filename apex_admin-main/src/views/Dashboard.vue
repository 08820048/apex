<template>
  <div class="dashboard">
    <div class="page-header">
      <h1>仪表盘</h1>
      <p>欢迎回来，{{ userStore.userInfo?.username }}！</p>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.totalArticles }}</div>
          <div class="stat-label">总文章数</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><View /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.totalVisits }}</div>
          <div class="stat-label">总访问量</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.uniqueVisitors }}</div>
          <div class="stat-label">独立访客</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon">
          <el-icon><Calendar /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.todayVisits }}</div>
          <div class="stat-label">今日访问</div>
        </div>
      </div>
    </div>
    
    <!-- 快捷操作 -->
    <div class="quick-actions">
      <h2>快捷操作</h2>
      <div class="action-grid">
        <el-button type="primary" @click="$router.push('/articles/new')">
          <el-icon><EditPen /></el-icon>
          新建文章
        </el-button>
        <el-button @click="$router.push('/articles')">
          <el-icon><Document /></el-icon>
          管理文章
        </el-button>
        <el-button @click="$router.push('/categories')">
          <el-icon><Folder /></el-icon>
          管理分类
        </el-button>
        <el-button @click="$router.push('/tags')">
          <el-icon><PriceTag /></el-icon>
          管理标签
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { statsApi } from '@/api/stats'

const userStore = useUserStore()

const stats = ref({
  totalArticles: 0,
  totalVisits: 0,
  uniqueVisitors: 0,
  todayVisits: 0
})

const loadStats = async () => {
  try {
    const response = await statsApi.getVisitStats()
    stats.value = {
      totalArticles: response.data.totalArticles || 0,
      totalVisits: response.data.totalVisits || 0,
      uniqueVisitors: response.data.uniqueVisitors || 0,
      todayVisits: response.data.todayVisits || 0
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
    ElMessage.error('加载统计数据失败')
    // 保持默认值
    stats.value = {
      totalArticles: 0,
      totalVisits: 0,
      uniqueVisitors: 0,
      todayVisits: 0
    }
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  color: #303133;
  margin-bottom: 8px;
}

.page-header p {
  color: #909399;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ecf5ff;
  border-radius: 50%;
  margin-right: 16px;
}

.stat-icon .el-icon {
  font-size: 24px;
  color: #409eff;
}

.stat-content {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.quick-actions {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.quick-actions h2 {
  color: #303133;
  margin-bottom: 20px;
}

.action-grid {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.action-grid .el-button {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
