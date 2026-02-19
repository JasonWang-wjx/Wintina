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
@Table(name = "role_menu", indexes = {
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_role_menu", columnNames = {"role_id", "menu_id"})
})
@SQLRestriction("is_deleted = 0")
public class RoleMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'ID'")
    private Long id;

    @Column(name = "role_id", nullable = false, columnDefinition = "BIGINT COMMENT '角色ID'")
    private Long roleId;

    // 关联角色表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name = "menu_id", nullable = false, columnDefinition = "BIGINT COMMENT '菜单ID'")
    private Long menuId;

    // 关联菜单表
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private Menu menu;

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