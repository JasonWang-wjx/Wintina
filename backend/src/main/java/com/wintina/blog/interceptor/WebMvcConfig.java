package com.wintina.blog.interceptor;

import com.wintina.blog.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    /** 配置 Web MVC 拦截器 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/api/**")       // 拦截所有 /api/ 请求
            .excludePathPatterns(              // 放行不需要登录的接口
                "/api/auth/login",
                "/api/auth/register",
                "/api/portal/**"              // 博客前台公开接口
            );
    }
}