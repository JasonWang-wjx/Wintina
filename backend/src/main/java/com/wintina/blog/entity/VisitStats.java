package com.wintina.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("visit_stats")
public class VisitStats {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "date")
    private LocalDate date;

    @TableField(value = "pv")
    private Integer pv = 0;

    @TableField(value = "uv")
    private Integer uv = 0;

    @TableField(value = "ip_count")
    private Integer ipCount = 0;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;


}