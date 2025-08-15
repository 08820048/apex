<template>
  <div class="friend-link-list">
    <div class="page-header">
      <h1>友链管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新建友链
      </el-button>
    </div>
    
    <!-- 友链表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="friendLinks"
        style="width: 100%"
      >
        <el-table-column prop="name" label="友站名称" min-width="150" />
        <el-table-column prop="url" label="友站链接" min-width="200">
          <template #default="{ row }">
            <el-link :href="row.url" target="_blank" type="primary">
              {{ row.url }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="avatar" label="头像" width="80">
          <template #default="{ row }">
            <el-avatar
              v-if="row.avatar"
              :src="row.avatar"
              :size="40"
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200">
          <template #default="{ row }">
            <div class="description">
              {{ row.description || '-' }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isActive ? 'success' : 'danger'">
              {{ row.isActive ? '激活' : '禁用' }}
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
      :title="isEdit ? '编辑友链' : '新建友链'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="友站名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入友站名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="友站链接" prop="url">
          <el-input
            v-model="form.url"
            placeholder="请输入友站链接"
          />
        </el-form-item>
        
        <el-form-item label="友站头像">
          <el-input
            v-model="form.avatar"
            placeholder="请输入友站头像URL"
          />
          <div v-if="form.avatar" class="avatar-preview">
            <el-avatar :src="form.avatar" :size="60" />
          </div>
        </el-form-item>
        
        <el-form-item label="友站描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入友站描述"
            maxlength="200"
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
        
        <el-form-item label="是否激活">
          <el-switch v-model="form.isActive" />
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
import { friendLinkApi } from '@/api/friendLink'

const loading = ref(false)
const friendLinks = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref()

const form = reactive({
  id: null,
  name: '',
  url: '',
  avatar: '',
  description: '',
  sortOrder: 0,
  isActive: true
})

const rules = {
  name: [
    { required: true, message: '请输入友站名称', trigger: 'blur' },
    { max: 50, message: '友站名称不能超过50个字符', trigger: 'blur' }
  ],
  url: [
    { required: true, message: '请输入友站链接', trigger: 'blur' },
    { type: 'url', message: '请输入有效的URL', trigger: 'blur' }
  ]
}

// 加载友链列表
const loadFriendLinks = async () => {
  loading.value = true
  try {
    const response = await friendLinkApi.getList()
    friendLinks.value = response.data
  } catch (error) {
    console.error('加载友链列表失败:', error)
    ElMessage.error('加载友链列表失败')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.name = ''
  form.url = ''
  form.avatar = ''
  form.description = ''
  form.sortOrder = 0
  form.isActive = true
}

// 新建友链
const handleAdd = () => {
  resetForm()
  isEdit.value = false
  dialogVisible.value = true
}

// 编辑友链
const handleEdit = (friendLink) => {
  Object.assign(form, {
    id: friendLink.id,
    name: friendLink.name,
    url: friendLink.url,
    avatar: friendLink.avatar || '',
    description: friendLink.description || '',
    sortOrder: friendLink.sortOrder || 0,
    isActive: friendLink.isActive !== false
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
      await friendLinkApi.update(form.id, form)
      ElMessage.success('友链更新成功')
    } else {
      await friendLinkApi.create(form)
      ElMessage.success('友链创建成功')
    }
    
    dialogVisible.value = false
    loadFriendLinks()
  } catch (error) {
    console.error('保存友链失败:', error)
  } finally {
    submitting.value = false
  }
}

// 删除友链
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个友链吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await friendLinkApi.delete(id)
    ElMessage.success('删除成功')
    loadFriendLinks()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除友链失败:', error)
    }
  }
}

onMounted(() => {
  loadFriendLinks()
})
</script>

<style scoped>
.friend-link-list {
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

.avatar-preview {
  margin-top: 12px;
}
</style>
