<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { postApi } from '@/api/post'
import type { PostDetail, PostEditorOptions, SavePostPayload } from '@/types/post'
import { renderMarkdownToHtml, sanitizeHtml } from '@/utils/markdown'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const submitting = ref(false)
const post = ref<PostDetail | null>(null)
const options = ref<PostEditorOptions>({
  categories: [],
  tags: [],
  statusOptions: [],
})

const form = ref<SavePostPayload>({
  title: '',
  summary: '',
  content: '',
  contentHtml: '',
  categoryId: 1,
  status: 1,
  coverImage: '',
  tagIds: [],
  allowComment: 1,
  isTop: 0,
  isRecommend: 0,
  sortOrder: 0,
  seoKeywords: '',
  seoDescription: '',
})

const postId = computed(() => Number(route.params.id))
const isEditMode = computed(() => route.name === 'PostEdit')
const hasValidPostId = computed(() => Number.isInteger(postId.value) && postId.value > 0)
const wordCount = computed(() => form.value.content.replace(/\s+/g, '').length)

const syncHtml = () => {
  form.value.contentHtml = sanitizeHtml(renderMarkdownToHtml(form.value.content))
}

const loadOptions = async () => {
  try {
    const data = await postApi.getPostEditorOptions()
    options.value = data
    if (!form.value.categoryId && data.categories.length) {
      form.value.categoryId = data.categories[0]!.id
    }
    if (!data.statusOptions.some((item) => item.value === form.value.status) && data.statusOptions.length) {
      form.value.status = data.statusOptions[0]!.value
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('获取编辑器配置失败')
  }
}

const loadPost = async () => {
  if (!isEditMode.value) return
  if (!hasValidPostId.value) {
    ElMessage.error('文章参数错误')
    router.replace('/posts')
    return
  }

  loading.value = true
  try {
    const detail = await postApi.getPostDetail(postId.value)
    post.value = detail
    form.value = {
      title: detail.title,
      summary: detail.summary || '',
      content: detail.content || '',
      contentHtml: detail.contentHtml || '',
      categoryId: detail.categoryId || options.value.categories[0]?.id || 1,
      status: detail.status || options.value.statusOptions[0]?.value || 1,
      slug: detail.slug || '',
      coverImage: detail.coverImage || '',
      tagIds: detail.tagIds || [],
      allowComment: detail.allowComment ?? 1,
      isTop: detail.isTop ?? 0,
      isRecommend: detail.isRecommend ?? 0,
      sortOrder: detail.sortOrder ?? 0,
      seoKeywords: detail.seoKeywords || '',
      seoDescription: detail.seoDescription || '',
    }
  } catch (error) {
    console.error(error)
    ElMessage.error('获取文章失败')
    router.replace('/posts')
  } finally {
    loading.value = false
  }
}

const submit = async () => {
  if (!form.value.title.trim()) {
    ElMessage.warning('请输入标题')
    return
  }
  if (!form.value.content.trim()) {
    ElMessage.warning('请输入正文')
    return
  }

  submitting.value = true
  try {
    syncHtml()
    const payload: SavePostPayload = {
      ...form.value,
      title: form.value.title.trim(),
      summary: form.value.summary?.trim(),
      content: form.value.content,
      contentHtml: form.value.contentHtml,
      seoKeywords: form.value.seoKeywords?.trim(),
      seoDescription: form.value.seoDescription?.trim(),
    }

    if (isEditMode.value) {
      await postApi.updatePost(postId.value, payload)
      ElMessage.success('更新成功')
      router.push(`/posts/${postId.value}`)
      return
    }

    const id = await postApi.createPost(payload)
    ElMessage.success('发布成功')
    router.push(`/posts/${id}`)
  } catch (error: unknown) {
    console.error(error)
    const message = error instanceof Error ? error.message : ''
    ElMessage.error(message || (isEditMode.value ? '更新失败' : '发布失败'))
  } finally {
    submitting.value = false
  }
}

watch(() => form.value.content, syncHtml)

onMounted(async () => {
  syncHtml()
  await loadOptions()
  if (isEditMode.value) {
    await loadPost()
  }
})
</script>

<template>
  <div class="cinematic-page post-editor-page" v-loading="loading" element-loading-text="加载中...">
    <header class="post-shell glass-chip reveal-up">
      <div class="post-shell__left">
        <RouterLink to="/" class="post-shell__brand heading-serif">w</RouterLink>
        <span>博客 / {{ isEditMode ? '文章编辑' : '发布文章' }}</span>
      </div>
      <div class="post-shell__actions">
        <button type="button" class="glass-button--ghost" @click="router.push('/posts')">返回列表</button>
        <button type="button" class="glass-button--ghost" :disabled="submitting" @click="submit">保存草稿</button>
        <button type="button" class="glass-button" :disabled="submitting" @click="submit">{{ submitting ? '发布中...' : '发布文章' }}</button>
      </div>
    </header>

    <main class="post-editor-layout">
      <section class="glass-panel post-editor-main reveal-up delay-2">
        <el-input v-model="form.title" class="post-editor-title" placeholder="请输入文章标题" />
        <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="一句话摘要（可选）" />

        <MdEditor v-model="form.content" :preview-only="false" class="post-editor-md" />

        <footer class="post-editor-footer">
          <span>字数：{{ wordCount }}</span>
          <span>预计阅读：{{ Math.max(1, Math.ceil(wordCount / 320)) }} 分钟</span>
          <span>{{ isEditMode ? '编辑模式' : '新建模式' }}</span>
        </footer>
      </section>

      <aside class="post-editor-side">
        <section class="glass-panel post-side-card reveal-up delay-2">
          <h3>文章设置</h3>
          <el-form label-position="top">
            <el-form-item label="文章分类">
              <el-select v-model="form.categoryId" style="width: 100%">
                <el-option v-for="item in options.categories" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>

            <el-form-item label="文章标签">
              <el-select v-model="form.tagIds" multiple collapse-tags collapse-tags-tooltip style="width: 100%">
                <el-option v-for="item in options.tags" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>

            <el-form-item label="文章摘要">
              <el-input v-model="form.summary" type="textarea" :rows="3" maxlength="200" show-word-limit />
            </el-form-item>

            <el-form-item label="封面图片 URL">
              <el-input v-model="form.coverImage" placeholder="https://..." />
            </el-form-item>
          </el-form>
        </section>

        <section class="glass-panel post-side-card reveal-up delay-3">
          <h3>发布设置</h3>
          <el-form label-position="top">
            <el-form-item label="文章状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="item in options.statusOptions" :key="item.value" :value="item.value">{{ item.label }}</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item>
              <el-switch v-model="form.allowComment" :active-value="1" :inactive-value="0" active-text="允许评论" />
            </el-form-item>
            <el-form-item>
              <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" active-text="置顶文章" />
            </el-form-item>
            <el-form-item>
              <el-switch v-model="form.isRecommend" :active-value="1" :inactive-value="0" active-text="推荐文章" />
            </el-form-item>
          </el-form>
        </section>

        <section class="glass-panel post-side-card reveal-up delay-4">
          <h3>更多设置</h3>
          <el-form label-position="top">
            <el-form-item label="SEO 关键词">
              <el-input v-model="form.seoKeywords" placeholder="用逗号分隔" />
            </el-form-item>
            <el-form-item label="SEO 描述">
              <el-input v-model="form.seoDescription" type="textarea" :rows="3" />
            </el-form-item>
            <el-form-item label="排序优先级">
              <el-input-number v-model="form.sortOrder" :step="1" :min="0" style="width: 100%" />
            </el-form-item>
          </el-form>
        </section>
      </aside>
    </main>
  </div>
</template>

<style scoped lang="scss">
.post-editor-page {
  min-height: 100vh;
  padding: 20px;
}

.post-shell {
  width: min(1380px, calc(100% - 24px));
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  padding: 12px 16px;
  position: sticky;
  top: 16px;
  z-index: 20;
}

.post-shell__left {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--text-secondary);
}

.post-shell__brand {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: grid;
  place-items: center;
  background: rgba(255, 255, 255, 0.08);
  font-size: 1.7rem;
}

.post-shell__actions {
  display: flex;
  gap: 10px;
}

.post-editor-layout {
  width: min(1380px, calc(100% - 24px));
  margin: 18px auto 0;
  display: grid;
  grid-template-columns: minmax(0, 1fr) 360px;
  gap: 18px;
}

.post-editor-main {
  padding: 18px;
}

.post-editor-title {
  margin-bottom: 14px;
}

.post-editor-md {
  margin-top: 14px;
}

:deep(.md-editor) {
  min-height: 720px;
}

.post-editor-footer {
  margin-top: 12px;
  display: flex;
  gap: 18px;
  color: var(--text-muted);
}

.post-editor-side {
  display: grid;
  gap: 16px;
  align-content: start;
}

.post-side-card {
  padding: 16px;
}

.post-side-card h3 {
  margin: 0 0 14px;
}

@media (max-width: 1080px) {
  .post-editor-layout {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .post-shell {
    flex-direction: column;
    align-items: stretch;
  }

  .post-shell__actions {
    width: 100%;
  }

  .post-shell__actions button {
    flex: 1;
  }
}
</style>
