<template>
  <div class="subscriber-list">
    <div class="page-header">
      <h1>订阅管理</h1>
      <div class="header-info">
        <el-statistic title="总订阅数" :value="pagination.total" />
      </div>
    </div>
    
    <!-- 搜索和筛选 -->
    <div class="filters">
      <el-form :model="searchForm" inline>
        <el-form-item label="邮箱">
          <el-input
            v-model="searchForm.email"
            placeholder="请输入邮箱地址"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 订阅者表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="subscribers"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="email" label="邮箱地址" min-width="200" />
        <el-table-column prop="subscribedAt" label="订阅时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.subscribedAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isActive ? 'success' : 'danger'">
              {{ row.isActive ? '激活' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastEmailSent" label="最后发送" width="180">
          <template #default="{ row }">
            {{ formatDate(row.lastEmailSent) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 批量操作 -->
    <div v-if="selectedSubscribers.length > 0" class="batch-actions">
      <el-alert
        :title="`已选择 ${selectedSubscribers.length} 个订阅者`"
        type="info"
        show-icon
        :closable="false"
      >
        <template #default>
          <div class="batch-buttons">
            <el-button
              size="small"
              type="danger"
              @click="handleBatchDelete"
            >
              批量删除
            </el-button>
          </div>
        </template>
      </el-alert>
    </div>
    
    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { subscriberApi } from '@/api/subscriber'

const loading = ref(false)
const subscribers = ref([])
const selectedSubscribers = ref([])

const searchForm = reactive({
  email: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 加载订阅者列表
const loadSubscribers = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page - 1, // 后端从0开始
      size: pagination.size,
      ...searchForm
    }
    
    const response = await subscriberApi.getList(params)
    subscribers.value = response.data.content
    pagination.total = response.data.totalElements
  } catch (error) {
    console.error('加载订阅者列表失败:', error)
    ElMessage.error('加载订阅者列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadSubscribers()
}

// 重置
const handleReset = () => {
  searchForm.email = ''
  pagination.page = 1
  loadSubscribers()
}

// 删除订阅者
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个订阅者吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await subscriberApi.delete(id)
    ElMessage.success('删除成功')
    loadSubscribers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除订阅者失败:', error)
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedSubscribers.value.length} 个订阅者吗？`,
      '批量删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 批量删除
    const deletePromises = selectedSubscribers.value.map(subscriber =>
      subscriberApi.delete(subscriber.id)
    )
    
    await Promise.all(deletePromises)
    ElMessage.success('批量删除成功')
    loadSubscribers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
    }
  }
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedSubscribers.value = selection
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadSubscribers()
}

// 页码变化
const handleCurrentChange = (page) => {
  pagination.page = page
  loadSubscribers()
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

onMounted(() => {
  loadSubscribers()
})
</script>

<style scoped>
.subscriber-list {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  color: #303133;
}

.header-info {
  display: flex;
  gap: 20px;
}

.filters {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.batch-actions {
  margin: 20px 0;
}

.batch-buttons {
  margin-top: 12px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
