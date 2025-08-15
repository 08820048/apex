import api from './config'

export const portfolioApi = {
  // 获取所有作品集
  getList: () => {
    return api.get('/admin/portfolios')
  },

  // 获取作品集详情
  getDetail: (id) => {
    return api.get(`/admin/portfolios/${id}`)
  },

  // 创建作品集
  create: (data) => {
    return api.post('/admin/portfolios', data)
  },

  // 更新作品集
  update: (id, data) => {
    return api.put(`/admin/portfolios/${id}`, data)
  },

  // 删除作品集
  delete: (id) => {
    return api.delete(`/admin/portfolios/${id}`)
  }
}
