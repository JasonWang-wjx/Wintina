<template>
  <div class="home-root">
    <!-- 动态背景 Blob -->
    <div class="bg-blobs">
      <div class="blob blob-1"></div>
      <div class="blob blob-2"></div>
      <div class="blob blob-3"></div>
    </div>

    <!-- 噪音贴图层 -->
    <svg class="noise-layer" viewBox="0 0 512 512">
      <filter id="noise">
        <feTurbulence
          type="fractalNoise"
          baseFrequency="0.65"
          numOctaves="3"
          stitchTiles="stitch"
        />
        <feColorMatrix type="saturate" values="0" />
      </filter>
      <rect width="100%" height="100%" filter="url(#noise)" opacity="0.035" />
    </svg>

    <!-- 主容器 -->
    <div class="home-container">
      <!-- Logo / Brand -->
      <div class="brand">
        <div class="brand-icon">
          <svg width="36" height="36" viewBox="0 0 36 36" fill="none">
            <defs>
              <linearGradient id="iconGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stop-color="#fff" stop-opacity="0.9" />
                <stop offset="100%" stop-color="#fff" stop-opacity="0.4" />
              </linearGradient>
            </defs>
            <circle cx="18" cy="18" r="17" stroke="url(#iconGrad)" stroke-width="1.5" fill="none" />
            <circle cx="18" cy="18" r="8" fill="url(#iconGrad)" />
            <circle cx="18" cy="18" r="3" fill="#fff" fill-opacity="0.6" />
          </svg>
        </div>
        <span class="brand-name">Glassify</span>
      </div>

      <!-- 欢迎信息 -->
      <div class="welcome-section">
        <h1 class="welcome-title">Welcome to Glassify</h1>
        <p class="welcome-subtitle">A modern blogging platform with AI-powered recommendations</p>
        <button class="get-started-btn" @click="navigateToLogin">Get Started</button>
      </div>

      <!-- 功能卡片 -->
      <div class="features-section">
        <div class="feature-card">
          <div class="feature-icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
              <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
            </svg>
          </div>
          <h3 class="feature-title">AI Recommendations</h3>
          <p class="feature-description">
            Get personalized content suggestions based on your interests
          </p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <circle cx="12" cy="12" r="10" />
              <path d="M8 14s1.5 2 4 2 4-2 4-2" />
              <line x1="9" y1="9" x2="9.01" y2="9" />
              <line x1="15" y1="9" x2="15.01" y2="9" />
            </svg>
          </div>
          <h3 class="feature-title">User Friendly</h3>
          <p class="feature-description">
            Intuitive interface with smooth animations and transitions
          </p>
        </div>

        <div class="feature-card">
          <div class="feature-icon">
            <svg
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
              <polyline points="22 4 12 14.01 9 11.01" />
            </svg>
          </div>
          <h3 class="feature-title">Fast Performance</h3>
          <p class="feature-description">Optimized for speed and responsiveness</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

function navigateToLogin() {
  router.push('/login')
}
</script>

<style lang="scss" scoped>
// ═══════════════════════════════════════════════
//  Variables
// ═══════════════════════════════════════════════

// 颜色
$clr-bg: #0c0e1a;
$clr-text: rgba(255, 255, 255, 0.92);
$clr-text-dim: rgba(255, 255, 255, 0.45);
$clr-accent: #a78bfa; // 紫色亮调
$clr-accent-deep: #7c3aed; // 紫色深调
$clr-accent-mid: #6d28d9;
$clr-accent-blue: #4f46e5;
$clr-accent-light: #c4b5fd; // hover 亮紫

// Blob 颜色 map
$blob-map: (
  1: (
    color: #7c3aed,
    size: 500px,
    top: -150px,
    left: -100px,
    duration: 16s,
    delay: 0s,
    opacity: 0.55,
  ),
  2: (
    color: #06b6d4,
    size: 400px,
    top: 50%,
    right: -100px,
    duration: 18s,
    delay: -4s,
    opacity: 0.45,
  ),
  3: (
    color: #ec4899,
    size: 350px,
    bottom: -100px,
    left: 20%,
    duration: 20s,
    delay: -8s,
    opacity: 0.4,
  ),
);

// 尺寸
$radius-card: 20px;

// 断点
$bp-mobile: 768px;

// ═══════════════════════════════════════════════
//  Mixins
// ═══════════════════════════════════════════════

// 毛玻璃背景
@mixin glass(
  $blur: 32px,
  $saturate: 1.4,
  $bg-opacity-start: 0.1,
  $bg-opacity-mid: 0.04,
  $bg-opacity-end: 0.07
) {
  background: linear-gradient(
    145deg,
    rgba(255, 255, 255, $bg-opacity-start) 0%,
    rgba(255, 255, 255, $bg-opacity-mid) 60%,
    rgba(255, 255, 255, $bg-opacity-end) 100%
  );
  backdrop-filter: blur($blur) saturate($saturate);
  -webkit-backdrop-filter: blur($blur) saturate($saturate);
}

// 玻璃边框 + 阴影
@mixin glass-border($border-opacity: 0.15, $shadow-opacity: 0.25, $highlight-opacity: 0.18) {
  border: 1px solid rgba(255, 255, 255, $border-opacity);
  box-shadow:
    0 8px 32px rgba(0, 0, 0, $shadow-opacity),
    inset 0 1px 0 rgba(255, 255, 255, $highlight-opacity);
}

