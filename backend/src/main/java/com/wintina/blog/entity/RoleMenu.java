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
@TableName("role_menu")
public class RoleMenu {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "role_id")
    private Long roleId;

    // 关联角色表
    @TableField(exist = false)
    private Role role;

    @TableField(value = "menu_id")
    private Long menuId;

    // 关联菜单表
    @TableField(exist = false)
    private Menu menu;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}