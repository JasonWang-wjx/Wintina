<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { postApi } from '@/api/post'
import type { PostListItem } from '@/types/post'

const router = useRouter()

const loading = ref(false)
const keyword = ref('')
const current = ref(1)
const size = ref(6)
const total = ref(0)
const posts = ref<PostListItem[]>([])

const hasData = computed(() => posts.value.length > 0)

const formatDate = (value?: string) => (value ? value.replace('T', ' ').slice(0, 10) : '未发布')

const formatReading = (value?: number) => `${value || 1} 分钟阅读`

const fetchPosts = async () => {
  loading.value = true
  try {
    const data = await postApi.pagePublishedPosts({
      current: current.value,
      size: size.value,
      keyword: keyword.value.trim() || undefined,
    })
    posts.value = data.records || []
    total.value = data.total || 0
  } catch (error) {
    console.error(error)
    ElMessage.error('获取文章失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  current.value = 1
  fetchPosts()
}

const handlePageChange = (page: number) => {
  current.value = page
  fetchPosts()
}

const goDetail = (id: number) => {
  router.push(`/posts/${id}`)
}

onMounted(fetchPosts)
</script>

<template>
  <div class="cinematic-page post-list-page">
    <header class="post-shell glass-chip reveal-up">
      <RouterLink to="/" class="post-shell__brand heading-serif">w</RouterLink>
      <nav class="post-shell__nav">
        <RouterLink to="/">首页</RouterLink>
        <RouterLink to="/posts" class="is-active">博客</RouterLink>
        <a href="#">分类</a>
        <a href="#">标签</a>
        <a href="#">关于</a>
      </nav>
      <div class="post-shell__actions">
        <el-input v-model="keyword" class="post-search" placeholder="搜索文章..." clearable @keyup.enter="handleSearch">
          <template #suffix>
            <button type="button" class="post-search__button" @click="handleSearch">⌕</button>
          </template>
        </el-input>
        <button type="button" class="glass-button--ghost post-shell__ghost">Admin</button>
      </div>
    </header>

    <main class="post-list-layout">
      <section class="post-list-hero glass-panel reveal-up delay-2">
        <div class="post-list-hero__content">
          <p class="eyebrow">文章列表</p>
          <h1 class="heading-serif">记录思考，分享知识</h1>
          <p class="post-list-hero__desc">深色玻璃拟态风格，保留博客氛围和信息层级，突出封面、摘要、作者与阅读数据。</p>
          <div class="post-list-hero__stats">
            <div><strong>{{ total }}</strong><span>文章总数</span></div>
            <div><strong>{{ posts.length }}</strong><span>当前页</span></div>
            <div><strong>{{ formatReading(posts[0]?.readTimeMinutes) }}</strong><span>推荐阅读</span></div>
          </div>
        </div>
        <div class="post-list-hero__art"></div>
      </section>

      <section class="post-list-body">
        <div v-loading="loading" class="post-list-feed" element-loading-text="加载中...">
          <article
            v-for="post in posts"
            :key="post.id"
            class="glass-panel post-card reveal-up"
            @click="goDetail(post.id)"
          >
            <div class="post-card__cover" :style="{ backgroundImage: post.coverImage ? `url(${post.coverImage})` : '' }">
              <span class="glass-chip post-card__category">{{ post.categoryName || '未分类' }}</span>
            </div>
            <div class="post-card__content">
              <div class="post-card__topline">
                <span>{{ post.authorName || 'Wintina' }}</span>
                <span>{{ formatDate(post.publishTime || post.createTime) }}</span>
              </div>
              <h2>{{ post.title }}</h2>
              <p>{{ post.summary || '暂无摘要，点击查看完整文章内容。' }}</p>
              <div class="post-card__meta">
                <span>👁 {{ post.viewCount || 0 }}</span>
                <span>💬 {{ post.commentCount || 0 }}</span>
                <span>⏱ {{ formatReading(post.readTimeMinutes) }}</span>
              </div>
              <div class="post-card__tags" v-if="post.tagNames?.length">
                <span v-for="tag in post.tagNames" :key="tag" class="glass-chip">{{ tag }}</span>
              </div>
            </div>
          </article>

          <div v-if="!loading && !hasData" class="glass-panel post-empty">暂无文章</div>
        </div>

        <aside class="post-list-aside">
          <section class="glass-panel post-side-card reveal-up delay-2">
            <h3>关于我</h3>
            <div class="post-author">
              <div class="post-author__avatar"></div>
              <div>
                <strong>Wintina</strong>
                <p>热爱技术，记录生活，分享思考。</p>
              </div>
            </div>
            <div class="post-author__stats">
              <div><strong>86</strong><span>文章</span></div>
              <div><strong>128</strong><span>关注</span></div>
              <div><strong>2.3k</strong><span>粉丝</span></div>
            </div>
            <button type="button" class="glass-button post-side-card__button">关注我</button>
          </section>

          <section class="glass-panel post-side-card reveal-up delay-3">
            <h3>热门文章</h3>
            <ul class="post-side-list">
              <li v-for="post in posts.slice(0, 5)" :key="post.id" @click="goDetail(post.id)">
                <span class="post-side-list__thumb" :style="{ backgroundImage: post.coverImage ? `url(${post.coverImage})` : '' }"></span>
                <div>
                  <strong>{{ post.title }}</strong>
                  <p>{{ post.viewCount || 0 }} 阅读</p>
                </div>
              </li>
            </ul>
          </section>

          <section class="glass-panel post-side-card reveal-up delay-4">
            <h3>标签云</h3>
            <div class="post-tag-cloud">
              <span v-for="tag in posts.flatMap((item) => item.tagNames || []).slice(0, 10)" :key="tag" class="glass-chip">{{ tag }}</span>
            </div>
          </section>
        </aside>
      </section>

      <section class="post-list-pagination" v-if="total > size">
        <el-pagination
          background
          layout="prev, pager, next"
          :current-page="current"
          :page-size="size"
          :total="total"
          @current-change="handlePageChange"
        />
      </section>
    </main>
  </div>
</template>

<style scoped lang="scss">
.post-list-page {
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

.post-shell__actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.post-search {
  width: 320px;
}

.post-search__button {
  border: 0;
  background: transparent;
  color: var(--text-secondary);
  cursor: pointer;
}

.post-list-layout {
  width: min(1280px, calc(100% - 24px));
  margin: 18px auto 0;
}

.post-list-hero {
  padding: 28px;
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 24px;
  align-items: center;
}

.post-list-hero h1 {
  margin: 12px 0 0;
  font-size: clamp(2.4rem, 6vw, 4.5rem);
  line-height: 0.95;
}

.post-list-hero__desc {
  margin: 16px 0 0;
  color: var(--text-secondary);
  max-width: 52ch;
  line-height: 1.8;
}

.post-list-hero__stats {
  display: flex;
  gap: 20px;
  margin-top: 22px;
}

.post-list-hero__stats div {
  min-width: 92px;
}

.post-list-hero__stats strong {
  display: block;
  font-size: 1.6rem;
}

.post-list-hero__stats span {
  color: var(--text-muted);
  font-size: 0.9rem;
}

.post-list-hero__art {
  min-height: 240px;
  border-radius: 28px;
  background:
    radial-gradient(circle at 70% 30%, rgba(255, 200, 220, 0.32), transparent 28%),
    radial-gradient(circle at 75% 60%, rgba(255, 255, 255, 0.18), transparent 24%),
    linear-gradient(135deg, rgba(255, 255, 255, 0.08), rgba(255, 255, 255, 0.02));
  box-shadow: inset 0 1px 1px rgba(255, 255, 255, 0.12);
}

.post-list-body {
  margin-top: 18px;
  display: grid;
  grid-template-columns: minmax(0, 1fr) 340px;
  gap: 18px;
}

.post-list-feed {
  min-height: 400px;
  display: grid;
  gap: 16px;
}

.post-card {
  display: grid;
  grid-template-columns: 240px 1fr;
  overflow: hidden;
  cursor: pointer;
}

.post-card__cover {
  min-height: 190px;
  background: linear-gradient(135deg, rgba(255, 193, 205, 0.38), rgba(255, 255, 255, 0.08));
  background-size: cover;
  background-position: center;
  padding: 14px;
}

.post-card__category {
  display: inline-flex;
}

.post-card__content {
  padding: 18px 18px 16px;
}

.post-card__topline {
  display: flex;
  justify-content: space-between;
  color: var(--text-muted);
  font-size: 0.9rem;
}

.post-card h2 {
  margin: 12px 0 0;
  font-size: 1.7rem;
  line-height: 1.05;
}

.post-card p {
  margin: 10px 0 0;
  color: var(--text-secondary);
  line-height: 1.7;
}

.post-card__meta {
  margin-top: 14px;
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  color: var(--text-muted);
}

.post-card__tags {
  margin-top: 14px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.post-list-aside {
  display: grid;
  gap: 16px;
}

.post-side-card {
  padding: 20px;
}

.post-side-card h3 {
  margin: 0 0 18px;
  font-size: 1.2rem;
}

.post-author {
  display: flex;
  gap: 14px;
  align-items: center;
}

.post-author__avatar {
  width: 74px;
  height: 74px;
  border-radius: 50%;
  background: radial-gradient(circle at 30% 30%, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0.08));
}

.post-author p {
  margin: 8px 0 0;
  color: var(--text-secondary);
}

.post-author__stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  margin-top: 18px;
}

.post-author__stats div {
  text-align: center;
  padding: 12px 8px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.06);
}

.post-author__stats strong {
  display: block;
  font-size: 1.2rem;
}

.post-author__stats span {
  color: var(--text-muted);
  font-size: 0.82rem;
}

.post-side-card__button {
  width: 100%;
  margin-top: 16px;
}

.post-side-list {
  margin: 0;
  padding: 0;
  list-style: none;
  display: grid;
  gap: 12px;
}

.post-side-list li {
  display: grid;
  grid-template-columns: 72px 1fr;
  gap: 12px;
  align-items: center;
  cursor: pointer;
}

.post-side-list__thumb {
  width: 72px;
  height: 54px;
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(255, 193, 205, 0.38), rgba(255, 255, 255, 0.08));
  background-size: cover;
  background-position: center;
}

.post-side-list strong {
  display: block;
}

.post-side-list p {
  margin: 4px 0 0;
  color: var(--text-muted);
  font-size: 0.85rem;
}

.post-tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.post-empty {
  padding: 40px;
  text-align: center;
}

.post-list-pagination {
  display: flex;
  justify-content: center;
  margin: 26px 0 20px;
}

@media (max-width: 1120px) {
  .post-list-body {
    grid-template-columns: 1fr;
  }

  .post-list-aside {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 860px) {
  .post-shell {
    grid-template-columns: auto 1fr;
  }

  .post-shell__nav,
  .post-shell__ghost {
    display: none;
  }

  .post-list-hero {
    grid-template-columns: 1fr;
  }

  .post-card {
    grid-template-columns: 1fr;
  }

  .post-list-aside {
    grid-template-columns: 1fr;
  }
}
</style>
