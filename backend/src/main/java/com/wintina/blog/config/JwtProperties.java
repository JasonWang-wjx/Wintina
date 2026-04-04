package com.wintina.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT签名密钥
     */
    private String secret = "your-default-secret-key-for-jwt-signing-must-be-at-least-256-bits";
    /**
     * 访问令牌过期时间
     */
    private Long expiration = 86400000L;

    /**
     * 刷新令牌过期时间
     */
    private Long refreshExpiration = 604800000L;
    /**
     * 访问令牌头名称
     */
    private String header = "Authorization";
    /**
     * 访问令牌前缀
     */
    private String prefix = "Bearer ";
    /**
     * 获取JWT签名密钥
     * @return JWT签名密钥
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 设置JWT签名密钥
     * @param secret JWT签名密钥
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * 获取访问令牌过期时间
     * @return 访问令牌过期时间
     */
    public Long getExpiration() {
        return expiration;
    }

    /**
     * 设置访问令牌过期时间
     * @param expiration 访问令牌过期时间
     */
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    /**
     * 获取刷新令牌过期时间
     * @return 刷新令牌过期时间
     */
    public Long getRefreshExpiration() {
        return refreshExpiration;
    }

    /**
     * 设置刷新令牌过期时间
     * @param refreshExpiration 刷新令牌过期时间
     */
    public void setRefreshExpiration(Long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    /**
     * 获取访问令牌头名称
     * @return 访问令牌头名称
     */
    public String getHeader() {
        return header;
    }

    /**
     * 设置访问令牌头名称
     * @param header 访问令牌头名称
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * 获取访问令牌前缀
     * @return 访问令牌前缀
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 设置访问令牌前缀
     * @param prefix 访问令牌前缀
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
