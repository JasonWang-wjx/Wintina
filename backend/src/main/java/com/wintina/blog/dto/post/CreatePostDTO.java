package com.wintina.blog.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreatePostDTO {
    @NotBlank(message = "标题不能为空")
    private String title;

    private String slug;

    private String summary;

    @NotBlank(message = "内容不能为空")
    private String content;

    private String contentHtml;

    private String coverImage;

    @NotNull(message = "文章状态不能为空")
    private Integer status;

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    private List<Long> tagIds;

    private Integer allowComment;

    private Integer isTop;

    private Integer isRecommend;

    private Integer sortOrder;

    private String seoKeywords;

    private String seoDescription;
}
