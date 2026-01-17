// 通用响应类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
}

// 分页参数
export interface PageParams {
  current: number
  size: number
}

// 分页响应
export interface PageResult<T = any> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}
