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
@Table(name = "post", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_category_id", columnList = "category_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_publish_time", columnList = "publish_time"),
        @Index(name = "idx_is_top_recommend", columnList = "is_top, is_recommend"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted"),
        // 全文索引无需JPA注解（数据库层面配置）
        @Index(name = "idx_title_content", columnList = "title, content")
})
@SQLRestriction("is_deleted = 0")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '文章ID'")
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT '作者ID'")
    private Long userId;

    // 关联用户表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "category_id", columnDefinition = "BIGINT COMMENT '分类ID'")
    private Long categoryId;

    // 关联分类表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private Category category;

    @Column(name = "title", nullable = false, length = 200, columnDefinition = "VARCHAR(200) COMMENT '文章标题'")
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 200, columnDefinition = "VARCHAR(200) COMMENT '文章别名(URL)'")
    private String slug;

    @Column(name = "summary", columnDefinition = "TEXT COMMENT '文章摘要'")
    private String summary;

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT COMMENT '文章内容(Markdown)'")
    private String content;

    @Column(name = "content_html", columnDefinition = "LONGTEXT COMMENT '文章内容(HTML)'")
    private String contentHtml;

    @Column(name = "cover_image", length = 255, columnDefinition = "VARCHAR(255) COMMENT '封面图片'")
    private String coverImage;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1 COMMENT '状态: 0-草稿, 1-已发布, 2-私密'")
    private Integer status = 1;

    @Column(name = "is_top", columnDefinition = "TINYINT DEFAULT 0 COMMENT '是否置顶: 0-否, 1-是'")
    private Integer isTop = 0;

    @Column(name = "is_recommend", columnDefinition = "TINYINT DEFAULT 0 COMMENT '是否推荐: 0-否, 1-是'")
    private Integer isRecommend = 0;

    @Column(name = "allow_comment", columnDefinition = "TINYINT DEFAULT 1 COMMENT '是否允许评论: 0-否, 1-是'")
    private Integer allowComment = 1;

    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0 COMMENT '浏览次数'")
    private Integer viewCount = 0;

    @Column(name = "like_count", columnDefinition = "INT DEFAULT 0 COMMENT '点赞数'")
    private Integer likeCount = 0;

    @Column(name = "comment_count", columnDefinition = "INT DEFAULT 0 COMMENT '评论数'")
    private Integer commentCount = 0;

    @Column(name = "collect_count", columnDefinition = "INT DEFAULT 0 COMMENT '收藏数'")
    private Integer collectCount = 0;

    @Column(name = "sort_order", columnDefinition = "INT DEFAULT 0 COMMENT '排序'")
    private Integer sortOrder = 0;

    @Column(name = "seo_keywords", length = 200, columnDefinition = "VARCHAR(200) COMMENT 'SEO关键词'")
    private String seoKeywords;

    @Column(name = "seo_description", length = 500, columnDefinition = "VARCHAR(500) COMMENT 'SEO描述'")
    private String seoDescription;

    @Column(name = "publish_time", columnDefinition = "DATETIME COMMENT '发布时间'")
    private LocalDateTime publishTime;

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