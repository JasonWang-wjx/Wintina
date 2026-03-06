package com.wintina.blog.common.exception;

import com.wintina.blog.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author Jason
 * @description 全局异常处理器类
 * @date 2026-03-01
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理自定义异常
     * @param e 自定义异常
     * @return 错误响应
     */
    @ExceptionHandler(AppException.class)
    public Result<?> handleAppException(AppException e) {
        logger.error("自定义异常：{}", e.getMessage(), e);
        Integer code = e.getCode() != null ? e.getCode() : 500;
        return Result.fail(code, e.getMessage());
    }
    
    /**
     * 处理运行时异常
     * @param e 运行时异常
     * @return 错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常：{}", e.getMessage(), e);
        return Result.fail(500, "系统内部错误，请稍后重试");
    }
    
    /**
     * 处理其他异常
     * @param e 异常
     * @return 错误响应
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        logger.error("未知异常：{}", e.getMessage(), e);
        return Result.fail(500, "系统内部错误，请稍后重试");
    }
}