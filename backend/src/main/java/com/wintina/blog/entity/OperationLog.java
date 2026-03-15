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
@TableName("operation_log")
public class OperationLog {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    // 关联用户表
    @TableField(exist = false)
    private User user;

    @TableField(value = "username")
    private String username;

    @TableField(value = "operation")
    private String operation;

    @TableField(value = "method")
    private String method;

    @TableField(value = "params")
    private String params;

    @TableField(value = "result")
    private String result;

    @TableField(value = "ip")
    private String ip;

    @TableField(value = "location")
    private String location;

    @TableField(value = "browser")
    private String browser;

    @TableField(value = "os")
    private String os;

    @TableField(value = "status")
    private Integer status = 1;

    @TableField(value = "error_msg")
    private String errorMsg;

    @TableField(value = "cost_time")
    private Integer costTime;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}