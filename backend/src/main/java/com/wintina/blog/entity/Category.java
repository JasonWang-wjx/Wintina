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
@TableName("category")
public class Category {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "slug")
    private String slug;

    @TableField(value = "description")
    private String description;

    @TableField(value = "parent_id")
    private Long parentId = 0L;

    // 自关联：父分类（多对一）
    @TableField(exist = false)
    private Category parentCategory;

    // 自关联：子分类（一对多）
    @TableField(exist = false)
    private List<Category> childCategories;

    @TableField(value = "sort_order")
    private Integer sortOrder = 0;

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