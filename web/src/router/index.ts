import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import Home from '@/views/blog/Home.vue'
import Login from '@/views/auth/Login.vue'
import PostList from '@/views/blog/PostList.vue'
import PostDetail from '@/views/blog/PostDetail.vue'
import PostEditor from '@/views/blog/PostEditor.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/posts',
    name: 'PostList',
    component: PostList,
  },
  {
    path: '/posts/new',
    name: 'PostCreate',
    component: PostEditor,
  },
  {
    path: '/posts/:id/edit',
    name: 'PostEdit',
    component: PostEditor,
  },
  {
    path: '/posts/:id',
    name: 'PostDetail',
    component: PostDetail,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
