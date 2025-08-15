<template>
  <div class="markdown-editor">
    <!-- 普通编辑模式 -->
    <div v-if="!focusMode" class="normal-mode">
      <!-- 顶部控制栏 -->
      <div class="normal-header">
        <div class="normal-header-left">
          <span class="editor-title">Markdown编辑器</span>
        </div>
        <div class="normal-header-right">
          <el-button-group class="view-controls">
            <el-button
              size="small"
              @click="togglePreview"
              :type="showPreview ? 'primary' : ''"
              :icon="showPreview ? 'Hide' : 'View'"
            >
              {{ showPreview ? '隐藏预览' : '显示预览' }}
            </el-button>
            <el-button size="small" @click="openFullscreenPreview" :icon="FullScreen">
              全屏预览
            </el-button>
            <el-button size="small" @click="enterFocusMode" type="warning" :icon="Edit">
              专注编辑
            </el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 编辑工具栏 -->
      <div class="editor-toolbar">
        <div class="toolbar-content">
          <el-button-group>
            <el-button size="small" @click="insertText('**', '**')" title="粗体">
              <strong>B</strong>
            </el-button>
            <el-button size="small" @click="insertText('*', '*')" title="斜体">
              <em>I</em>
            </el-button>
            <el-button size="small" @click="insertText('# ', '')" title="标题1">H1</el-button>
            <el-button size="small" @click="insertText('## ', '')" title="标题2">H2</el-button>
            <el-button size="small" @click="insertText('### ', '')" title="标题3">H3</el-button>
          </el-button-group>
          <el-button-group>
            <el-button size="small" @click="insertText('- ', '')" title="无序列表">列表</el-button>
            <el-button size="small" @click="insertText('1. ', '')" title="有序列表">序号</el-button>
            <el-button size="small" @click="insertText('> ', '')" title="引用">引用</el-button>
            <el-button size="small" @click="insertText('`', '`')" title="行内代码">代码</el-button>
          </el-button-group>
          <el-button-group>
            <el-button size="small" @click="insertText('[链接文字](', ')')" title="插入链接">链接</el-button>
            <el-button size="small" @click="insertText('![图片描述](', ')')" title="插入图片">图片</el-button>
            <el-button size="small" @click="insertText('```\n', '\n```')" title="代码块">代码块</el-button>
            <el-button size="small" @click="insertText('---\n', '')" title="分割线">分割线</el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 编辑器主体 -->
      <div class="editor-body" :class="{ 'split-view': showPreview }">
        <!-- 编辑区域 -->
        <div class="editor-pane">
          <el-input
            ref="textareaRef"
            v-model="localValue"
            type="textarea"
            placeholder="请输入Markdown内容..."
            class="markdown-textarea"
            @input="handleInput"
            @focus="handleEditorFocus"
            resize="none"
          />
        </div>

        <!-- 预览区域 -->
        <div v-if="showPreview" class="preview-pane">
          <div class="preview-container" v-html="renderedMarkdown"></div>
        </div>
      </div>
    </div>

    <!-- 专注编辑模式 -->
    <div v-else class="focus-mode">
      <!-- 专注模式顶部栏 -->
      <div class="focus-header">
        <div class="focus-header-left">
          <el-button size="small" @click="exitFocusMode" :icon="Back" type="info">
            返回
          </el-button>
          <span class="focus-title">专注编辑模式</span>
        </div>

        <div class="focus-header-center">
          <!-- 视图切换按钮组 -->
          <div class="view-switcher">
            <el-button-group>
              <el-button
                size="small"
                @click="focusViewMode = 'split'"
                :type="focusViewMode === 'split' ? 'primary' : ''"
                title="分栏视图"
              >
                <el-icon><Grid /></el-icon>
                分栏视图
              </el-button>
              <el-button
                size="small"
                @click="focusViewMode = 'edit'"
                :type="focusViewMode === 'edit' ? 'primary' : ''"
                title="全屏编辑"
              >
                <el-icon><Edit /></el-icon>
                专注编辑
              </el-button>
              <el-button
                size="small"
                @click="focusViewMode = 'preview'"
                :type="focusViewMode === 'preview' ? 'primary' : ''"
                title="全屏预览"
              >
                <el-icon><View /></el-icon>
                全屏预览
              </el-button>
            </el-button-group>
          </div>
        </div>

        <div class="focus-header-right">
          <el-button size="small" @click="openFullscreenPreview" :icon="FullScreen">
            弹窗预览
          </el-button>
        </div>
      </div>

      <!-- 编辑工具栏 -->
      <div class="focus-toolbar" v-if="focusViewMode !== 'preview'">
        <div class="focus-toolbar-content">
          <el-button-group>
            <el-button size="small" @click="insertText('**', '**')" title="粗体">
              <strong>B</strong>
            </el-button>
            <el-button size="small" @click="insertText('*', '*')" title="斜体">
              <em>I</em>
            </el-button>
            <el-button size="small" @click="insertText('# ', '')" title="标题1">H1</el-button>
            <el-button size="small" @click="insertText('## ', '')" title="标题2">H2</el-button>
            <el-button size="small" @click="insertText('### ', '')" title="标题3">H3</el-button>
          </el-button-group>
          <el-button-group>
            <el-button size="small" @click="insertText('- ', '')" title="无序列表">列表</el-button>
            <el-button size="small" @click="insertText('1. ', '')" title="有序列表">序号</el-button>
            <el-button size="small" @click="insertText('> ', '')" title="引用">引用</el-button>
            <el-button size="small" @click="insertText('`', '`')" title="行内代码">代码</el-button>
          </el-button-group>
          <el-button-group>
            <el-button size="small" @click="insertText('[链接文字](', ')')" title="插入链接">链接</el-button>
            <el-button size="small" @click="insertText('![图片描述](', ')')" title="插入图片">图片</el-button>
            <el-button size="small" @click="insertText('```\n', '\n```')" title="代码块">代码块</el-button>
            <el-button size="small" @click="insertText('---\n', '')" title="分割线">分割线</el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 专注模式编辑器主体 -->
      <div class="focus-editor-body" :class="`view-${focusViewMode}`">
        <!-- 编辑区域 -->
        <div v-if="focusViewMode !== 'preview'" class="focus-editor-pane">
          <el-input
            ref="focusTextareaRef"
            v-model="localValue"
            type="textarea"
            placeholder="专注编辑模式 - 请输入Markdown内容..."
            class="focus-markdown-textarea"
            @input="handleInput"
            resize="none"
          />
        </div>

        <!-- 预览区域 -->
        <div v-if="focusViewMode !== 'edit'" class="focus-preview-pane">
          <div class="focus-preview-container" v-html="renderedMarkdown"></div>
        </div>
      </div>
    </div>

    <!-- 专注模式提示弹窗 -->
    <el-dialog
      v-model="focusModePromptVisible"
      title="检测到文章编辑"
      width="500px"
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="focus-prompt-content">
        <el-icon class="focus-prompt-icon"><Edit /></el-icon>
        <p>检测到您开始编辑文章内容</p>
        <p>是否进入<strong>专注编辑模式</strong>？</p>
        <p class="focus-prompt-desc">专注模式提供全屏编辑体验，减少干扰，提高写作效率</p>
      </div>
      <template #footer>
        <el-button @click="declineFocusMode">继续当前模式</el-button>
        <el-button type="primary" @click="acceptFocusMode">进入专注模式</el-button>
      </template>
    </el-dialog>

    <!-- 全屏预览弹窗 -->
    <el-dialog
      v-model="fullscreenPreviewVisible"
      title="全屏预览"
      :width="'90%'"
      :top="'5vh'"
      :show-close="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      class="fullscreen-preview-dialog"
    >
      <div class="fullscreen-preview-container" v-html="renderedMarkdown"></div>
      <template #footer>
        <el-button @click="fullscreenPreviewVisible = false">关闭</el-button>
        <el-button type="primary" @click="copyMarkdown">复制Markdown</el-button>
        <el-button type="success" @click="copyHtml">复制HTML</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue'
