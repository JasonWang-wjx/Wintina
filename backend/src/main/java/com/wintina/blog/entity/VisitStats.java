package com.wintina.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visit_stats", uniqueConstraints = {
        @UniqueConstraint(name = "uk_date", columnNames = "date")
})
public class VisitStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT COMMENT 'ID'")
    private Long id;

    @Column(name = "date", nullable = false, columnDefinition = "DATE COMMENT '日期'")
    private LocalDate date;

    @Column(name = "pv", columnDefinition = "INT DEFAULT 0 COMMENT '页面浏览量'")
    private Integer pv = 0;

    @Column(name = "uv", columnDefinition = "INT DEFAULT 0 COMMENT '独立访客数'")
    private Integer uv = 0;

    @Column(name = "ip_count", columnDefinition = "INT DEFAULT 0 COMMENT 'IP数量'")
    private Integer ipCount = 0;

    @Column(name = "create_time", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private LocalDateTime createTime;

    @Column(name = "update_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'")
    private LocalDateTime updateTime;

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