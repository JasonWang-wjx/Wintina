package com.wintina.blog.controller.post;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wintina.blog.common.Result;
import com.wintina.blog.dto.post.CreatePostDTO;
import com.wintina.blog.dto.post.PostQueryDTO;
import com.wintina.blog.dto.post.UpdatePostDTO;
import com.wintina.blog.security.CustomUserDetails;
import com.wintina.blog.service.post.PostService;
import com.wintina.blog.vo.post.PostDetailVO;
import com.wintina.blog.vo.post.PostListItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Result<Long> createPost(@RequestBody @Validated CreatePostDTO dto,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long postId = postService.createPost(userDetails.getUserId(), dto);
        return Result.success(200, "创建成功", postId);
    }

    @PutMapping("/{id}")
    public Result<?> updatePost(@PathVariable Long id,
                                @RequestBody @Validated UpdatePostDTO dto,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        dto.setId(id);
        postService.updatePost(userDetails.getUserId(), dto);
        return Result.success(200, "更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> deletePost(@PathVariable Long id,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(userDetails.getUserId(), id);
        return Result.success(200, "删除成功", null);
    }

    @GetMapping("/{id}")
    public Result<PostDetailVO> getPostDetail(@PathVariable Long id,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long currentUserId = userDetails != null ? userDetails.getUserId() : null;
        PostDetailVO detail = postService.getPostDetail(id, currentUserId, false);
        return Result.success(200, "获取成功", detail);
    }

    @GetMapping
    public Result<IPage<PostListItemVO>> pagePublishedPosts(PostQueryDTO queryDTO) {
        IPage<PostListItemVO> page = postService.pagePosts(queryDTO, true);
        return Result.success(200, "获取成功", page);
    }

    @GetMapping("/manage")
    public Result<IPage<PostListItemVO>> pageManagePosts(PostQueryDTO queryDTO,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        queryDTO.setUserId(userDetails.getUserId());
        IPage<PostListItemVO> page = postService.pagePosts(queryDTO, false);
        return Result.success(200, "获取成功", page);
    }
}
