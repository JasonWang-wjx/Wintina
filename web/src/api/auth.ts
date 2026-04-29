import request from './requests'
import type { LoginDTO, LoginVO, RegisterDTO } from '@/types'

export const authApi = {
  login(data: LoginDTO) {
    return request<LoginVO>({
      url: '/auth/login',
      method: 'POST',
      data,
    })
  },

  register(data: RegisterDTO) {
    return request({
      url: '/auth/register',
      method: 'POST',
      data,
    })
  },

  changePassword(data: { oldPassword: string; newPassword: string }) {
    return request({
      url: '/auth/password',
      method: 'PUT',
      data,
    })
  },
}
