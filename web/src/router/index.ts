import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import Home from '@/views/blog/Home.vue'
import Login from '@/views/auth/Login.vue'

const routes: RouteRecordRaw[] = [
  // 首页
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  // 登录页
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
