/**
 * 用户相关类型定义
 */

export interface User {
  userId: number
  username: string
  email?: string
  avatar?: string
  nickname?: string
  bio?: string
  role?: 'ADMIN' | 'USER'
  roleId?: number
  status?: number
  createTime?: string
}

export interface LoginDTO {
  username: string
  password: string
}

export interface LoginVO {
  userId: number
  username: string
  nickname?: string
  avatar?: string
  email?: string
  token: string
}

export interface RegisterDTO {
  username: string
  password: string
  email: string
  nickname?: string
}
