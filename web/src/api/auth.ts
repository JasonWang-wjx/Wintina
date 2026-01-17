import request from './requests'
import type { LoginDTO, LoginVO, RegisterDTO, User } from '@/types'

/**
 * 认证相关接口
 */
export const authApi = {
  /**
   * 登录
   */
  login(data: LoginDTO) {
    return request<LoginVO>({
      url: '/auth/login',
      method: 'POST',
      data,
    })
  },

  /**
   * 注册
   */
  register(data: RegisterDTO) {
    return request({
      url: '/auth/register',
      method: 'POST',
      data,
    })
  },

  /**
   * 登出
   */
  logout() {
    return request({
      url: '/auth/logout',
      method: 'POST',
    })
  },

  /**
   * 获取当前用户信息
   */
  getUserInfo() {
    return request<User>({
      url: '/auth/info',
      method: 'GET',
    })
  },
}
