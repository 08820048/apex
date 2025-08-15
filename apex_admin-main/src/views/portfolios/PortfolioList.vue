<template>
  <div class="portfolio-list">
    <div class="page-header">
      <h1>作品集管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新建作品
      </el-button>
    </div>
    
    <!-- 作品集表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="portfolios"
        style="width: 100%"
      >
        <el-table-column prop="name" label="项目名称" min-width="150" />
        <el-table-column prop="description" label="项目描述" min-width="200">
          <template #default="{ row }">
            <div class="description">
              {{ row.description || '-' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="technologies" label="技术栈" min-width="150">
          <template #default="{ row }">
            {{ row.technologies || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="url" label="项目链接" width="120">
          <template #default="{ row }">
            <el-link 
              v-if="row.url" 
              :href="row.url" 
              target="_blank" 
              type="primary"
            >
              查看
            </el-link>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isFeatured" label="精选" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isFeatured ? 'success' : 'info'">
              {{ row.isFeatured ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
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
    
    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑作品' : '新建作品'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="项目名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入项目名称"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="项目描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入项目描述"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="项目链接">
          <el-input
            v-model="form.url"
            placeholder="请输入项目链接"
          />
        </el-form-item>
        
        <el-form-item label="封面图片">
          <el-input
            v-model="form.coverImage"
            placeholder="请输入封面图片URL"
          />
        </el-form-item>
        
        <el-form-item label="技术栈">
          <el-input
            v-model="form.technologies"
            placeholder="请输入技术栈，如：Java, Spring Boot, Vue.js"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="排序">
          <el-input-number
            v-model="form.sortOrder"
            :min="0"
            :max="999"
            placeholder="排序值"
          />
        </el-form-item>
        
        <el-form-item label="是否精选">
          <el-switch v-model="form.isFeatured" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { portfolioApi } from '@/api/portfolio'

const loading = ref(false)
const portfolios = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref()

const form = reactive({
  id: null,
  name: '',
  description: '',
  url: '',
  coverImage: '',
  technologies: '',
  sortOrder: 0,
  isFeatured: false
})

const rules = {
  name: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { max: 100, message: '项目名称不能超过100个字符', trigger: 'blur' }
  ]
}

// 加载作品集列表
const loadPortfolios = async () => {
  loading.value = true
  try {
    const response = await portfolioApi.getList()
    portfolios.value = response.data
  } catch (error) {
    console.error('加载作品集列表失败:', error)
    ElMessage.error('加载作品集列表失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.name = ''
  form.description = ''
  form.url = ''
  form.coverImage = ''
  form.technologies = ''
  form.sortOrder = 0
  form.isFeatured = false
}

// 新建作品
const handleAdd = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

// 编辑作品
const handleEdit = (portfolio) => {
  Object.assign(form, {
    id: portfolio.id,
    name: portfolio.name,
    description: portfolio.description || '',
    url: portfolio.url || '',
    coverImage: portfolio.coverImage || '',
    technologies: portfolio.technologies || '',
    sortOrder: portfolio.sortOrder || 0,
    isFeatured: portfolio.isFeatured || false
  })
  isEdit.value = true
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    
    if (isEdit.value) {
      await portfolioApi.update(form.id, form)
      ElMessage.success('作品更新成功')
    } else {
      await portfolioApi.create(form)
      ElMessage.success('作品创建成功')
    }
    
    dialogVisible.value = false
    loadPortfolios()
  } catch (error) {
    console.error('保存作品失败:', error)
  } finally {
    submitting.value = false
  }
}

// 删除作品
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个作品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await portfolioApi.delete(id)
    ElMessage.success('删除成功')
    loadPortfolios()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除作品失败:', error)
    }
  }
}

onMounted(() => {
  loadPortfolios()
})
</script>

<style scoped>
.portfolio-list {
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

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.description {
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
