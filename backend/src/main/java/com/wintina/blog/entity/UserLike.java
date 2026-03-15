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
@TableName("user_like")
public class UserLike {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    // 关联用户表
    @TableField(exist = false)
    private User user;

    @TableField(value = "target_type")
    private String targetType;

    @TableField(value = "target_id")
    private Long targetId;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}