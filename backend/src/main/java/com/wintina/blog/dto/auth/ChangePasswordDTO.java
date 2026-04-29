package com.wintina.blog.dto.auth;

/**
 * @author Jason
 * @date 2026/4/5
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * 用户修改密码DTO
 * 用于用户修改密码时传递的参数
 */
@Data
public class ChangePasswordDTO {
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度必须在6-20位之间")
    private String newPassword;
}
