package com.wintina.blog.controller.auth;

import com.wintina.blog.common.Result;
import com.wintina.blog.dto.auth.*;
import com.wintina.blog.dto.auth.LoginDTO;
import com.wintina.blog.dto.auth.RegisterDTO;
import com.wintina.blog.service.auth.UserService;
import com.wintina.blog.vo.auth.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /** 注册接口：POST /api/auth/register */
    @PostMapping("/register")
    public Result<?> register(@RequestBody @Validated RegisterDTO dto) {
        userService.register(dto);
        return Result.success(200, "注册成功", null);
    }

    /** 登录接口：POST /api/auth/login */
    @PostMapping("/login")
    public Result<UserInfoVO> login(@RequestBody @Validated LoginDTO dto) {
        UserInfoVO vo = userService.login(dto);
        return Result.success(200, "登录成功", vo);
    }
}