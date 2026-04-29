import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { authApi } from '@/api/auth'
import type { LoginDTO, User } from '@/types/user'

const USER_STORAGE_KEY = 'userInfo'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const storedUserInfo = localStorage.getItem(USER_STORAGE_KEY)
  const userInfo = ref<User | null>(storedUserInfo ? JSON.parse(storedUserInfo) : null)

  const isLogin = computed(() => !!token.value)
  const displayName = computed(() => userInfo.value?.nickname || userInfo.value?.username || '')
  const isAdmin = computed(
    () => userInfo.value?.role === 'ADMIN' || userInfo.value?.roleId === 1,
  )

  const setUserInfo = (data: User) => {
    userInfo.value = data
    localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(data))
  }

  const login = async (loginForm: LoginDTO) => {
    const data = await authApi.login(loginForm)
    token.value = data.token
    localStorage.setItem('token', data.token)
    setUserInfo({
      userId: data.userId,
      username: data.username,
      nickname: data.nickname,
      avatar: data.avatar,
      email: data.email,
      role: 'USER',
    })
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem(USER_STORAGE_KEY)
  }

  return {
    token,
    userInfo,
    displayName,
    isLogin,
    isAdmin,
    login,
    logout,
  }
})
