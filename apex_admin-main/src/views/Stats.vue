<template>
  <div class="stats">
    <div class="page-header">
      <h1>访问统计</h1>
      <el-button @click="loadStats" :loading="loading">
        <el-icon><Refresh /></el-icon>
        刷新数据
      </el-button>
    </div>
    
    <!-- 统计概览 -->
    <div class="stats-overview">
      <div class="stat-card">
        <div class="stat-icon total">
          <el-icon><View /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.totalVisits }}</div>
          <div class="stat-label">总访问量</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon today">
          <el-icon><Calendar /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.todayVisits }}</div>
          <div class="stat-label">今日访问</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon unique">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.uniqueVisitors }}</div>
          <div class="stat-label">独立访客</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon articles">
          <el-icon><Document /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ stats.totalArticles }}</div>
          <div class="stat-label">文章总数</div>
        </div>
      </div>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-container">
      <div class="chart-card">
        <div class="chart-header">
          <h3>最近访问趋势</h3>
        </div>
        <div class="chart-content">
          <div v-if="stats.recentVisits && stats.recentVisits.length > 0" class="visits-chart">
            <div
              v-for="visit in stats.recentVisits"
              :key="visit.date"
              class="visit-bar"
            >
              <div
                class="bar"
                :style="{ height: getBarHeight(visit.visits) + '%' }"
              ></div>
              <div class="bar-label">{{ formatShortDate(visit.date) }}</div>
              <div class="bar-value">{{ visit.visits }}</div>
            </div>
          </div>
          <div v-else class="no-data">
            暂无访问数据
          </div>
        </div>
      </div>
      
      <div class="chart-card">
        <div class="chart-header">
          <h3>热门页面</h3>
        </div>
        <div class="chart-content">
          <div v-if="stats.popularPages && stats.popularPages.length > 0" class="popular-pages">
            <div
              v-for="(page, index) in stats.popularPages"
              :key="page.uri"
              class="page-item"
            >
              <div class="page-rank">{{ index + 1 }}</div>
              <div class="page-info">
                <div class="page-uri">{{ page.uri }}</div>
                <div class="page-visits">{{ page.visits }} 次访问</div>
              </div>
            </div>
          </div>
          <div v-else class="no-data">
            暂无页面数据
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { statsApi } from '@/api/stats'

const loading = ref(false)

const stats = ref({
  totalVisits: 0,
  todayVisits: 0,
  uniqueVisitors: 0,
  totalArticles: 0,
  recentVisits: [],
  popularPages: []
})

// 计算柱状图高度
const getBarHeight = (visits) => {
  if (!stats.value.recentVisits || stats.value.recentVisits.length === 0) return 0
  const maxVisits = Math.max(...stats.value.recentVisits.map(v => v.visits))
  return maxVisits > 0 ? (visits / maxVisits) * 100 : 0
}

// 格式化短日期
const formatShortDate = (dateString) => {
  const date = new Date(dateString)
  return `${date.getMonth() + 1}/${date.getDate()}`
}

// 加载统计数据
const loadStats = async () => {
  loading.value = true
  try {
    const response = await statsApi.getVisitStats()
    stats.value = response.data
  } catch (error) {
    console.error('加载统计数据失败:', error)
    ElMessage.error('加载统计数据失败')
    
    // 使用模拟数据
    stats.value = {
      totalVisits: 10000,
      todayVisits: 100,
      uniqueVisitors: 5000,
      totalArticles: 25,
      recentVisits: [
        { date: '2024-01-01', visits: 120 },
        { date: '2024-01-02', visits: 150 },
        { date: '2024-01-03', visits: 100 },
        { date: '2024-01-04', visits: 180 },
        { date: '2024-01-05', visits: 200 },
        { date: '2024-01-06', visits: 160 },
        { date: '2024-01-07', visits: 140 }
      ],
      popularPages: [
        { uri: '/articles/1', visits: 500 },
        { uri: '/articles/2', visits: 350 },
        { uri: '/about', visits: 200 },
        { uri: '/articles/3', visits: 180 },
        { uri: '/portfolio', visits: 150 }
      ]
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.stats {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  color: #303133;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
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
  border-radius: 50%;
  margin-right: 16px;
}

.stat-icon.total {
  background: #e3f2fd;
  color: #1976d2;
}

.stat-icon.today {
  background: #f3e5f5;
  color: #7b1fa2;
}

.stat-icon.unique {
  background: #e8f5e8;
  color: #388e3c;
}

.stat-icon.articles {
  background: #fff3e0;
  color: #f57c00;
}

.stat-icon .el-icon {
  font-size: 24px;
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

.charts-container {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
}

.chart-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.chart-header {
  padding: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.chart-header h3 {
  color: #303133;
  margin: 0;
}

.chart-content {
  padding: 20px;
}

.visits-chart {
  display: flex;
  align-items: end;
  gap: 12px;
  height: 200px;
}

.visit-bar {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
}

.bar {
  width: 100%;
  background: linear-gradient(to top, #409eff, #79bbff);
  border-radius: 4px 4px 0 0;
  min-height: 4px;
  margin-bottom: 8px;
}

.bar-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.bar-value {
  font-size: 12px;
  font-weight: 600;
  color: #303133;
}

.popular-pages {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.page-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
}

.page-rank {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #409eff;
  color: white;
  border-radius: 50%;
  font-size: 12px;
  font-weight: 600;
}

.page-info {
  flex: 1;
}

.page-uri {
  font-weight: 500;
  color: #303133;
  margin-bottom: 2px;
}

.page-visits {
  font-size: 12px;
  color: #909399;
}

.no-data {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}

@media (max-width: 768px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
}
</style>
