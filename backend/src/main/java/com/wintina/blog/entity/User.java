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
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO, value = "user_id")
    private Long userId;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "nickname")
    private String nickname;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "intro")
    private String intro;

    @TableField(value = "website")
    private String website;

    @TableField(value = "role_id")
    private Long roleId;

    // 关联角色表（多对一）
    @TableField(exist = false)
    private Role role;

    @TableField(value = "login_type")
    private Integer loginType;

    @TableField(value = "ip_address")
    private String ipAddress;

    @TableField(value = "ip_source")
    private String ipSource;

    @TableField(value = "last_login_time")
    private LocalDateTime lastLoginTime;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}