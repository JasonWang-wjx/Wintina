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
@Table(name = "menu", indexes = {
        @Index(name = "idx_parent_id", columnList = "parent_id"),
        @Index(name = "idx_menu_type", columnList = "menu_type"),
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = 0")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '菜单ID'")
    private Long id;

    @Column(name = "parent_id", columnDefinition = "BIGINT DEFAULT 0 COMMENT '父菜单ID'")
    private Long parentId = 0L;

    // 自关联：父菜单（多对一）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Menu parentMenu;

    // 自关联：子菜单（一对多）
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentMenu")
    private List<Menu> childMenus;

    @Column(name = "menu_name", nullable = false, length = 50, columnDefinition = "VARCHAR(50) COMMENT '菜单名称'")
    private String menuName;

    @Column(name = "path", length = 255, columnDefinition = "VARCHAR(255) COMMENT '路由路径'")
    private String path;

    @Column(name = "component", length = 255, columnDefinition = "VARCHAR(255) COMMENT '组件路径'")
    private String component;

    @Column(name = "icon", length = 50, columnDefinition = "VARCHAR(50) COMMENT '菜单图标'")
    private String icon;

    @Column(name = "order_num", columnDefinition = "INT DEFAULT 0 COMMENT '排序'")
    private Integer orderNum = 0;

    @Column(name = "menu_type", columnDefinition = "TINYINT COMMENT '类型(1:目录 2:菜单 3:按钮)'")
    private Integer menuType;

    @Column(name = "permission", length = 100, columnDefinition = "VARCHAR(100) COMMENT '权限标识'")
    private String permission;

    @Column(name = "is_hidden", columnDefinition = "TINYINT DEFAULT 0 COMMENT '是否隐藏(0:显示 1:隐藏)'")
    private Integer isHidden = 0;

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