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
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "post_id")
    private Long postId;

    // 关联博文表
    @TableField(exist = false)
    private Post post;

    @TableField(value = "user_id")
    private Long userId;

    // 关联用户表
    @TableField(exist = false)
    private User user;

    @TableField(value = "parent_id")
    private Long parentId = 0L;

    // 自关联：父评论
    @TableField(exist = false)
    private Comment parentComment;

    // 自关联：子评论
    @TableField(exist = false)
    private List<Comment> childComments;

    @TableField(value = "reply_to_id")
    private Long replyToId;

    // 关联回复的评论
    @TableField(exist = false)
    private Comment replyToComment;

    @TableField(value = "content")
    private String content;

    @TableField(value = "nickname")
    private String nickname;

    @TableField(value = "email")
    private String email;

    @TableField(value = "website")
    private String website;

    @TableField(value = "ip")
    private String ip;

    @TableField(value = "user_agent")
    private String userAgent;

    @TableField(value = "like_count")
    private Integer likeCount = 0;

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