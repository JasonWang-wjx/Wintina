package com.wintina.blog.interceptor;

import com.wintina.blog.common.exception.AppException;
import com.wintina.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    /** 拦截器：验证 JWT 令牌 */
    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response, Object handler) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AppException(401, "未登录，请先登录");
        }
        String token = header.substring(7);
        if (!jwtUtil.validateToken(token, jwtUtil.getUserIdFromToken(token))) {
            throw new AppException(401, "Token 已过期或无效");
        }
        // 将 userId 存入 request，方便后续 Controller 使用
        request.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
        return true;
    }
}