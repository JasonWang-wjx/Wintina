package com.wintina.blog.service.post;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wintina.blog.dto.post.CreatePostDTO;
import com.wintina.blog.dto.post.PostQueryDTO;
import com.wintina.blog.dto.post.UpdatePostDTO;
import com.wintina.blog.vo.post.PostDetailVO;
import com.wintina.blog.vo.post.PostListItemVO;

public interface PostService {
    Long createPost(Long userId, CreatePostDTO dto);

    void updatePost(Long userId, UpdatePostDTO dto);

    void deletePost(Long userId, Long postId);

    PostDetailVO getPostDetail(Long postId, Long currentUserId, boolean onlyPublished);

    IPage<PostListItemVO> pagePosts(PostQueryDTO queryDTO, boolean onlyPublished);
}
