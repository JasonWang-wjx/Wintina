package com.wintina.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
})
@SQLRestriction("is_deleted = 0")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT '用户ID'")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true, length = 50, columnDefinition = "VARCHAR(50) COMMENT '用户名'")
    private String username;

    @Column(name = "password", nullable = false, length = 255, columnDefinition = "VARCHAR(255) COMMENT '密码(BCrypt加密)'")
    private String password;

    @Column(name = "nickname", length = 50, columnDefinition = "VARCHAR(50) COMMENT '昵称'")
    private String nickname;

    @Column(name = "email", unique = true, length = 100, columnDefinition = "VARCHAR(100) COMMENT '邮箱'")
    private String email;

    @Column(name = "phone", length = 20, columnDefinition = "VARCHAR(20) COMMENT '手机号'")
    private String phone;

    @Column(name = "avatar", length = 255, columnDefinition = "VARCHAR(255) COMMENT '头像URL'")
    private String avatar;

    @Column(name = "intro", length = 500, columnDefinition = "VARCHAR(500) COMMENT '个人简介'")
    private String intro;

    @Column(name = "website", length = 255, columnDefinition = "VARCHAR(255) COMMENT '个人网站'")
    private String website;

    @Column(name = "role_id", columnDefinition = "BIGINT COMMENT '角色ID'")
    private Long roleId;

    // 关联角色表（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name = "login_type", columnDefinition = "TINYINT COMMENT '登录方式(1:账号 2:微信 3:QQ)'")
    private Integer loginType;

    @Column(name = "ip_address", length = 50, columnDefinition = "VARCHAR(50) COMMENT '登录IP'")
    private String ipAddress;

    @Column(name = "ip_source", length = 100, columnDefinition = "VARCHAR(100) COMMENT 'IP来源'")
    private String ipSource;

    @Column(name = "last_login_time", columnDefinition = "DATETIME COMMENT '最后登录时间'")
    private LocalDateTime lastLoginTime;

    @Column(name = "create_time", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private LocalDateTime createTime;

    @Column(name = "update_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
    private LocalDateTime updateTime;

    @Column(name = "is_deleted", columnDefinition = "TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除 1:已删除)'")
    private Integer isDeleted = 0;

    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}