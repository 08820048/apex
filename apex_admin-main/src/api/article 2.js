import api from './config'

export const articleApi = {
  // 获取文章列表（包括草稿）
  getList: (params = {}) => {
    const { page = 0, size = 10, ...otherParams } = params
    return api.get('/admin/articles', {
      params: { page, size, ...otherParams }
    })
  },

  // 获取文章详情
  getDetail: (id) => {
    return api.get(`/admin/articles/${id}`)
  },

  // 创建文章
  create: (data) => {
    return api.post('/admin/articles', data)
  },

  // 更新文章
  update: (id, data) => {
    return api.put(`/admin/articles/${id}`, data)
  },

  // 删除文章
  delete: (id) => {
    return api.delete(`/admin/articles/${id}`)
  },

  // 发布文章
  publish: (id) => {
    return api.put(`/admin/articles/${id}/publish`)
  },

  // 取消发布文章
  unpublish: (id) => {
    return api.put(`/admin/articles/${id}/unpublish`)
  }
}
