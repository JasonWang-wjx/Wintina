<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const CAPABILITIES_VIDEO_URL =
  'https://d8j0ntlcm91z4.cloudfront.net/user_38xzZboKViGWJOttwIXH07lWA1P/hf_20260418_094631_d30ab262-45ee-4b7d-99f3-5d5848c8ef13.mp4'

const router = useRouter()
const userStore = useUserStore()

const heroVideo = ref<HTMLVideoElement | null>(null)
const heroFrame = ref<number>()
const heroFadingOut = ref(false)

const isLoggedIn = computed(() => userStore.isLogin)
const primaryCtaLabel = computed(() => (isLoggedIn.value ? '继续浏览文章' : '开始登录'))
const entryLabel = computed(() => (isLoggedIn.value ? '已登录' : '登录'))

const capabilityCards = [
  {
    title: '个性化推荐',
    tags: ['兴趣建模', '阅读轨迹', '实时更新', '精准触达'],
    description: '让每位读者打开首页时，都优先看到更可能产生共鸣的内容。',
  },
  {
    title: '创作工作流',
    tags: ['灵感生成', '结构建议', '高效发布', '风格统一'],
    description: '从选题到落稿再到发布，用更轻量的步骤完成高质量内容表达。',
  },
  {
    title: '博客洞察',
    tags: ['互动分析', '趋势判断', '用户画像', '增长决策'],
    description: '把访问与互动数据转成可执行判断，持续优化你的个人博客。',
  },
]

const fadeTo = (video: HTMLVideoElement, target: number, duration: number) => {
  if (heroFrame.value) {
    cancelAnimationFrame(heroFrame.value)
  }

  const startOpacity = Number.parseFloat(video.style.opacity || '0')
  const startTime = performance.now()

  const tick = (now: number) => {
    const progress = Math.min((now - startTime) / duration, 1)
    video.style.opacity = String(startOpacity + (target - startOpacity) * progress)

    if (progress < 1) {
      heroFrame.value = requestAnimationFrame(tick)
    }
  }

  heroFrame.value = requestAnimationFrame(tick)
}

const bindLoop = () => {
  const video = heroVideo.value
  if (!video) return () => {}

  const onLoadedData = () => {
    video.style.opacity = '0'
    video.play().catch(() => undefined)
    fadeTo(video, 1, 500)
  }

  const onTimeUpdate = () => {
    const remaining = video.duration - video.currentTime
    if (!heroFadingOut.value && remaining <= 0.55 && remaining > 0) {
      heroFadingOut.value = true
      fadeTo(video, 0, 500)
    }
  }

  const onEnded = () => {
    video.style.opacity = '0'
    window.setTimeout(() => {
      video.currentTime = 0
      video.play().catch(() => undefined)
      heroFadingOut.value = false
      fadeTo(video, 1, 500)
    }, 100)
  }

  video.addEventListener('loadeddata', onLoadedData)
  video.addEventListener('timeupdate', onTimeUpdate)
  video.addEventListener('ended', onEnded)

  return () => {
    video.removeEventListener('loadeddata', onLoadedData)
    video.removeEventListener('timeupdate', onTimeUpdate)
    video.removeEventListener('ended', onEnded)
  }
}

let cleanupHero: (() => void) | undefined

onMounted(() => {
  cleanupHero = bindLoop()
})

onBeforeUnmount(() => {
  cleanupHero?.()
  if (heroFrame.value) cancelAnimationFrame(heroFrame.value)
})

