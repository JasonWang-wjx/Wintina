import type { PageResult } from './common'

export interface PostQueryParams {
  current?: number
  size?: number
  categoryId?: number
  tagId?: number
  keyword?: string
}

export interface PostListItem {
  id: number
  title: string
  slug?: string
  summary?: string
  coverImage?: string
  status?: number
  viewCount?: number
  likeCount?: number
  commentCount?: number
  readTimeMinutes?: number
  userId?: number
  authorName?: string
  authorAvatar?: string
  categoryId?: number
  categoryName?: string
  tagIds?: number[]
  tagNames?: string[]
  publishTime?: string
  createTime?: string
  updateTime?: string
}

export interface TocItem {
  id: string
  text: string
  level: number
}

export interface RelatedPostItem {
  id: number
  title: string
  coverImage?: string
  publishTime?: string
  viewCount?: number
}

export interface PostDetail extends PostListItem {
  content?: string
  contentHtml?: string
  allowComment?: number
  isTop?: number
  isRecommend?: number
  collectCount?: number
  sortOrder?: number
  seoKeywords?: string
  seoDescription?: string
  authorBio?: string
  authorPostCount?: number
  toc?: TocItem[]
  relatedPosts?: RelatedPostItem[]
}

export interface SavePostPayload {
  title: string
  summary?: string
  content: string
  contentHtml?: string
  categoryId: number
  status: number
  slug?: string
  coverImage?: string
  tagIds?: number[]
  allowComment?: number
  isTop?: number
  isRecommend?: number
  sortOrder?: number
  seoKeywords?: string
  seoDescription?: string
}

export interface PostEditorCategory {
  id: number
  name: string
}

export interface PostEditorTag {
  id: number
  name: string
  color?: string
}

export interface PostEditorStatusOption {
  value: number
  label: string
}

export interface PostEditorOptions {
  categories: PostEditorCategory[]
  tags: PostEditorTag[]
  statusOptions: PostEditorStatusOption[]
}

export type PostPageResult = PageResult<PostListItem>
