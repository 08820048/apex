import api from './config'

export const categoryApi = {
  // 获取所有分类
  getList: () => {
    return api.get('/admin/categories')
  },

  // 获取分类详情
  getDetail: (id) => {
    return api.get(`/admin/categories/${id}`)
  },

  // 创建分类
  create: (data) => {
    return api.post('/admin/categories', data)
  },

  // 更新分类
  update: (id, data) => {
    return api.put(`/admin/categories/${id}`, data)
  },

  // 删除分类
  delete: (id) => {
    return api.delete(`/admin/categories/${id}`)
  }
}
