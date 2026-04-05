package com.wintina.blog.vo.auth;

import lombok.Data;

@Data
public class UserInfoVO {
    private Long   userId;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String token;      // 登录成功后附带token
}