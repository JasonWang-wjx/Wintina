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
@Table(name = "comment", indexes = {
        @Index(name = "idx_post_id", columnList = "post_id"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_parent_id", columnList = "parent_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = 0")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '评论ID'")
    private Long id;

    @Column(name = "post_id", nullable = false, columnDefinition = "BIGINT COMMENT '文章ID'")
    private Long postId;

    // 关联博文表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @Column(name = "user_id", columnDefinition = "BIGINT COMMENT '用户ID(游客为NULL)'")
    private Long userId;

    // 关联用户表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "parent_id", columnDefinition = "BIGINT DEFAULT 0 COMMENT '父评论ID, 0表示顶级评论'")
    private Long parentId = 0L;

    // 自关联：父评论
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Comment parentComment;

    // 自关联：子评论
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment")
    private List<Comment> childComments;

    @Column(name = "reply_to_id", columnDefinition = "BIGINT COMMENT '回复的评论ID'")
    private Long replyToId;

    // 关联回复的评论
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_id", insertable = false, updatable = false)
    private Comment replyToComment;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT COMMENT '评论内容'")
    private String content;

    @Column(name = "nickname", length = 50, columnDefinition = "VARCHAR(50) COMMENT '昵称(游客)'")
    private String nickname;

    @Column(name = "email", length = 100, columnDefinition = "VARCHAR(100) COMMENT '邮箱(游客)'")
    private String email;

    @Column(name = "website", length = 200, columnDefinition = "VARCHAR(200) COMMENT '网站(游客)'")
    private String website;

    @Column(name = "ip", length = 50, columnDefinition = "VARCHAR(50) COMMENT 'IP地址'")
    private String ip;

    @Column(name = "user_agent", length = 500, columnDefinition = "VARCHAR(500) COMMENT '用户代理'")
    private String userAgent;

    @Column(name = "like_count", columnDefinition = "INT DEFAULT 0 COMMENT '点赞数'")
    private Integer likeCount = 0;

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