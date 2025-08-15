import api from './config'

export const tagApi = {
  // 获取所有标签
  getList: () => {
    return api.get('/admin/tags')
  },

  // 获取标签详情
  getDetail: (id) => {
    return api.get(`/admin/tags/${id}`)
  },

  // 创建标签
  create: (data) => {
    return api.post('/admin/tags', data)
  },

  // 更新标签
  update: (id, data) => {
    return api.put(`/admin/tags/${id}`, data)
  },

  // 删除标签
  delete: (id) => {
    return api.delete(`/admin/tags/${id}`)
  }
}