const scrollToSection = (id: string) => {
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

const goToLogin = () => {
  router.push('/login')
}

const goToPosts = () => {
  router.push('/posts')
}

const handlePrimaryAction = () => {
  if (isLoggedIn.value) {
    goToPosts()
    return
  }

  goToLogin()
}

const handleEntryAction = () => {
  if (isLoggedIn.value) {
    scrollToSection('hero')
    return
  }

  goToLogin()
}

const handleLogout = () => {
  userStore.logout()
  router.replace('/')
}
</script>

<template>
  <div class="cinematic-page blog-home-page">
    <header class="home-nav glass-chip reveal-up">
      <RouterLink to="/" class="home-nav__logo heading-serif">w</RouterLink>
      <nav class="home-nav__center">
        <button type="button" @click="scrollToSection('hero')">首页</button>
        <button type="button" @click="scrollToSection('capabilities')">能力</button>
        <button type="button" @click="goToPosts">博客</button>
        <button type="button">关于</button>
        <button type="button" class="home-nav__entry" @click="handleEntryAction">
          {{ entryLabel }}
        </button>
      </nav>
      <span class="home-nav__right">Wintina</span>
    </header>

    <section id="hero" class="home-section home-hero">
      <video
        ref="heroVideo"
        class="home-hero__video"
        :src="CAPABILITIES_VIDEO_URL"
        muted
        playsinline
        preload="auto"
      ></video>

      <div class="home-hero__mask"></div>

      <div class="home-hero__content reveal-up delay-2">
        <span class="glass-chip home-hero__badge">Personal Blog</span>
        <h1 class="heading-serif">Nothing is impossible to a willing heart.</h1>
        <p>
          在一个更安静、更沉浸的主页里，管理创作节奏、打磨内容表达，并让推荐系统把文章送达更契合的读者。
        </p>

        <div v-if="isLoggedIn" class="glass-panel home-hero__user">
          <div>
            <span>当前账号</span>
            <strong>{{ userStore.displayName || 'Wintina 用户' }}</strong>
          </div>
          <button type="button" class="glass-button--ghost" @click="handleLogout">退出登录</button>
        </div>

        <div class="home-hero__actions">
          <button type="button" class="glass-button" @click="handlePrimaryAction">
            {{ primaryCtaLabel }} ↗
          </button>
          <button type="button" class="home-hero__link" @click="scrollToSection('capabilities')">
            查看平台能力
          </button>
        </div>
      </div>
    </section>

    <section id="capabilities" class="home-section home-capabilities">
      <div class="home-capabilities__intro">
        <p>// Capabilities</p>
        <h2 class="heading-serif">为个人博客而生的智能能力</h2>
      </div>

      <div class="home-capabilities__grid">
        <article
          v-for="card in capabilityCards"
          :key="card.title"
          class="glass-panel capability-card"
        >
          <div class="capability-card__top">
            <div class="capability-card__icon"><span></span></div>
            <div class="capability-card__tags">
              <span v-for="tag in card.tags" :key="tag" class="glass-chip">{{ tag }}</span>
            </div>
          </div>

          <div class="capability-card__body">
            <h3 class="heading-serif">{{ card.title }}</h3>
            <p>{{ card.description }}</p>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<style scoped lang="scss">
.blog-home-page {
  background: #03060d;
}

.home-nav {
  position: fixed;
  top: 16px;
  /* 固定定位居中方案 */
  left: 0;
  right: 0;
  margin: 0 auto;

  z-index: 40;
  width: min(1180px, calc(100% - 24px));
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;
  padding: 8px 12px;
}

.home-nav__logo {
  justify-self: start;
  display: grid;
  place-items: center;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  font-size: 1.8rem;
}

.home-nav__center {
  justify-self: center;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.home-nav__center button {
  border: 0;
  background: transparent;
  color: rgba(255, 255, 255, 0.9);
  padding: 10px 12px;
}

.home-nav__entry {
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.14);
}

.home-nav__right {
  justify-self: end;
  color: rgba(255, 255, 255, 0.75);
  font-size: 0.86rem;
}

.home-section {
  position: relative;
  min-height: 100vh;
}

.home-hero {
  overflow: hidden;
}

.home-hero__video,
.home-hero__mask {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.home-hero__video {
  object-fit: cover;
  object-position: center;
  opacity: 0;
}

.home-hero__mask {
  background:
    radial-gradient(circle at 18% 18%, rgba(145, 181, 255, 0.22), transparent 26%),
    linear-gradient(180deg, rgba(3, 7, 16, 0.28) 0%, rgba(3, 8, 18, 0.72) 72%, #040812 100%);
}

.home-hero__content {
  position: relative;
  z-index: 1;
  max-width: 860px;
  margin: 0 auto;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 120px 20px 40px;
}

.home-hero__badge {
  padding: 9px 14px;
}

.home-hero__content h1 {
  margin: 24px 0 16px;
  font-size: clamp(3rem, 7.2vw, 5.6rem);
  line-height: 0.88;
}

.home-hero__content p {
  color: rgba(255, 255, 255, 0.9);
  line-height: 1.72;
  max-width: 700px;
}

.home-hero__user {
  margin-top: 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px;
}

.home-hero__user span {
  display: block;
  color: var(--text-muted);
  margin-bottom: 4px;
}

.home-hero__actions {
  display: flex;
  gap: 14px;
  margin-top: 24px;
}

.home-hero__actions .glass-button {
  border: 0;
}

.home-hero__link {
  border: 0;
  background: transparent;
  color: rgba(255, 255, 255, 0.92);
}

.home-capabilities {
  padding: 110px 24px 60px;
  background:
    radial-gradient(circle at top, rgba(113, 150, 255, 0.12), transparent 20%),
    linear-gradient(180deg, #040812 0%, #050a14 100%);
}

.home-capabilities__intro {
  max-width: 1120px;
  margin: 0 auto;
}

.home-capabilities__intro p {
  margin: 0 0 16px;
  color: var(--text-muted);
}

.home-capabilities__intro h2 {
  margin: 0;
  font-size: clamp(2.5rem, 6vw, 4.8rem);
  line-height: 0.92;
}

.home-capabilities__grid {
  max-width: 1120px;
  margin: 44px auto 0;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 20px;
}

.capability-card {
  min-height: 340px;
  padding: 22px;
  display: flex;
  flex-direction: column;
}

.capability-card__top {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.capability-card__icon {
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.1);
}

.capability-card__icon span {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.92);
}

.capability-card__tags {
  max-width: 74%;
  display: flex;
  justify-content: flex-end;
  flex-wrap: wrap;
  gap: 6px;
}

.capability-card__tags .glass-chip {
  font-size: 11px;
  padding: 6px 10px;
}

.capability-card__body {
  margin-top: auto;
}

.capability-card__body h3 {
  margin: 0;
  font-size: clamp(1.8rem, 3vw, 2.6rem);
  line-height: 0.95;
}

.capability-card__body p {
  margin: 12px 0 0;
  color: var(--text-secondary);
  line-height: 1.65;
}

@media (max-width: 1024px) {
  .home-nav {
    grid-template-columns: auto 1fr;
    gap: 12px;
  }

  .home-nav__center {
    justify-self: end;
  }

  .home-nav__right {
    display: none;
  }

  .home-capabilities__grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .home-nav {
    width: calc(100% - 12px);
    top: 8px;
  }

  .home-nav__center {
    display: none;
  }

  .home-hero__actions,
  .home-hero__user {
    flex-direction: column;
    width: 100%;
  }
}
</style>