import { marked } from 'marked'
import { FullScreen, Edit, Back, Grid, View } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  height: {
    type: String,
    default: '500px'
  }
})

const emit = defineEmits(['update:modelValue'])

const textareaRef = ref()
const focusTextareaRef = ref()
const localValue = ref(props.modelValue)
const showPreview = ref(true) // 默认显示预览
const fullscreenPreviewVisible = ref(false) // 全屏预览弹窗

// 专注模式相关状态
const focusMode = ref(false) // 是否在专注模式
const focusModePromptVisible = ref(false) // 专注模式提示弹窗
const focusViewMode = ref('split') // 专注模式视图：split/edit/preview
const hasStartedEditing = ref(false) // 是否已开始编辑
const editStartTime = ref(0) // 编辑开始时间

// 配置marked
marked.setOptions({
  breaks: true,
  gfm: true,
  highlight: function(code, lang) {
    return `<pre><code class="language-${lang}">${code}</code></pre>`
  }
})

// 渲染Markdown
const renderedMarkdown = computed(() => {
  if (!localValue.value) return '<p class="empty-content">暂无内容</p>'
  return marked(localValue.value)
})

// 监听外部值变化
watch(() => props.modelValue, (newVal) => {
  localValue.value = newVal
})

// 处理输入
const handleInput = () => {
  emit('update:modelValue', localValue.value)
}

