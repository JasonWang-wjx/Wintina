package com.wintina.blog.vo.post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDetailVO {
    @Data
    public static class TocItem {
        private String id;
        private String text;
        private Integer level;
    }

    @Data
    public static class RelatedPostItem {
        private Long id;
        private String title;
        private String coverImage;
        private LocalDateTime publishTime;
        private Integer viewCount;
    }

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String contentHtml;
    private String coverImage;
    private Integer status;
    private Integer allowComment;
    private Integer isTop;
    private Integer isRecommend;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    private Integer sortOrder;
    private String seoKeywords;
    private String seoDescription;
    private Long userId;
    private String authorName;
    private String authorAvatar;
    private String authorBio;
    private Integer authorPostCount;
    private Integer readTimeMinutes;
    private List<TocItem> toc;
    private List<RelatedPostItem> relatedPosts;
    private Long categoryId;
    private String categoryName;
    private List<Long> tagIds;
    private List<String> tagNames;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
