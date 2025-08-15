import api from './config'

export const friendLinkApi = {
  // 获取所有友链
  getList: () => {
    return api.get('/admin/friend-links')
  },

  // 获取友链详情
  getDetail: (id) => {
    return api.get(`/admin/friend-links/${id}`)
  },

  // 创建友链
  create: (data) => {
    return api.post('/admin/friend-links', data)
  },

  // 更新友链
  update: (id, data) => {
    return api.put(`/admin/friend-links/${id}`, data)
  },

  // 删除友链
  delete: (id) => {
    return api.delete(`/admin/friend-links/${id}`)
  }
}
