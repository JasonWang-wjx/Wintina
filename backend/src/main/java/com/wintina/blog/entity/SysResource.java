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
@Table(name = "resource", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = 0")
public class SysResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '资源ID'")
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT '发布用户ID'")
    private Long userId;

    // 关联用户表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 200, columnDefinition = "VARCHAR(200) COMMENT '资源标题'")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT COMMENT '资源描述'")
    private String description;

    @Column(name = "url", nullable = false, length = 500, columnDefinition = "VARCHAR(500) COMMENT '资源链接'")
    private String url;

    @Column(name = "file_url", length = 500, columnDefinition = "VARCHAR(500) COMMENT '文件下载地址'")
    private String fileUrl;

    @Column(name = "file_size", columnDefinition = "BIGINT COMMENT '文件大小'")
    private Long fileSize;

    @Column(name = "file_type", length = 50, columnDefinition = "VARCHAR(50) COMMENT '文件类型'")
    private String fileType;

    @Column(name = "icon", length = 255, columnDefinition = "VARCHAR(255) COMMENT '图标'")
    private String icon;

    @Column(name = "type", nullable = false, length = 20, columnDefinition = "VARCHAR(20) NOT NULL COMMENT '类型: TOOL-工具, RESOURCE-资源'")
    private String type;

    @Column(name = "download_count", columnDefinition = "INT DEFAULT 0 COMMENT '下载次数'")
    private Integer downloadCount = 0;

    @Column(name = "view_count", columnDefinition = "INT DEFAULT 0 COMMENT '查看次数'")
    private Integer viewCount = 0;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1 COMMENT '状态: 0-下架, 1-上架'")
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