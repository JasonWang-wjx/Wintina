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
@Table(name = "config", indexes = {
        @Index(name = "idx_is_deleted", columnList = "is_deleted")
})
@SQLRestriction("is_deleted = 0")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT '配置ID'")
    private Long id;

    @Column(name = "config_key", nullable = false, unique = true, length = 100, columnDefinition = "VARCHAR(100) COMMENT '配置键'")
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT COMMENT '配置值'")
    private String configValue;

    @Column(name = "description", length = 500, columnDefinition = "VARCHAR(500) COMMENT '配置描述'")
    private String description;

    @Column(name = "type", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'STRING' COMMENT '类型: STRING, NUMBER, BOOLEAN, JSON'")
    private String type = "STRING";

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