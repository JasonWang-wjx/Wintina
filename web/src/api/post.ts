import request from './requests'
import type {
  PostDetail,
  PostEditorOptions,
  PostPageResult,
  PostQueryParams,
  SavePostPayload,
} from '@/types/post'

export const postApi = {
  pagePublishedPosts(params: PostQueryParams) {
    return request<PostPageResult>({
      url: '/posts',
      method: 'GET',
      params,
    })
  },

  getPostDetail(id: number | string) {
    return request<PostDetail>({
      url: `/posts/${id}`,
      method: 'GET',
    })
  },

  getPostEditorOptions() {
    return request<PostEditorOptions>({
      url: '/posts/options',
      method: 'GET',
    })
  },

  createPost(data: SavePostPayload) {
    return request<number>({
      url: '/posts',
      method: 'POST',
      data,
    })
  },

  updatePost(id: number | string, data: SavePostPayload) {
    return request<void>({
      url: `/posts/${id}`,
      method: 'PUT',
      data,
    })
  },
}
