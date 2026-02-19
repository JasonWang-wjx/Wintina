<template>
  <div class="login-root">
    <div class="login-container">
      <div class="glass-card">
        <h1 class="card-title">Welcome back</h1>
        <p class="card-subtitle">Sign in to continue your journey</p>

        <form class="login-form" @submit.prevent="handleSubmit">
          <div class="input-group">
            <label class="input-label">Email</label>
            <div class="input-wrapper">
              <input
                type="email"
                v-model="form.email"
                placeholder="you@example.com"
                autocomplete="email"
                @focus="focusedField = 'email'"
                @blur="handleEmailBlur"
              />
            </div>
            <span class="error-msg" v-if="errors.email">{{ errors.email }}</span>
          </div>

          <div class="input-group">
            <label class="input-label">Password</label>
            <div class="input-wrapper">
              <input
                :type="showPassword ? 'text' : 'password'"
                v-model="form.password"
                placeholder="••••••••"
                autocomplete="current-password"
                @focus="focusedField = 'password'"
                @blur="handlePasswordBlur"
              />
              <button
                type="button"
                class="eye-btn"
                @click="showPassword = !showPassword"
                aria-label="Toggle visibility"
              >
                {{ showPassword ? 'Hide' : 'Show' }}
              </button>
            </div>
            <span class="error-msg" v-if="errors.password">{{ errors.password }}</span>
          </div>

          <div class="form-options">
            <label class="remember-label">
              <input type="checkbox" v-model="form.remember" class="remember-cb" />
              Remember me
            </label>
            <a href="#" class="forgot-link" @click.prevent>Forgot password?</a>
          </div>

          <button type="submit" class="submit-btn" :class="{ loading: isLoading }">
            {{ isLoading ? 'Signing in…' : 'Sign In' }}
          </button>
        </form>

        <p class="signup-hint">
          Don't have an account? <a href="#" class="signup-link" @click.prevent>Sign up</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

// ─── 状态 ────────────────────────────────────
const form = reactive({
  email: '',
  password: '',
  remember: false,
})

const errors = reactive<Record<string, string>>({
  email: '',
  password: '',
})

const focusedField = ref<string>('')
const showPassword = ref(false)
const isLoading = ref(false)

// ─── 事件处理 ──────────────────────────────────
function handleEmailBlur() {
  focusedField.value = ''
  validateEmail()
}

function handlePasswordBlur() {
  focusedField.value = ''
  validatePassword()
}

// ─── 验证 ────────────────────────────────────
function validateEmail() {
  if (!form.email) {
    errors.email = 'Email is required'
  } else if (!/^[^@]+@[^@]+\.[^@]+$/.test(form.email)) {
    errors.email = 'Please enter a valid email'
  } else {
    errors.email = ''
  }
}

function validatePassword() {
  if (!form.password) {
    errors.password = 'Password is required'
  } else if (form.password.length < 6) {
    errors.password = 'Password must be at least 6 characters'
  } else {
    errors.password = ''
  }
}

// ─── 提交 ────────────────────────────────────
async function handleSubmit() {
  validateEmail()
  validatePassword()
  if (errors.email || errors.password) return

  isLoading.value = true
  // 模拟异步请求
  await new Promise((resolve) => setTimeout(resolve, 2000))
  isLoading.value = false
  alert('Login successful! 🎉')
}
</script>

<style lang="scss" scoped>
.login-root {
  position: fixed;
  inset: 0;
  min-height: 100vh;
  background: #0c0e1a;
  overflow: hidden;
  font-family: 'Inter', 'SF Pro Display', system-ui, sans-serif;
  color: rgba(255, 255, 255, 0.92);
  -webkit-font-smoothing: antialiased;
}

.login-container {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 24px;
}

.glass-card {
  position: relative;
  width: 100%;
  max-width: 400px;
  padding: 40px 36px 32px;
  border-radius: 28px;
  background: linear-gradient(
    145deg,
    rgba(255, 255, 255, 0.1) 0%,
    rgba(255, 255, 255, 0.04) 60%,
    rgba(255, 255, 255, 0.07) 100%
  );
  backdrop-filter: blur(32px) saturate(1.4);
  -webkit-backdrop-filter: blur(32px) saturate(1.4);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow:
    0 8px 32px rgba(0, 0, 0, 0.25),
    inset 0 1px 0 rgba(255, 255, 255, 0.18);
}

.card-title {
  font-size: 26px;
  font-weight: 700;
  letter-spacing: -0.8px;
  margin: 0 0 6px;
  text-align: center;
}

.card-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.45);
  text-align: center;
  margin: 0 0 24px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.input-label {
  font-size: 13px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.45);
  letter-spacing: 0.2px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  height: 48px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.1);
  overflow: hidden;
}

.input-wrapper input {
  flex: 1;
  height: 100%;
  background: transparent;
  border: none;
  outline: none;
  color: rgba(255, 255, 255, 0.92);
  font-size: 14.5px;
  font-weight: 450;
  padding: 0 14px;
  caret-color: #a78bfa;
}

.input-wrapper input::placeholder {
  color: rgba(255, 255, 255, 0.28);
}

.eye-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 100%;
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.45);
  cursor: pointer;
  font-size: 12px;
}

.eye-btn:hover {
  color: rgba(255, 255, 255, 0.7);
}

.error-msg {
  font-size: 12px;
  color: #f87171;
  padding-left: 4px;
}

.form-options {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: -6px;
}

.remember-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.45);
  cursor: pointer;
}

.remember-label:hover {
  color: rgba(255, 255, 255, 0.92);
}

.forgot-link {
  font-size: 13px;
  font-weight: 600;
  color: #a78bfa;
  text-decoration: none;
}

.forgot-link:hover {
  color: #c4b5fd;
  text-shadow: 0 0 10px rgba(167, 139, 250, 0.4);
}

.submit-btn {
  position: relative;
  width: 100%;
  height: 50px;
  margin-top: 4px;
  border-radius: 14px;
  border: none;
  cursor: pointer;
  font-size: 15px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 0.3px;
  overflow: hidden;
  background: linear-gradient(135deg, #7c3aed 0%, #6d28d9 50%, #4f46e5 100%);
  box-shadow:
    0 4px 18px rgba(124, 58, 237, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.15);
  transition:
    transform 0.18s,
    box-shadow 0.18s;
}

.submit-btn:hover {
  transform: translateY(-1px);
  box-shadow:
    0 6px 24px rgba(124, 58, 237, 0.5),
    inset 0 1px 0 rgba(255, 255, 255, 0.15);
}

.submit-btn:active {
  transform: translateY(0);
}

.submit-btn.loading {
  opacity: 0.8;
  cursor: not-allowed;
}

.signup-hint {
  text-align: center;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.45);
  margin: 24px 0 0;
}

.signup-link {
  font-weight: 600;
  color: #a78bfa;
  text-decoration: none;
}

.signup-link:hover {
  color: #c4b5fd;
  text-shadow: 0 0 10px rgba(167, 139, 250, 0.4);
}
</style>
