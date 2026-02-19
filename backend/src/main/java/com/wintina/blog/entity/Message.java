package com.wintina.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = 0")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '留言ID'")
    private Long id;

    @Column(name = "user_id", columnDefinition = "BIGINT COMMENT '用户ID'")
    private Long userId;

    // 关联用户表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "parent_id", columnDefinition = "BIGINT DEFAULT 0 COMMENT '父留言ID'")
    private Long parentId = 0L;

    // 自关联：父留言
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Message parentMessage;

    // 自关联：子留言
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentMessage")
    private List<Message> childMessages;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT COMMENT '留言内容'")
    private String content;

    @Column(name = "nickname", length = 50, columnDefinition = "VARCHAR(50) COMMENT '昵称(游客)'")
    private String nickname;

    @Column(name = "email", length = 100, columnDefinition = "VARCHAR(100) COMMENT '邮箱(游客)'")
    private String email;

    @Column(name = "website", length = 200, columnDefinition = "VARCHAR(200) COMMENT '网站(游客)'")
    private String website;

    @Column(name = "ip", length = 50, columnDefinition = "VARCHAR(50) COMMENT 'IP地址'")
    private String ip;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1 COMMENT '状态: 0-待审核, 1-已通过, 2-已拒绝'")
    private Integer status = 1;

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