// 切换预览
const togglePreview = () => {
  showPreview.value = !showPreview.value
}

// 打开全屏预览
const openFullscreenPreview = () => {
  fullscreenPreviewVisible.value = true
}

// 复制Markdown内容
const copyMarkdown = async () => {
  try {
    await navigator.clipboard.writeText(localValue.value)
    ElMessage.success('Markdown内容已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败，请手动复制')
  }
}

// 复制HTML内容
const copyHtml = async () => {
  try {
    await navigator.clipboard.writeText(renderedMarkdown.value)
    ElMessage.success('HTML内容已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败，请手动复制')
  }
}

// 处理编辑器获得焦点
const handleEditorFocus = () => {
  if (!hasStartedEditing.value && !focusMode.value) {
    hasStartedEditing.value = true
    editStartTime.value = Date.now()

    // 延迟显示提示，避免误触
    setTimeout(() => {
      if (hasStartedEditing.value && !focusMode.value) {
        focusModePromptVisible.value = true
      }
    }, 3000) // 3秒后显示提示
  }
}

// 进入专注模式
const enterFocusMode = () => {
  focusMode.value = true
  focusViewMode.value = 'split'
  document.body.style.overflow = 'hidden' // 禁止页面滚动
}

// 退出专注模式
const exitFocusMode = () => {
  focusMode.value = false
  document.body.style.overflow = '' // 恢复页面滚动
}

// 接受专注模式
const acceptFocusMode = () => {
  focusModePromptVisible.value = false
  enterFocusMode()
}

// 拒绝专注模式
const declineFocusMode = () => {
  focusModePromptVisible.value = false
  hasStartedEditing.value = true // 标记已处理，不再提示
}

// 插入文本
const insertText = async (before, after = '') => {
  await nextTick()
  const currentTextareaRef = focusMode.value ? focusTextareaRef : textareaRef
  const textarea = currentTextareaRef.value?.textarea || currentTextareaRef.value
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const selectedText = localValue.value.substring(start, end)

  const newText = before + selectedText + after
  const newValue = localValue.value.substring(0, start) + newText + localValue.value.substring(end)

  localValue.value = newValue
  emit('update:modelValue', newValue)

  // 重新设置光标位置
  await nextTick()
  const newCursorPos = start + before.length + selectedText.length
  textarea.focus()
  textarea.setSelectionRange(newCursorPos, newCursorPos)
}
</script>

<style scoped>
.markdown-editor {
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  overflow: hidden;
  background: #fff;
}

/* 普通模式顶部控制栏 */
.normal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-bottom: 1px solid #e4e7ed;
}

.normal-header-left {
  display: flex;
  align-items: center;
}

