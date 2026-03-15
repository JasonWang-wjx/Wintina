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
@TableName("resource")
public class SysResource {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    // 关联用户表
    @TableField(exist = false)
    private User user;

    @TableField(value = "title")
    private String title;

    @TableField(value = "description")
    private String description;

    @TableField(value = "url")
    private String url;

    @TableField(value = "file_url")
    private String fileUrl;

    @TableField(value = "file_size")
    private Long fileSize;

    @TableField(value = "file_type")
    private String fileType;

    @TableField(value = "icon")
    private String icon;

    @TableField(value = "type")
    private String type;

    @TableField(value = "download_count")
    private Integer downloadCount = 0;

    @TableField(value = "view_count")
    private Integer viewCount = 0;

    @TableField(value = "status")
    private Integer status = 1;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}