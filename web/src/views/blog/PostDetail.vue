<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { postApi } from '@/api/post'
import type { PostDetail } from '@/types/post'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const post = ref<PostDetail | null>(null)

const postId = computed(() => Number(route.params.id))
const hasValidPostId = computed(() => Number.isInteger(postId.value) && postId.value > 0)

const formatDate = (value?: string) => (value ? value.replace('T', ' ').slice(0, 10) : '未发布')

const formatDateTime = (value?: string) => (value ? value.replace('T', ' ').slice(0, 16) : '未发布')

const fetchDetail = async () => {
  if (!hasValidPostId.value) {
    ElMessage.error('文章参数错误')
    router.replace('/posts')
    return
  }

  loading.value = true
  try {
    post.value = await postApi.getPostDetail(postId.value)
  } catch (error: unknown) {
    console.error(error)
    const status = (error as { response?: { status?: number } })?.response?.status
    if (status === 404) {
      ElMessage.error('文章不存在或未发布')
      router.replace('/posts')
      return
    }
    ElMessage.error('获取文章详情失败')
  } finally {
    loading.value = false
  }
}

const goPost = (id: number) => {
  router.push(`/posts/${id}`)
}

onMounted(fetchDetail)
watch(() => route.params.id, fetchDetail)
</script>

<template>
  <div class="cinematic-page post-detail-page" v-loading="loading" element-loading-text="加载中...">
    <header class="post-shell glass-chip reveal-up">
      <RouterLink to="/" class="post-shell__brand heading-serif">w</RouterLink>
      <nav class="post-shell__nav">
        <RouterLink to="/">首页</RouterLink>
        <RouterLink to="/posts" class="is-active">博客</RouterLink>
        <a href="#">分类</a>
        <a href="#">标签</a>
      </nav>
      <button type="button" class="glass-button--ghost" @click="router.push('/posts')">返回列表</button>
    </header>

    <main class="post-detail-layout" v-if="post">
      <article class="glass-panel post-main reveal-up delay-2">
        <div class="post-main__cover" :style="{ backgroundImage: post.coverImage ? `url(${post.coverImage})` : '' }">
          <span class="glass-chip">{{ post.categoryName || '未分类' }}</span>
        </div>
        <h1 class="heading-serif">{{ post.title }}</h1>
        <p class="post-main__subtitle">{{ post.summary || '基于协同过滤与内容推荐的智能推荐系统实现。' }}</p>

        <div class="post-main__meta">
          <span>{{ post.authorName || 'Wintina' }}</span>
          <span>{{ formatDateTime(post.publishTime || post.createTime) }}</span>
          <span>{{ post.viewCount || 0 }} 阅读</span>
          <span>{{ post.readTimeMinutes || 1 }} 分钟阅读</span>
        </div>

        <div v-if="post.contentHtml" class="post-main__content" v-html="post.contentHtml"></div>
        <pre v-else class="post-main__content post-main__content--plain">{{ post.content || '暂无正文内容' }}</pre>

        <div class="post-main__actions">
          <button type="button" class="glass-button--ghost">点赞 {{ post.likeCount || 0 }}</button>
          <button type="button" class="glass-button--ghost">收藏 {{ post.collectCount || 0 }}</button>
          <button type="button" class="glass-button--ghost">分享</button>
        </div>
      </article>

      <aside class="post-side">
        <section class="glass-panel post-side-card reveal-up delay-2">
          <h3>作者信息</h3>
          <div class="post-author">
            <img v-if="post.authorAvatar" :src="post.authorAvatar" alt="作者头像" />
            <div v-else class="post-author__avatar"></div>
            <div>
              <strong>{{ post.authorName || 'Wintina' }}</strong>
              <p>{{ post.authorBio || '热爱技术，记录生活，分享思考。' }}</p>
            </div>
          </div>
          <div class="post-author__stats">
            <div><strong>{{ post.authorPostCount || 0 }}</strong><span>文章</span></div>
            <div><strong>{{ post.tagNames?.length || 0 }}</strong><span>标签</span></div>
            <div><strong>{{ post.viewCount || 0 }}</strong><span>阅读</span></div>
          </div>
        </section>

        <section class="glass-panel post-side-card reveal-up delay-3" v-if="post.toc?.length">
          <h3>文章目录</h3>
          <ul class="post-toc">
            <li v-for="item in post.toc" :key="item.id" :style="{ paddingLeft: `${(item.level - 1) * 12}px` }">
              {{ item.text }}
            </li>
          </ul>
        </section>

        <section class="glass-panel post-side-card reveal-up delay-4" v-if="post.relatedPosts?.length">
          <h3>相关文章</h3>
          <ul class="post-related">
            <li v-for="item in post.relatedPosts" :key="item.id" @click="goPost(item.id)">
              <span class="post-related__thumb" :style="{ backgroundImage: item.coverImage ? `url(${item.coverImage})` : '' }"></span>
              <div>
                <strong>{{ item.title }}</strong>
                <p>{{ formatDate(item.publishTime) }} · {{ item.viewCount || 0 }} 阅读</p>
              </div>
            </li>
          </ul>
        </section>
      </aside>
    </main>
  </div>
