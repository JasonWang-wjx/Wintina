package com.wintina.blog.dto.post;

import lombok.Data;

@Data
public class PostQueryDTO {
    private Long categoryId;
    private Long tagId;
    private String keyword;
    private Integer status;
    private Long userId;
    private Long current = 1L;
    private Long size = 10L;
}
