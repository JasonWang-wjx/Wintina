# CLAUDE.md

本文档为 Claude Code (claude.ai/code) 在本仓库中工作时提供说明。

## 仓库结构

这是一个前后端分离项目：
- `backend/`: 基于 Java 21 的 Spring Boot 3.5 应用
- `web/`: 基于 Vue 3 + TypeScript + Vite 的前端
- `mobile/`、`docker/`、`docs/`: 这些目录存在于仓库中，但不属于当前 web/backend 请求流程的一部分

## 开发命令

### 后端 (`backend/`)

- 安装/构建并运行测试：
  - `mvn test`
- 启动 Spring Boot 应用：
  - `mvn spring-boot:run`
- 构建后端 jar：
  - `mvn package`
- 运行单个测试类：
  - `mvn -Dtest=BackendApplicationTests test`
- 运行单个测试方法：
  - `mvn -Dtest=BackendApplicationTests#contextLoads test`

### 前端 (`web/`)

- 安装依赖：
  - `npm install`
- 启动 Vite 开发服务器：
  - `npm run dev`
- 生产构建：
  - `npm run build`
- 仅做类型检查：
  - `npm run type-check`
- 运行单元测试：
  - `npm run test:unit`
- 运行单个 Vitest 文件：
  - `npm run test:unit -- src/components/Foo.spec.ts`
- 按名称匹配运行测试：
  - `npm run test:unit -- -t "test name"`
- 预览生产构建结果：
  - `npm run preview`
- 格式化前端源码：
  - `npm run format`

## 架构

### 后端

后端位于 `backend/src/main/java/com/wintina/blog`，采用分层 Spring Boot 架构。

- 入口类：`BackendApplication.java`
- HTTP 层：`controller/`
- 业务逻辑层：`service/` 和 `service/impl/`
- 持久层：`mapper/` 中的 MyBatis-Plus Mapper，以及 `entity/`
- API 契约：请求使用 `dto/`，响应使用 `vo/`
- 横切基础设施：`security/`、`interceptor/`、`config/`、`common/`

持久层使用的是 MyBatis-Plus，而不是 Spring Data Repository，尽管 `pom.xml` 中存在 JPA 依赖。实体类遵循 MyBatis-Plus 约定，例如使用 `application.properties` 中配置的逻辑删除。

API 响应统一通过 `common/Result.java` 封装，异常由全局异常处理器集中转换。前端代码假定后端返回的是基于响应体的成功约定，即 `code == 200`，而不仅仅是 HTTP 2xx。

### 后端认证流程

认证逻辑主要集中在 `controller/auth/AuthController.java` 和 `service/impl/UserServiceImpl.java` 中的 `/api/auth/**` 路径下。

- `POST /api/auth/register`: 校验用户名是否重复后创建用户，并使用 bcrypt 对密码进行哈希
- `POST /api/auth/login`: 校验凭据，返回用户资料和 JWT
- `PUT /api/auth/password`: 需要已认证用户上下文，并更新数据库中的 bcrypt 密码哈希

当前并行存在两层认证校验：
- `security/SecurityConfig.java` 中 Spring Security 过滤器链里的 `JwtAuthenticationFilter`
- `interceptor/WebMvcConfig.java` 中 MVC 拦截器注册的 `JwtInterceptor`

修改认证行为时，要同时检查这两层。当前公共 API 访问规则在两个地方都定义了一份，只改其中一个容易产生漂移。

数据库连接配置放在 `backend/src/main/resources/datasource/jdbc.properties` 中，并与 `application.properties` 分开加载。

### 前端

前端是一个在 `web/src/main.ts` 中启动的 Vue 3 单页应用，使用了：
- Pinia 做状态管理
- Vue Router 做路由
- Element Plus 做 UI
- Axios 做 API 访问

`@` 别名通过 `vite.config.ts` 和 TypeScript 配置映射到 `web/src`。

当前路由表集中定义在 `web/src/router/index.ts`。虽然仓库中存在 `router/blog.ts` 和 `router/admin.ts`，但实际仍然是在 `index.ts` 中直接注册路由。

### 前端数据与认证流程

API 访问统一经过 `web/src/api/requests.ts`。

- 基础 URL 来自 `VITE_API_BASE_URL`
- 请求拦截器会从 Pinia 用户 store 注入 `Authorization: Bearer <token>`
- 响应拦截器会解包后端的 `Result<T>` 包装结构
- 收到 401 响应时会清除本地 token 状态并跳转到 `/login`

认证状态保存在 `web/src/stores/user.ts`。

- `token` 会持久化到 `localStorage`
- `login()` 会保存返回的 JWT 和基础用户资料
- `logout()` 会同时清除 Pinia 状态和 `localStorage`
- `getUserInfo()` 期望后端提供一个返回当前用户信息的接口

要保持前后端认证 API 一致：`web/src/api/auth.ts` 和 Pinia store 期望存在 `getUserInfo`、`logout` 等接口，但当前后端控制器主要只提供注册、登录、修改密码。

## 配置说明

- 前端本地 API 目标地址定义在 `web/.env.development` 中，为 `http://localhost:8080/api`
- 后端 JWT 配置和 MyBatis-Plus 逻辑删除配置位于 `backend/src/main/resources/application.properties`
- Vitest 通过 `web/vitest.config.ts` 使用 `jsdom`

## 当前仓库中的测试现状

- 后端目前只有一个最小化的 Spring Boot 测试脚手架：`backend/src/test/java/com/wintina/blog/BackendApplicationTests.java`
- 前端已经在 `web/vitest.config.ts` 中配置了 Vitest，但 `web/src` 下目前还没有已提交的前端测试文件
