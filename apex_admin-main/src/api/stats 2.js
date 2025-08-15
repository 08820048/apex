import api from './config'

export const statsApi = {
  // 获取访问统计
  getVisitStats: () => {
    return api.get('/admin/stats/visits')
  }
}
