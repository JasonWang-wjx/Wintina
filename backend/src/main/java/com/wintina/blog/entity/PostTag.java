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
@TableName("post_tag")
public class PostTag {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "post_id")
    private Long postId;

    // 关联博文表
    @TableField(exist = false)
    private Post post;

    @TableField(value = "tag_id")
    private Long tagId;

    // 关联标签表
    @TableField(exist = false)
    private Tag tag;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}