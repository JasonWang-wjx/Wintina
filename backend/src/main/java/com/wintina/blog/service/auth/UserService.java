package com.wintina.blog.service.auth;


import com.wintina.blog.dto.auth.ChangePasswordDTO;
import com.wintina.blog.dto.auth.LoginDTO;
import com.wintina.blog.dto.auth.RegisterDTO;
import com.wintina.blog.vo.auth.UserInfoVO;

public interface UserService {
    /** 用户注册 */
    void register(RegisterDTO dto);

    /** 用户登录，返回用户信息（含token） */
    UserInfoVO login(LoginDTO dto);
    /** 用户修改密码 */
    void changePassword(Long userId, ChangePasswordDTO dto);

}