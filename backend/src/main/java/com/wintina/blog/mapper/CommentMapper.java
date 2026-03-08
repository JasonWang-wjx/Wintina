package com.wintina.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wintina.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Jason
 * @date 2026/3/8
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}