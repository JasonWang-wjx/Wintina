import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '@/api/auth'  // 封装的用户认证相关接口（登录、获取用户信息），用于与后端交互获取用户数据
import type { LoginDTO, LoginVO, User } from '@/types/user'   // 分别约束「登录表单数据格式」和「用户信息数据格式」，保证类型安全，避免非法数据赋值

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<User | null>(null)

  const isLogin = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.role === 'ADMIN')

  // 登录
  const login = async (loginForm: LoginDTO) => {
    const data = await authApi.login(loginForm)
    token.value = data.token
    userInfo.value = data.user
    localStorage.setItem('token', data.token)
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  // 获取用户信息
  const getUserInfo = async () => {
    const data = await authApi.getUserInfo()
    userInfo.value = data
  }

  // 暴露对外可访问的内容
  return {
    token,
    userInfo,
    isLogin,
    isAdmin,
    login,
    logout,
    getUserInfo,
  }
})
