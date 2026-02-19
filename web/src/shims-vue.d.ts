// 声明所有.vue后缀的文件为Vue组件模块
declare module '*.vue' {
  // 导入Vue3的组件核心类型
  import type { DefineComponent } from 'vue'
  // 定义组件类型：无props/无emits/无自定义选项的基础Vue组件
  const component: DefineComponent<{}, {}, any>
  // 导出组件，让TS能识别
  export default component
}
