package com.wintina.blog.vo.post;

import lombok.Data;

import java.util.List;

@Data
public class PostEditorOptionsVO {
    @Data
    public static class CategoryOption {
        private Long id;
        private String name;
    }

    @Data
    public static class TagOption {
        private Long id;
        private String name;
        private String color;
    }

    @Data
    public static class StatusOption {
        private Integer value;
        private String label;
    }

    private List<CategoryOption> categories;
    private List<TagOption> tags;
    private List<StatusOption> statusOptions;
}
