package com.wintina.blog.common.enums;

/**
 * 文章状态枚举
 */
public enum PostStatusEnum {
    /**
     * 草稿
     */
    DRAFT(0, "草稿"),
    
    /**
     * 已发布
     */
    PUBLISHED(1, "已发布"),
    
    /**
     * 私密
     */
    PRIVATE(2, "私密");
    
    private final Integer code;
    private final String name;
    
    PostStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return 枚举实例
     */
    public static PostStatusEnum getByCode(Integer code) {
        for (PostStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}