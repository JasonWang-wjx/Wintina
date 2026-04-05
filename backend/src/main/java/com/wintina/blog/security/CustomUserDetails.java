package com.wintina.blog.security;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUserDetails extends User {

    private final Long userId;

    /**
     * 自定义的CustomUserDetails类，用于存储用户ID
     * @param username 用户名
     * @param password 密码
     * @param authorities 权限列表
     * @param userId 用户ID
     */
    public CustomUserDetails(String username, String password,
                             Collection<SimpleGrantedAuthority> authorities, 
                             Long userId) {
        super(username, password, authorities);
        this.userId = userId;
    }
}