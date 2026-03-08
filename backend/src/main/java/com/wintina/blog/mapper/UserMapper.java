package com.wintina.blog.mapper;

/**
 * @author Jason
 * @date 2026/3/7
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wintina.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 基础CRUD由BaseMapper提供，暂不需要额外方法
}