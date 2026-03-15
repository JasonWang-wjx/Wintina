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
@TableName("image")
public class Image {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    // 关联用户表
    @TableField(exist = false)
    private User user;

    @TableField(value = "name")
    private String name;

    @TableField(value = "url")
    private String url;

    @TableField(value = "thumbnail_url")
    private String thumbnailUrl;

    @TableField(value = "size")
    private Long size;

    @TableField(value = "width")
    private Integer width;

    @TableField(value = "height")
    private Integer height;

    @TableField(value = "mime_type")
    private String mimeType;

    @TableField(value = "storage_type")
    private String storageType = "LOCAL";

    @TableField(value = "storage_path")
    private String storagePath;

    @TableField(value = "description")
    private String description;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}