.editor-title {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.normal-header-right {
  display: flex;
  align-items: center;
}

.view-controls {
  background: rgba(255,255,255,0.8);
  border-radius: 6px;
  padding: 2px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.view-controls .el-button {
  border: none;
  background: transparent;
  margin: 0 1px;
  transition: all 0.3s ease;
}

.view-controls .el-button:hover {
  background: rgba(64, 158, 255, 0.1);
}

.view-controls .el-button.el-button--primary {
  background: #409eff;
  color: white;
}

.view-controls .el-button.el-button--warning {
  background: #e6a23c;
  color: white;
}

/* 编辑工具栏 */
.editor-toolbar {
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #fafafa;
}

.toolbar-content {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: center;
}

.editor-body {
  display: flex;
  height: 500px;
}

.editor-body.split-view .editor-pane {
  width: 50%;
  border-right: 1px solid #e4e7ed;
}

.editor-body:not(.split-view) .editor-pane {
  width: 100%;
}

/* 响应式设计 - 普通模式 */
@media (max-width: 768px) {
  .normal-header {
    flex-direction: column;
    gap: 12px;
    padding: 12px;
  }

  .normal-header-right {
    width: 100%;
    justify-content: center;
  }

  .view-controls .el-button {
    padding: 6px 8px;
    font-size: 12px;
  }

  .toolbar-content {
    justify-content: flex-start;
    gap: 6px;
  }

  .editor-body.split-view {
    flex-direction: column;
    height: auto;
  }

  .editor-body.split-view .editor-pane {
    width: 100%;
    height: 300px;
    border-right: none;
    border-bottom: 1px solid #e4e7ed;
  }

  .preview-pane {
    width: 100% !important;
    height: 300px;
  }
}

@media (max-width: 480px) {
  .editor-title {
    font-size: 12px;
  }

  .view-controls .el-button {
    padding: 4px 6px;
    font-size: 11px;
  }

  .view-controls .el-button .el-icon {
    margin-right: 2px;
  }
}

.editor-pane {
  display: flex;
  flex-direction: column;
}

.preview-pane {
  width: 50%;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.markdown-textarea {
  flex: 1;
  border: none;
}

.markdown-textarea :deep(.el-textarea__inner) {
  border: none;
  border-radius: 0;
  resize: none;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  line-height: 1.6;
  height: 100% !important;
  min-height: auto !important;
}

.preview-container {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background: #fff;
  border-left: 1px solid #e4e7ed;
}

.empty-content {
  color: #999;
  text-align: center;
  margin-top: 100px;
}

/* Markdown预览样式 */
.preview-container :deep(h1),
.preview-container :deep(h2),
.preview-container :deep(h3),
.preview-container :deep(h4),
.preview-container :deep(h5),
.preview-container :deep(h6) {
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
  line-height: 1.25;
}

.preview-container :deep(h1) {
  font-size: 2em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 10px;
}

.preview-container :deep(h2) {
  font-size: 1.5em;
  border-bottom: 1px solid #eaecef;
  padding-bottom: 8px;
}

.preview-container :deep(p) {
  margin-bottom: 16px;
  line-height: 1.6;
}

.preview-container :deep(code) {
  background: #f6f8fa;
  padding: 2px 4px;
  border-radius: 3px;
  font-size: 0.9em;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
}

.preview-container :deep(pre) {
  background: #f6f8fa;
  padding: 16px;
  border-radius: 6px;
  overflow-x: auto;
  margin: 16px 0;
}

.preview-container :deep(pre code) {
  background: none;
  padding: 0;
}

.preview-container :deep(blockquote) {
  border-left: 4px solid #dfe2e5;
  padding-left: 16px;
  margin: 16px 0;
  color: #6a737d;
}

.preview-container :deep(ul),
.preview-container :deep(ol) {
  padding-left: 24px;
  margin-bottom: 16px;
}

.preview-container :deep(li) {
  margin-bottom: 4px;
}

.preview-container :deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin: 16px 0;
}

.preview-container :deep(table th),
.preview-container :deep(table td) {
  border: 1px solid #dfe2e5;
  padding: 8px 12px;
  text-align: left;
}

.preview-container :deep(table th) {
  background: #f6f8fa;
  font-weight: 600;
}

.preview-container :deep(img) {
  max-width: 100%;
  height: auto;
  margin: 16px 0;
}

.preview-container :deep(hr) {
  border: none;
  border-top: 1px solid #eaecef;
  margin: 24px 0;
}

/* 全屏预览样式 */
.fullscreen-preview-dialog :deep(.el-dialog) {
  margin: 0;
  height: 90vh;
  display: flex;
  flex-direction: column;
}

.fullscreen-preview-dialog :deep(.el-dialog__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.fullscreen-preview-dialog :deep(.el-dialog__body) {
  flex: 1;
  padding: 0;
  overflow: hidden;
}

.fullscreen-preview-dialog :deep(.el-dialog__footer) {
  padding: 16px 20px;
  border-top: 1px solid #e4e7ed;
  background: #fafafa;
}

.fullscreen-preview-container {
  height: 100%;
  padding: 24px;
  overflow-y: auto;
  background: #fff;
  font-size: 16px;
  line-height: 1.6;
}

/* 全屏预览中的样式优化 */
.fullscreen-preview-container :deep(h1) {
  font-size: 2.2em;
  margin-top: 0;
  margin-bottom: 24px;
}

.fullscreen-preview-container :deep(h2) {
  font-size: 1.8em;
  margin-top: 32px;
  margin-bottom: 20px;
}

.fullscreen-preview-container :deep(h3) {
  font-size: 1.4em;
  margin-top: 24px;
  margin-bottom: 16px;
}

.fullscreen-preview-container :deep(p) {
  margin-bottom: 18px;
  line-height: 1.7;
}

.fullscreen-preview-container :deep(pre) {
  font-size: 14px;
  line-height: 1.5;
}

.fullscreen-preview-container :deep(blockquote) {
  margin: 20px 0;
  padding-left: 20px;
}

.fullscreen-preview-container :deep(table) {
  margin: 20px 0;
  font-size: 15px;
}

.fullscreen-preview-container :deep(img) {
  margin: 20px 0;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

/* 专注模式样式 */
.focus-mode {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: #fff;
  z-index: 2000;
  display: flex;
  flex-direction: column;
}

/* 专注模式顶部栏 */
.focus-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  border-bottom: 1px solid #e4e7ed;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.focus-header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.focus-title {
  font-weight: 600;
  font-size: 16px;
  color: white;
}

.focus-header-center {
  flex: 1;
  display: flex;
  justify-content: center;
}

.view-switcher {
  background: rgba(255,255,255,0.1);
  border-radius: 8px;
  padding: 4px;
}

.view-switcher .el-button-group .el-button {
  background: transparent;
  border: none;
  color: white;
  font-weight: 500;
  padding: 8px 16px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.view-switcher .el-button:hover {
  background: rgba(255,255,255,0.2);
}

.view-switcher .el-button.el-button--primary {
  background: white;
  color: #667eea;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.focus-header-right {
  display: flex;
  align-items: center;
}

.focus-header-right .el-button {
  background: rgba(255,255,255,0.1);
  border: 1px solid rgba(255,255,255,0.3);
  color: white;
}

.focus-header-right .el-button:hover {
  background: rgba(255,255,255,0.2);
}

/* 编辑工具栏 */
.focus-toolbar {
  padding: 12px 24px;
  border-bottom: 1px solid #e4e7ed;
  background: #fafafa;
}

.focus-toolbar-content {
  display: flex;
  gap: 8px;
  justify-content: center;
  flex-wrap: wrap;
}

.focus-editor-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.focus-editor-body.view-split {
  /* 分栏模式 */
}

.focus-editor-body.view-split .focus-editor-pane {
  width: 50%;
  border-right: 1px solid #e4e7ed;
}

.focus-editor-body.view-split .focus-preview-pane {
  width: 50%;
}

.focus-editor-body.view-edit .focus-editor-pane {
  width: 100%;
}

.focus-editor-body.view-preview .focus-preview-pane {
  width: 100%;
}

.focus-editor-pane {
  display: flex;
  flex-direction: column;
  background: #fff;
}

.focus-preview-pane {
  display: flex;
  flex-direction: column;
  background: #fff;
}

.focus-markdown-textarea {
  flex: 1;
  border: none;
}

.focus-markdown-textarea :deep(.el-textarea__inner) {
  border: none;
  border-radius: 0;
  resize: none;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 16px;
  line-height: 1.8;
  height: 100% !important;
  min-height: auto !important;
  padding: 24px;
}

.focus-preview-container {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  font-size: 16px;
  line-height: 1.7;
}

/* 专注模式提示弹窗样式 */
.focus-prompt-content {
  text-align: center;
  padding: 20px 0;
}

.focus-prompt-icon {
  font-size: 48px;
  color: #409eff;
  margin-bottom: 16px;
}

.focus-prompt-content p {
  margin: 8px 0;
  font-size: 16px;
  line-height: 1.6;
}

.focus-prompt-desc {
  color: #666;
  font-size: 14px !important;
  margin-top: 16px !important;
}

/* 响应式设计 - 专注模式 */
@media (max-width: 768px) {
  .focus-header {
    flex-direction: column;
    gap: 12px;
    padding: 16px;
  }

  .focus-header-center {
    order: 2;
    width: 100%;
  }

  .focus-header-right {
    order: 3;
    width: 100%;
    justify-content: center;
  }

  .view-switcher .el-button {
    padding: 6px 12px;
    font-size: 12px;
  }

  .focus-toolbar-content {
    justify-content: flex-start;
    gap: 6px;
  }

  .focus-editor-body.view-split {
    flex-direction: column;
  }

  .focus-editor-body.view-split .focus-editor-pane {
    width: 100%;
    height: 50%;
    border-right: none;
    border-bottom: 1px solid #e4e7ed;
  }

  .focus-editor-body.view-split .focus-preview-pane {
    width: 100%;
    height: 50%;
  }
}

@media (max-width: 480px) {
  .focus-header {
    padding: 12px;
  }

  .focus-title {
    font-size: 14px;
  }

  .view-switcher .el-button {
    padding: 4px 8px;
    font-size: 11px;
  }

  .view-switcher .el-button .el-icon {
    margin-right: 2px;
  }
}
</style>
