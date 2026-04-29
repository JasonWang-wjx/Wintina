<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { authApi } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import type { LoginDTO, RegisterDTO } from '@/types'

const CAPABILITIES_VIDEO_URL =
  'https://d8j0ntlcm91z4.cloudfront.net/user_38xzZboKViGWJOttwIXH07lWA1P/hf_20260418_094631_d30ab262-45ee-4b7d-99f3-5d5848c8ef13.mp4'

type AuthMode = 'login' | 'register'

const router = useRouter()
const userStore = useUserStore()

if (userStore.isLogin) {
  router.replace('/')
}

const authMode = ref<AuthMode>('login')
const loginLoading = ref(false)
const registerLoading = ref(false)
const loginFormRef = ref<FormInstance>()
const registerFormRef = ref<FormInstance>()

const loginForm = reactive<LoginDTO>({
  username: '',
  password: '',
})

const registerForm = reactive<RegisterDTO>({
  username: '',
  email: '',
  password: '',
  nickname: '',
})

const loginRules: FormRules<LoginDTO> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

const registerRules: FormRules<RegisterDTO> = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为 3-20 位', trigger: 'blur' },
  ],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 位', trigger: 'blur' },
  ],
  nickname: [{ max: 20, message: '昵称不能超过 20 位', trigger: 'blur' }],
}

const authTitle = computed(() => (authMode.value === 'login' ? '欢迎回到你的博客空间' : '创建你的博客账号'))

const authSubtitle = computed(() =>
  authMode.value === 'login'
    ? '登录后继续写作、管理内容与查看个性化阅读反馈。'
    : '注册后即可开启智能推荐与沉浸式个人博客体验。',
)

const switchMode = (mode: AuthMode) => {
  authMode.value = mode
}

const submitLogin = async () => {
  if (!loginFormRef.value) return
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loginLoading.value = true
  try {
    await userStore.login(loginForm)
    ElMessage.success('登录成功，欢迎回来')
    await router.push('/')
  } finally {
    loginLoading.value = false
  }
}

const submitRegister = async () => {
  if (!registerFormRef.value) return
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  registerLoading.value = true
  try {
    await authApi.register({
      ...registerForm,
      nickname: registerForm.nickname?.trim() || registerForm.username,
    })
    ElMessage.success('注册成功，请使用新账号登录')
    authMode.value = 'login'
    loginForm.username = registerForm.username
    loginForm.password = ''
    registerFormRef.value.resetFields()
  } finally {
    registerLoading.value = false
  }
}
</script>

<template>
  <div class="cinematic-page blog-auth-page">
    <video class="blog-auth-video" :src="CAPABILITIES_VIDEO_URL" autoplay muted playsinline loop preload="auto"></video>
    <div class="blog-auth-mask"></div>

    <header class="blog-auth-nav glass-chip reveal-up">
      <RouterLink to="/" class="blog-auth-nav__brand">
        <span class="heading-serif">w</span>
        <span>Wintina Blog</span>
      </RouterLink>
      <span class="glass-chip">Personalized Writing Platform</span>
    </header>

    <main class="blog-auth-layout">
      <section class="blog-auth-copy reveal-up delay-2">
        <span class="glass-chip">Writing · Insight · Growth</span>
        <h1 class="heading-serif">让登录成为个人博客创作的起点</h1>
        <p>
          你可以在这里持续写作、沉淀知识与观点，并通过 AI 推荐把内容更稳定地触达真正感兴趣的读者。
        </p>

        <div class="blog-auth-copy__notes">
          <article class="glass-panel">
            <strong class="heading-serif">Focus</strong>
            <span>专注表达与持续输出</span>
          </article>
          <article class="glass-panel">
            <strong class="heading-serif">Signal</strong>
            <span>用数据反馈优化内容方向</span>
          </article>
        </div>
      </section>

      <section class="glass-panel blog-auth-panel reveal-up delay-3">
        <div class="blog-auth-panel__header">
          <div>
            <p>账号中心</p>
            <h2>{{ authTitle }}</h2>
            <span>{{ authSubtitle }}</span>
          </div>
          <div class="glass-chip blog-auth-tabs">
            <button type="button" :class="{ active: authMode === 'login' }" @click="switchMode('login')">登录</button>
            <button type="button" :class="{ active: authMode === 'register' }" @click="switchMode('register')">
              注册
            </button>
          </div>
        </div>

        <el-form
          v-show="authMode === 'login'"
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          label-position="top"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" size="large" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              show-password
              placeholder="请输入密码"
              size="large"
            />
          </el-form-item>
          <button type="button" class="glass-button blog-auth-submit" :disabled="loginLoading" @click="submitLogin">
            {{ loginLoading ? '正在登录...' : '立即登录' }}
          </button>
        </el-form>

        <el-form
          v-show="authMode === 'register'"
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          label-position="top"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="registerForm.username" placeholder="3-20 位用户名" size="large" />
          </el-form-item>
          <div class="blog-auth-grid">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱" size="large" />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="registerForm.nickname" placeholder="可选，默认使用用户名" size="large" />
            </el-form-item>
          </div>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              show-password
              placeholder="至少 6 位密码"
              size="large"
            />
          </el-form-item>
          <button
            type="button"
            class="glass-button blog-auth-submit"
            :disabled="registerLoading"
            @click="submitRegister"
          >
            {{ registerLoading ? '正在创建账号...' : '创建账号' }}
          </button>
        </el-form>

        <div class="blog-auth-panel__switch">
          <span>{{ authMode === 'login' ? '还没有账号？' : '已经有账号了？' }}</span>
          <button type="button" @click="switchMode(authMode === 'login' ? 'register' : 'login')">
            {{ authMode === 'login' ? '去注册' : '去登录' }}
          </button>
        </div>
      </section>
    </main>
  </div>
