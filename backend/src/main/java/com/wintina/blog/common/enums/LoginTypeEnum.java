package com.wintina.blog.common.enums;

/**
 * 登录类型枚举
 */
public enum LoginTypeEnum {
    /**
     * 账号登录
     */
    ACCOUNT(1, "账号"),
    
    /**
     * 微信登录
     */
    WECHAT(2, "微信"),
    
    /**
     * QQ登录
     */
    QQ(3, "QQ");
    
    private final Integer code;
    private final String name;
    
    LoginTypeEnum(Integer code, String name) {
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
     * 根据类型码获取枚举
     * @param code 类型码
     * @return 枚举实例
     */
    public static LoginTypeEnum getByCode(Integer code) {
        for (LoginTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}