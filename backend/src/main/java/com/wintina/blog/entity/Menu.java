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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("menu")
public class Menu {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "parent_id")
    private Long parentId = 0L;

    // 自关联：父菜单（多对一）
    @TableField(exist = false)
    private Menu parentMenu;

    // 自关联：子菜单（一对多）
    @TableField(exist = false)
    private List<Menu> childMenus;

    @TableField(value = "menu_name")
    private String menuName;

    @TableField(value = "path")
    private String path;

    @TableField(value = "component")
    private String component;

    @TableField(value = "icon")
    private String icon;

    @TableField(value = "order_num")
    private Integer orderNum = 0;

    @TableField(value = "menu_type")
    private Integer menuType;

    @TableField(value = "permission")
    private String permission;

    @TableField(value = "is_hidden")
    private Integer isHidden = 0;

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