</template>

<style scoped lang="scss">
.blog-auth-page {
  position: relative;
  min-height: 100vh;
  padding: 26px;
}

.blog-auth-video,
.blog-auth-mask {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.blog-auth-video {
  object-fit: cover;
}

.blog-auth-mask {
  background:
    radial-gradient(circle at 16% 22%, rgba(142, 178, 255, 0.2), transparent 26%),
    linear-gradient(120deg, rgba(3, 8, 18, 0.78) 22%, rgba(4, 10, 20, 0.54) 56%, rgba(5, 10, 20, 0.82) 100%);
}

.blog-auth-nav,
.blog-auth-layout {
  position: relative;
  z-index: 1;
}

.blog-auth-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 14px;
}

.blog-auth-nav__brand {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-weight: 600;
}

.blog-auth-nav__brand .heading-serif {
  font-size: 1.9rem;
  line-height: 1;
}

.blog-auth-layout {
  min-height: calc(100vh - 84px);
  display: grid;
  grid-template-columns: minmax(0, 1.15fr) minmax(360px, 520px);
  gap: 30px;
  align-items: center;
}

.blog-auth-copy h1 {
  margin: 22px 0 18px;
  font-size: clamp(3rem, 7vw, 5.6rem);
  line-height: 0.9;
}

.blog-auth-copy p {
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.72;
  max-width: 660px;
}

.blog-auth-copy__notes {
  margin-top: 26px;
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  max-width: 540px;
}

.blog-auth-copy__notes article {
  padding: 18px;
  border-radius: var(--radius-card);
}

.blog-auth-copy__notes strong {
  display: block;
  font-size: 2rem;
}

.blog-auth-copy__notes span {
  color: var(--text-secondary);
}

.blog-auth-panel {
  padding: 26px;
  border-radius: var(--radius-panel);
}

.blog-auth-panel__header {
  display: flex;
  justify-content: space-between;
  gap: 14px;
  margin-bottom: 22px;
}

.blog-auth-panel__header p {
  margin: 0;
  color: var(--text-muted);
  letter-spacing: 0.14em;
  text-transform: uppercase;
  font-size: 0.8rem;
}

.blog-auth-panel__header h2 {
  margin: 8px 0;
}

.blog-auth-panel__header span {
  color: var(--text-secondary);
  line-height: 1.6;
}

.blog-auth-tabs {
  display: inline-flex;
  gap: 4px;
  padding: 4px;
}

.blog-auth-tabs button {
  border: 0;
  border-radius: 999px;
  background: transparent;
  color: var(--text-secondary);
  padding: 8px 14px;
}

.blog-auth-tabs button.active {
  background: rgba(255, 255, 255, 0.18);
  color: rgba(255, 255, 255, 0.96);
}

.blog-auth-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

:deep(.el-form-item__label) {
  color: rgba(255, 255, 255, 0.86);
}

:deep(.el-input__wrapper) {
  min-height: 50px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.08);
  box-shadow: inset 0 1px 1px rgba(255, 255, 255, 0.14);
}

:deep(.el-input__inner) {
  color: rgba(255, 255, 255, 0.95);
}

.blog-auth-submit {
  width: 100%;
  margin-top: 8px;
  border: 0;
}

.blog-auth-panel__switch {
  margin-top: 16px;
  display: flex;
  justify-content: center;
  gap: 8px;
}

.blog-auth-panel__switch span {
  color: var(--text-secondary);
}

.blog-auth-panel__switch button {
  border: 0;
  background: transparent;
  color: rgba(255, 255, 255, 0.94);
}

@media (max-width: 1024px) {
  .blog-auth-layout {
    grid-template-columns: 1fr;
    padding-top: 24px;
  }
}

@media (max-width: 760px) {
  .blog-auth-page {
    padding: 14px;
  }

  .blog-auth-nav {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .blog-auth-copy__notes,
  .blog-auth-grid {
    grid-template-columns: 1fr;
  }

  .blog-auth-panel__header {
    flex-direction: column;
  }
}
</style>
