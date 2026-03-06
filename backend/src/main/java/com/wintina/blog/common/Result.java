package com.wintina.blog.common;

import lombok.Data;

/**
 * @author Jason
 * @description 通用响应体
 * @date 2026-03-01
 * @version 1.0
 */

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

//    /**
//     * 成功响应方法
//     * @param data 响应数据
//     * @return 成功响应结果
//     */
//    public static <T> Result<T> success(T data) {
//        Result<T> result = new Result<>();
//        result.setCode(200);
//        result.setMessage("操作成功");
//        result.setData(data);
//        return result;
//    }
//
//    /**
//     * 失败响应方法
//     * @param message 错误信息
//     * @return 失败响应结果
//     */
//    public static <T> Result<T> fail(String message) {
//        Result<T> result = new Result<>();
//        result.setCode(400);
//        result.setMessage(message);
//        result.setData(null);
//        return result;
//    }


    /**
     * 重载：自定义状态码和消息的成功响应
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     * @return 成功响应结果
     */
    public static <T> Result<T> success(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 重载：自定义状态码的失败响应
     * @param code 状态码
     * @param message 消息
     * @return 失败响应结果
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}