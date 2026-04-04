package com.wintina.blog.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    /**
     * JWT签名密钥
     */
    @Value("${jwt.secret:your-default-secret-key-for-jwt-signing-must-be-at-least-256-bits}")
    private String secret;

    /**
     * 访问令牌过期时间
     */
    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    /**
     * 刷新令牌过期时间
     */
    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    /**
     * 获取JWT签名密钥
     * @return JWT签名密钥
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成访问令牌
     * @param userId 用户ID
     * @param username 用户名
     * @return 访问令牌
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims, userId.toString(), expiration);
    }

    /**
     * 生成刷新令牌
     * @param userId 用户ID
     * @param username 用户名
     * @return 刷新令牌
     */
    public String generateRefreshToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("type", "refresh");
        return createToken(claims, userId.toString(), refreshExpiration);
    }

    /**
     * 创建JWT令牌
     * @param claims 令牌声明
     * @param subject 令牌主题
     * @param expirationTime 过期时间
     * @return JWT令牌
     */
    private String createToken(Map<String, Object> claims, String subject, Long expirationTime) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token JWT令牌
     * @return 令牌声明
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从JWT令牌中提取用户ID
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.getSubject());
    }

    /**
     * 从JWT令牌中提取用户名
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class);
    }

    /**
     * 检查JWT令牌是否过期
     * @param token JWT令牌
     * @return 如果令牌过期则返回true，否则返回false
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证JWT令牌是否有效
     * @param token JWT令牌
     * @param userId 用户ID
     * @return 如果令牌有效则返回true，否则返回false
     */
    public boolean validateToken(String token, Long userId) {
        try {
            Claims claims = parseToken(token);
            Long tokenUserId = Long.valueOf(claims.getSubject());
            return (tokenUserId.equals(userId) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新JWT令牌
     * @param token 刷新令牌
     * @return 新的访问令牌
     */
    public String refreshToken(String token) {
        Claims claims = parseToken(token);
        Long userId = Long.valueOf(claims.getSubject());
        String username = claims.get("username", String.class);
        return generateToken(userId, username);
    }

    /**
     * 从JWT令牌中提取过期时间
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }
}
