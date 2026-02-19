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
@Table(name = "image", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = 0")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '图片ID'")
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT '上传用户ID'")
    private Long userId;

    // 关联用户表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 200, columnDefinition = "VARCHAR(200) COMMENT '图片名称'")
    private String name;

    @Column(name = "url", nullable = false, length = 500, columnDefinition = "VARCHAR(500) COMMENT '图片URL'")
    private String url;

    @Column(name = "thumbnail_url", length = 500, columnDefinition = "VARCHAR(500) COMMENT '缩略图URL'")
    private String thumbnailUrl;

    @Column(name = "size", columnDefinition = "BIGINT COMMENT '文件大小(字节)'")
    private Long size;

    @Column(name = "width", columnDefinition = "INT COMMENT '图片宽度'")
    private Integer width;

    @Column(name = "height", columnDefinition = "INT COMMENT '图片高度'")
    private Integer height;

    @Column(name = "mime_type", length = 50, columnDefinition = "VARCHAR(50) COMMENT 'MIME类型'")
    private String mimeType;

    @Column(name = "storage_type", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'LOCAL' COMMENT '存储类型: LOCAL, OSS, COS'")
    private String storageType = "LOCAL";

    @Column(name = "storage_path", length = 500, columnDefinition = "VARCHAR(500) COMMENT '存储路径'")
    private String storagePath;

    @Column(name = "description", length = 500, columnDefinition = "VARCHAR(500) COMMENT '图片描述'")
    private String description;

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