import api from './config'

export const authApi = {
  // 管理员登录
  login: (credentials) => {
    return api.post('/auth/login', credentials)
  },

  // 获取当前用户信息
  getCurrentUser: () => {
    return api.get('/admin/auth/me')
  },

  // 刷新token
  refreshToken: () => {
    return api.post('/admin/auth/refresh')
  },

  // 登出
  logout: () => {
    return api.post('/admin/auth/logout')
  }
}
