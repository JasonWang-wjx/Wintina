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
@TableName("post")
public class Post {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    // 关联用户表
    @TableField(exist = false)
    private User user;

    @TableField(value = "category_id")
    private Long categoryId;

    // 关联分类表
    @TableField(exist = false)
    private Category category;

    @TableField(value = "title")
    private String title;

    @TableField(value = "slug")
    private String slug;

    @TableField(value = "summary")
    private String summary;

    @TableField(value = "content")
    private String content;

    @TableField(value = "content_html")
    private String contentHtml;

    @TableField(value = "cover_image")
    private String coverImage;

    @TableField(value = "status")
    private Integer status = 1;

    @TableField(value = "is_top")
    private Integer isTop = 0;

    @TableField(value = "is_recommend")
    private Integer isRecommend = 0;

    @TableField(value = "allow_comment")
    private Integer allowComment = 1;

    @TableField(value = "view_count")
    private Integer viewCount = 0;

    @TableField(value = "like_count")
    private Integer likeCount = 0;

    @TableField(value = "comment_count")
    private Integer commentCount = 0;

    @TableField(value = "collect_count")
    private Integer collectCount = 0;

    @TableField(value = "sort_order")
    private Integer sortOrder = 0;

    @TableField(value = "seo_keywords")
    private String seoKeywords;

    @TableField(value = "seo_description")
    private String seoDescription;

    @TableField(value = "publish_time")
    private LocalDateTime publishTime;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(value = "is_deleted")
    private Integer isDeleted = 0;


}