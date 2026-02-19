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
@Table(name = "operation_log", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_create_time", columnList = "create_time"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = 0")
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '日志ID'")
    private Long id;

    @Column(name = "user_id", columnDefinition = "BIGINT COMMENT '用户ID'")
    private Long userId;

    // 关联用户表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "username", length = 50, columnDefinition = "VARCHAR(50) COMMENT '用户名'")
    private String username;

    @Column(name = "operation", nullable = false, length = 100, columnDefinition = "VARCHAR(100) COMMENT '操作内容'")
    private String operation;

    @Column(name = "method", length = 200, columnDefinition = "VARCHAR(200) COMMENT '请求方法'")
    private String method;

    @Column(name = "params", columnDefinition = "TEXT COMMENT '请求参数'")
    private String params;

    @Column(name = "result", columnDefinition = "TEXT COMMENT '返回结果'")
    private String result;

    @Column(name = "ip", length = 50, columnDefinition = "VARCHAR(50) COMMENT 'IP地址'")
    private String ip;

    @Column(name = "location", length = 100, columnDefinition = "VARCHAR(100) COMMENT '操作地点'")
    private String location;

    @Column(name = "browser", length = 100, columnDefinition = "VARCHAR(100) COMMENT '浏览器'")
    private String browser;

    @Column(name = "os", length = 100, columnDefinition = "VARCHAR(100) COMMENT '操作系统'")
    private String os;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1 COMMENT '状态: 0-失败, 1-成功'")
    private Integer status = 1;

    @Column(name = "error_msg", columnDefinition = "TEXT COMMENT '错误信息'")
    private String errorMsg;

    @Column(name = "cost_time", columnDefinition = "INT COMMENT '耗时(毫秒)'")
    private Integer costTime;

    @Column(name = "create_time", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private LocalDateTime createTime;

    @Column(name = "is_deleted", columnDefinition = "TINYINT DEFAULT 0 COMMENT '逻辑删除(0:未删除 1:已删除)'")
    private Integer isDeleted = 0;

    @PrePersist
    protected void onCreate() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
}