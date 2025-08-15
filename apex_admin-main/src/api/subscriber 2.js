import api from './config'

export const subscriberApi = {
  // 获取所有订阅者
  getList: (params = {}) => {
    const { page = 0, size = 10, ...otherParams } = params
    return api.get('/admin/email-subscribers', {
      params: { page, size, ...otherParams }
    })
  },

  // 删除订阅者
  delete: (id) => {
    return api.delete(`/admin/email-subscribers/${id}`)
  }
}