</template>

<style scoped lang="scss">
.post-detail-page {
  min-height: 100vh;
  padding: 20px;
}

.post-shell {
  width: min(1280px, calc(100% - 24px));
  margin: 0 auto;
  display: grid;
  grid-template-columns: auto 1fr auto;
  gap: 18px;
  align-items: center;
  padding: 14px 18px;
  position: sticky;
  top: 16px;
  z-index: 20;
}

.post-shell__brand {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  display: grid;
  place-items: center;
  background: rgba(255, 255, 255, 0.08);
  font-size: 1.8rem;
}

.post-shell__nav {
  display: flex;
  gap: 24px;
  color: var(--text-secondary);
}

.post-shell__nav .is-active {
  color: var(--text-primary);
}

.post-detail-layout {
  width: min(1280px, calc(100% - 24px));
  margin: 18px auto 0;
  display: grid;
  grid-template-columns: minmax(0, 1fr) 340px;
  gap: 18px;
}

.post-main {
  padding: 20px 24px 24px;
}

.post-main__cover {
  min-height: 260px;
  border-radius: 26px;
  background: linear-gradient(135deg, rgba(255, 193, 205, 0.38), rgba(255, 255, 255, 0.08));
  background-size: cover;
  background-position: center;
  padding: 18px;
}

.post-main h1 {
  margin: 20px 0 0;
  font-size: clamp(2.2rem, 5vw, 3.8rem);
  line-height: 0.95;
}

.post-main__subtitle {
  margin: 16px 0 0;
  color: var(--text-secondary);
  font-size: 1.08rem;
  line-height: 1.8;
}

.post-main__meta {
  margin-top: 16px;
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  color: var(--text-muted);
}

.post-main__content {
  margin-top: 26px;
  line-height: 1.8;
}

.post-main__content:deep(h1),
.post-main__content:deep(h2),
.post-main__content:deep(h3) {
  margin: 24px 0 10px;
}

.post-main__content:deep(p) {
  margin: 12px 0;
}

.post-main__content:deep(img) {
  max-width: 100%;
  border-radius: 16px;
}

.post-main__content--plain {
  white-space: pre-wrap;
  font-family: inherit;
}

.post-main__actions {
  margin-top: 26px;
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.post-side {
  display: grid;
  gap: 16px;
  align-content: start;
}

.post-side-card {
  padding: 18px;
}

.post-side-card h3 {
  margin: 0 0 16px;
  font-size: 1.15rem;
}

.post-author {
  display: flex;
  gap: 12px;
  align-items: center;
}

.post-author img,
.post-author__avatar {
  width: 68px;
  height: 68px;
  border-radius: 50%;
  object-fit: cover;
  background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0.08));
}

.post-author p {
  margin: 8px 0 0;
  color: var(--text-secondary);
}

.post-author__stats {
  margin-top: 14px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
}

.post-author__stats div {
  text-align: center;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.05);
  padding: 10px 6px;
}

.post-author__stats strong {
  display: block;
}

.post-author__stats span {
  color: var(--text-muted);
  font-size: 0.8rem;
}

.post-toc,
.post-related {
  margin: 0;
  padding: 0;
  list-style: none;
  display: grid;
  gap: 10px;
}

.post-toc li {
  color: var(--text-secondary);
  font-size: 0.92rem;
}

.post-related li {
  display: grid;
  grid-template-columns: 74px 1fr;
  gap: 10px;
  align-items: center;
  cursor: pointer;
}

.post-related__thumb {
  width: 74px;
  height: 54px;
  border-radius: 14px;
  background: linear-gradient(135deg, rgba(255, 193, 205, 0.38), rgba(255, 255, 255, 0.08));
  background-size: cover;
  background-position: center;
}

.post-related p {
  margin: 4px 0 0;
  color: var(--text-muted);
  font-size: 0.82rem;
}

@media (max-width: 1080px) {
  .post-detail-layout {
    grid-template-columns: 1fr;
  }

  .post-side {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 860px) {
  .post-shell {
    grid-template-columns: auto 1fr auto;
  }

  .post-shell__nav {
    display: none;
  }

  .post-side {
    grid-template-columns: 1fr;
  }
}
</style>