// ═══════════════════════════════════════════════
//  Root
// ═══════════════════════════════════════════════

.home-root {
  position: relative;
  min-height: 100vh;
  background: $clr-bg;
  overflow: hidden;
  font-family: 'Inter', 'SF Pro Display', system-ui, sans-serif;
  color: $clr-text;
  -webkit-font-smoothing: antialiased;
}

// ═══════════════════════════════════════════════
//  Background Blobs
// ═══════════════════════════════════════════════

.bg-blobs {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;
}

.blob {
  position: absolute;
  border-radius: 50%;
  filter: blur(90px);
  opacity: 0.55;
  animation: blobFloat 14s ease-in-out infinite alternate;
}

// 用 map 循环生成 blob
@each $index, $props in $blob-map {
  .blob-#{$index} {
    width: map-get($props, size);
    height: map-get($props, size);
    background: map-get($props, color);
    opacity: map-get($props, opacity);
    animation-duration: map-get($props, duration);
    animation-delay: map-get($props, delay);

    @if map-has-key($props, top) {
      top: map-get($props, top);
    }
    @if map-has-key($props, bottom) {
      bottom: map-get($props, bottom);
    }
    @if map-has-key($props, left) {
      left: map-get($props, left);
    }
    @if map-has-key($props, right) {
      right: map-get($props, right);
    }
  }
}

@keyframes blobFloat {
  0% {
    transform: translate(0, 0) scale(1);
  }
  33% {
    transform: translate(40px, -30px) scale(1.08);
  }
  66% {
    transform: translate(-30px, 25px) scale(0.95);
  }
  100% {
    transform: translate(20px, -10px) scale(1.04);
  }
}

// ═══════════════════════════════════════════════
//  Noise Overlay
// ═══════════════════════════════════════════════

.noise-layer {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
  mix-blend-mode: overlay;
}

// ═══════════════════════════════════════════════
//  Main Container
// ═══════════════════════════════════════════════

.home-container {
  position: relative;
  z-index: 2;
  padding: 40px;
  max-width: 1200px;
  margin: 0 auto;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  gap: 80px;
}

// ═══════════════════════════════════════════════
//  Brand
// ═══════════════════════════════════════════════

.brand {
  display: flex;
  align-items: center;
  gap: 10px;

  &-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 48px;
    height: 48px;
    border-radius: 16px;
    border: 1px solid rgba(255, 255, 255, 0.12);
    background: rgba(255, 255, 255, 0.06);
    backdrop-filter: blur(12px);
  }

  &-name {
    font-size: 22px;
    font-weight: 600;
    letter-spacing: -0.5px;
    background: linear-gradient(135deg, #fff 30%, rgba(255, 255, 255, 0.55));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}

// ═══════════════════════════════════════════════
//  Welcome Section
// ═══════════════════════════════════════════════

.welcome-section {
  text-align: center;
  margin-top: 60px;

  &-title {
    font-size: 48px;
    font-weight: 700;
    letter-spacing: -1px;
    margin-bottom: 16px;
    background: linear-gradient(135deg, #fff 30%, rgba(255, 255, 255, 0.7));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  &-subtitle {
    font-size: 18px;
    color: $clr-text-dim;
    margin-bottom: 32px;
  }
}

.get-started-btn {
  position: relative;
  width: 180px;
  height: 52px;
  border-radius: 14px;
  border: none;
  cursor: pointer;
  font-size: 15px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 0.3px;
  overflow: hidden;
  background: linear-gradient(
    135deg,
    $clr-accent-deep 0%,
    $clr-accent-mid 50%,
    $clr-accent-blue 100%
  );
  box-shadow:
    0 4px 18px rgba(124, 58, 237, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.15);
  transition:
    transform 0.18s,
    box-shadow 0.18s;

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(
      90deg,
      transparent 0%,
      rgba(255, 255, 255, 0.12) 50%,
      transparent 100%
    );
    background-size: 200% 100%;
    animation: shimmer 2.4s linear infinite;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow:
      0 6px 24px rgba(124, 58, 237, 0.5),
      inset 0 1px 0 rgba(255, 255, 255, 0.15);
  }

  &:active {
    transform: translateY(0);
  }
}

@keyframes shimmer {
  from {
    background-position: -200% 0;
  }
  to {
    background-position: 200% 0;
  }
}

// ═══════════════════════════════════════════════
//  Features Section
// ═══════════════════════════════════════════════

.features-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
  margin-top: 40px;
}

.feature-card {
  padding: 32px;
  border-radius: $radius-card;
  @include glass;
  @include glass-border;
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-5px);
  }

  .feature-icon {
    width: 56px;
    height: 56px;
    border-radius: 16px;
    background: rgba(167, 139, 250, 0.15);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 20px;
    color: $clr-accent;
  }

  .feature-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 12px;
  }

  .feature-description {
    font-size: 14px;
    color: $clr-text-dim;
    line-height: 1.5;
  }
}

// ═══════════════════════════════════════════════
//  Responsive
// ═══════════════════════════════════════════════

@media (max-width: $bp-mobile) {
  .home-container {
    padding: 24px;
    gap: 60px;
  }

  .welcome-section {
    margin-top: 40px;

    &-title {
      font-size: 36px;
    }

    &-subtitle {
      font-size: 16px;
    }
  }

  .features-section {
    grid-template-columns: 1fr;
  }

  .feature-card {
    padding: 24px;
  }
}
</style>
