package com.wintina.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tag")
public class Tag {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "slug")
    private String slug;

    @TableField(value = "color")
    private String color;

    @TableField(value = "post_count")
    private Integer postCount = 0;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;

}