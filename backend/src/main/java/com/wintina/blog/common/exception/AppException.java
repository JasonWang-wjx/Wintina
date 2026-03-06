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
    
    public AppException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AppException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}