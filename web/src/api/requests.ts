import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import type { ApiResponse } from '@/types/common.ts'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL, // 接口请求的基础域名/路径
  timeout: 10000, // 请求超时时间
})

// 请求拦截器
// 统一添加用户身份令牌（token）,实现接口的身份认证
service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    // 添加 token
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  },
)

// 响应拦截器
// 统一处理响应数据、统一捕获并处理接口错误
service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const { code, message, data } = response.data

    // 请求成功
    if (code === 200) {
      return data
    }

    // 业务错误
    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message || '请求失败'))
  },
  (error) => {
    console.error('响应错误:', error)

    // 未登录或 token 过期
    if (error.response?.status === 401) {
      ElMessage.error('请先登录')

      useUserStore().logout()
      window.location.href = '/login'

      return Promise.reject(error)
    }

    // 权限不足
    if (error.response?.status === 403) {
      ElMessage.error('无权限访问')
      return Promise.reject(error)
    }

    // 服务器错误
    if (error.response?.status >= 500) {
      ElMessage.error('服务器错误,请稍后重试')
      return Promise.reject(error)
    }

    // 其他错误
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)


// 🔥 新增核心：封装泛型请求函数，适配authApi的泛型调用，约束返回值类型
// 泛型T：指定接口返回的「真实业务数据类型」（如LoginVO、User）
// 返回Promise<T>：让TS能正确推导接口返回值类型，解决之前的TS2339错误
const request = <T = any>(config: AxiosRequestConfig): Promise<T> => {
  return service(config)
}

// 将封装好的 Axios 实例 service 导出
export default request
