package com.wintina.blog.common.exception;

import lombok.Getter;

/**
 * 自定义运行时异常
 * @author Jason
 * @description 自定义运行时异常类
 * @date 2026-03-01
 * @version 1.0
 */
public class AppException extends RuntimeException {
    private Integer code;
    
    public AppException() {
        super();
    }
    
    public AppException(String message) {
        super(message);
    }

    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误信息
     */
    public AppException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    /**
     * 构造方法
     * @param message 错误信息
     * @param cause 引起异常的可抛出对象
     */
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误信息
     * @param cause 引起异常的可抛出对象
     */
    public AppException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 获取错误码
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置错误码
     * @param code 错误码
     */
    public void setCode(Integer code) {
        this.code = code;
    }
}