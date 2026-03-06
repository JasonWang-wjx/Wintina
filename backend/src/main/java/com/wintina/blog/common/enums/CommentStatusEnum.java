package com.wintina.blog.common.enums;

/**
 * 评论状态枚举
 */
public enum CommentStatusEnum {
    /**
     * 待审核
     */
    PENDING(0, "待审核"),
    
    /**
     * 通过
     */
    APPROVED(1, "通过"),
    
    /**
     * 拒绝
     */
    REJECTED(2, "拒绝");
    
    private final Integer code;
    private final String name;
    
    CommentStatusEnum(Integer code, String name) {
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
    public static CommentStatusEnum getByCode(Integer code) {
        for (CommentStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}