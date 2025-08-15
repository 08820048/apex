<template>
  <div class="tag-list">
    <div class="page-header">
      <h1>标签管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新建标签
      </el-button>
    </div>
    
    <!-- 标签表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tags"
        style="width: 100%"
      >
        <el-table-column prop="name" label="标签名称" min-width="150">
          <template #default="{ row }">
            <el-tag :color="row.color" effect="dark">
              {{ row.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="color" label="颜色" width="120">
          <template #default="{ row }">
            <div class="color-preview">
              <div 
                class="color-block" 
                :style="{ backgroundColor: row.color }"
              ></div>
              <span>{{ row.color }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="articleCount" label="文章数量" width="120">
          <template #default="{ row }">
            {{ row.articleCount || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
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
      :title="isEdit ? '编辑标签' : '新建标签'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="标签名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入标签名称"
            maxlength="30"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="标签颜色">
          <div class="color-picker-container">
            <el-color-picker v-model="form.color" />
            <el-input
              v-model="form.color"
              placeholder="请选择或输入颜色值"
              style="margin-left: 12px; flex: 1"
            />
          </div>
        </el-form-item>
        
        <el-form-item label="预览">
          <el-tag :color="form.color" effect="dark">
            {{ form.name || '标签预览' }}
          </el-tag>
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
import { tagApi } from '@/api/tag'

const loading = ref(false)
const tags = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref()

const form = reactive({
  id: null,
  name: '',
  color: '#409eff'
})

const rules = {
  name: [
    { required: true, message: '请输入标签名称', trigger: 'blur' },
    { max: 30, message: '标签名称不能超过30个字符', trigger: 'blur' }
  ]
}

// 加载标签列表
const loadTags = async () => {
  loading.value = true
  try {
    const response = await tagApi.getList()
    tags.value = response.data
  } catch (error) {
    console.error('加载标签列表失败:', error)
    ElMessage.error('加载标签列表失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.name = ''
  form.color = '#409eff'
}

// 新建标签
const handleAdd = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

// 编辑标签
const handleEdit = (tag) => {
  Object.assign(form, {
    id: tag.id,
    name: tag.name,
    color: tag.color || '#409eff'
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
      await tagApi.update(form.id, form)
      ElMessage.success('标签更新成功')
    } else {
      await tagApi.create(form)
      ElMessage.success('标签创建成功')
    }
    
    dialogVisible.value = false
    loadTags()
  } catch (error) {
    console.error('保存标签失败:', error)
  } finally {
    submitting.value = false
  }
}

// 删除标签
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个标签吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await tagApi.delete(id)
    ElMessage.success('删除成功')
    loadTags()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除标签失败:', error)
    }
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

onMounted(() => {
  loadTags()
})
</script>

<style scoped>
.tag-list {
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

.color-preview {
  display: flex;
  align-items: center;
  gap: 8px;
}

.color-block {
  width: 20px;
  height: 20px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.color-picker-container {
  display: flex;
  align-items: center;
  width: 100%;
}
</style>
