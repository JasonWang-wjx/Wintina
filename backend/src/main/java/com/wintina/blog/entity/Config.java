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
@TableName("config")
public class Config {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "config_key")
    private String configKey;

    @TableField(value = "config_value")
    private String configValue;

    @TableField(value = "description")
    private String description;

    @TableField(value = "type")
    private String type = "STRING";

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}