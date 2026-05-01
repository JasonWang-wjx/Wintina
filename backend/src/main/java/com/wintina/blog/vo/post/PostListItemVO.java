package com.wintina.blog.vo.post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostListItemVO {
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String coverImage;
    private Integer status;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Long userId;
    private String authorName;
    private Long categoryId;
    private String categoryName;
    private List<Long> tagIds;
    private List<String> tagNames;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
