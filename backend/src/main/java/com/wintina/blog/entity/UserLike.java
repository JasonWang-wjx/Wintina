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
@Table(name = "user_like", indexes = {
        @Index(name = "idx_target", columnList = "target_type, target_id"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_like", columnNames = {"user_id", "target_type", "target_id"})
})
@SQLRestriction("is_deleted = 0")
public class UserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'ID'")
    private Long id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BIGINT COMMENT '用户ID'")
    private Long userId;

    // 关联用户表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "target_type", nullable = false, length = 20, columnDefinition = "VARCHAR(20) COMMENT '目标类型: POST, COMMENT'")
    private String targetType;

    @Column(name = "target_id", nullable = false, columnDefinition = "BIGINT COMMENT '目标ID'")
    private Long targetId;

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