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
@Table(name = "category", indexes = {
        @Index(name = "idx_parent_id", columnList = "parent_id"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = 0")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '分类ID'")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50, columnDefinition = "VARCHAR(50) COMMENT '分类名称'")
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 50, columnDefinition = "VARCHAR(50) COMMENT '分类别名'")
    private String slug;

    @Column(name = "description", length = 255, columnDefinition = "VARCHAR(255) COMMENT '分类描述'")
    private String description;

    @Column(name = "parent_id", columnDefinition = "BIGINT DEFAULT 0 COMMENT '父分类ID'")
    private Long parentId = 0L;

    // 自关联：父分类（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Category parentCategory;

    // 自关联：子分类（一对多）
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCategory")
    private List<Category> childCategories;

    @Column(name = "sort_order", columnDefinition = "INT DEFAULT 0 COMMENT '排序'")
    private Integer sortOrder = 0;

    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 1 COMMENT '状态(0:禁用 1:正常)'")
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