/**
 * 用户相关类型定义
 */

// 用户信息
export interface User {
  id: number
  username: string
  email: string
  avatar?: string
  nickname?: string
  bio?: string
  role: 'ADMIN' | 'USER'
  status: number
  createTime: string
}

// 登录请求
export interface LoginDTO {
  username: string
  password: string
}

// 登录响应
export interface LoginVO {
  token: string
  user: User
}

// 注册请求
export interface RegisterDTO {
  username: string
  password: string
  email: string
  nickname?: string
}
