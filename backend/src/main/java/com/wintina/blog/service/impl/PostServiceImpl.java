package com.wintina.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wintina.blog.common.enums.PostStatusEnum;
import com.wintina.blog.common.exception.AppException;
import com.wintina.blog.dto.post.CreatePostDTO;
import com.wintina.blog.dto.post.PostQueryDTO;
import com.wintina.blog.dto.post.UpdatePostDTO;
import com.wintina.blog.entity.*;
import com.wintina.blog.mapper.*;
import com.wintina.blog.service.post.PostService;
import com.wintina.blog.vo.post.PostDetailVO;
import com.wintina.blog.vo.post.PostEditorOptionsVO;
import com.wintina.blog.vo.post.PostListItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final Pattern HEADING_PATTERN = Pattern.compile("<(h[1-3])[^>]*>(.*?)</\\1>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private final PostMapper postMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final PostTagMapper postTagMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPost(Long userId, CreatePostDTO dto) {
        validateStatus(dto.getStatus());
        validateCategory(dto.getCategoryId());
        List<Long> tagIds = normalizeTagIds(dto.getTagIds());
        validateTags(tagIds);

        LocalDateTime now = LocalDateTime.now();
        Post post = new Post();
        post.setUserId(userId);
        post.setCategoryId(dto.getCategoryId());
        post.setTitle(dto.getTitle().trim());
        post.setSlug(resolveSlug(dto.getSlug(), dto.getTitle(), post.getSlug()));
        post.setSummary(trimToNull(dto.getSummary()));
        post.setContent(dto.getContent());
        post.setContentHtml(trimToNull(dto.getContentHtml()));
        post.setCoverImage(trimToNull(dto.getCoverImage()));
        post.setStatus(dto.getStatus());
        post.setAllowComment(defaultInt(dto.getAllowComment(), 1));
        post.setIsTop(defaultInt(dto.getIsTop(), 0));
        post.setIsRecommend(defaultInt(dto.getIsRecommend(), 0));
        post.setSortOrder(defaultInt(dto.getSortOrder(), 0));
        post.setSeoKeywords(trimToNull(dto.getSeoKeywords()));
        post.setSeoDescription(trimToNull(dto.getSeoDescription()));
        post.setCreateTime(now);
        post.setUpdateTime(now);
        if (PostStatusEnum.PUBLISHED.getCode().equals(dto.getStatus())) {
            post.setPublishTime(now);
        }

        postMapper.insert(post);
        savePostTags(post.getId(), tagIds, now);
        return post.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePost(Long userId, UpdatePostDTO dto) {
        validateStatus(dto.getStatus());
        validateCategory(dto.getCategoryId());
        List<Long> tagIds = normalizeTagIds(dto.getTagIds());
        validateTags(tagIds);

        Post post = postMapper.selectById(dto.getId());
        if (post == null) {
            throw new AppException(404, "文章不存在");
        }
        ensureOwnership(userId, post);

        LocalDateTime now = LocalDateTime.now();
        Integer oldStatus = post.getStatus();
        post.setCategoryId(dto.getCategoryId());
        post.setTitle(dto.getTitle().trim());
        post.setSlug(resolveSlug(dto.getSlug(), dto.getTitle(), post.getSlug()));
        post.setSummary(trimToNull(dto.getSummary()));
        post.setContent(dto.getContent());
        post.setContentHtml(trimToNull(dto.getContentHtml()));
        post.setCoverImage(trimToNull(dto.getCoverImage()));
        post.setStatus(dto.getStatus());
        post.setAllowComment(defaultInt(dto.getAllowComment(), 1));
        post.setIsTop(defaultInt(dto.getIsTop(), 0));
        post.setIsRecommend(defaultInt(dto.getIsRecommend(), 0));
        post.setSortOrder(defaultInt(dto.getSortOrder(), 0));
        post.setSeoKeywords(trimToNull(dto.getSeoKeywords()));
        post.setSeoDescription(trimToNull(dto.getSeoDescription()));
        post.setUpdateTime(now);
        if (!PostStatusEnum.PUBLISHED.getCode().equals(oldStatus)
                && PostStatusEnum.PUBLISHED.getCode().equals(dto.getStatus())) {
            post.setPublishTime(now);
        }

        postMapper.updateById(post);

        postTagMapper.delete(new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, post.getId()));
        savePostTags(post.getId(), tagIds, now);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long userId, Long postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new AppException(404, "文章不存在");
        }
        ensureOwnership(userId, post);
        postMapper.deleteById(postId);
        postTagMapper.delete(new LambdaQueryWrapper<PostTag>().eq(PostTag::getPostId, postId));
    }

    @Override
    public PostDetailVO getPostDetail(Long postId, Long currentUserId, boolean onlyPublished) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            throw new AppException(404, "文章不存在");
        }

        boolean canRead = PostStatusEnum.PUBLISHED.getCode().equals(post.getStatus())
                || (currentUserId != null && Objects.equals(currentUserId, post.getUserId()));
        if (onlyPublished && !PostStatusEnum.PUBLISHED.getCode().equals(post.getStatus())) {
            throw new AppException(404, "文章不存在");
        }
        if (!onlyPublished && !canRead) {
            throw new AppException(403, "无权限查看该文章");
        }

        PostDetailVO vo = toDetailVO(post);
        fillMeta(Collections.singletonList(vo));
        return vo;
    }

    @Override
    public IPage<PostListItemVO> pagePosts(PostQueryDTO queryDTO, boolean onlyPublished) {
        long current = queryDTO.getCurrent() == null || queryDTO.getCurrent() < 1 ? 1 : queryDTO.getCurrent();
        long size = queryDTO.getSize() == null || queryDTO.getSize() < 1 ? 10 : queryDTO.getSize();

        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        if (onlyPublished) {
            wrapper.eq(Post::getStatus, PostStatusEnum.PUBLISHED.getCode());
        } else {
            if (queryDTO.getUserId() != null) {
                wrapper.eq(Post::getUserId, queryDTO.getUserId());
            }
            if (queryDTO.getStatus() != null) {
                validateStatus(queryDTO.getStatus());
                wrapper.eq(Post::getStatus, queryDTO.getStatus());
            }
        }

        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(Post::getCategoryId, queryDTO.getCategoryId());
        }
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            String keyword = queryDTO.getKeyword().trim();
            wrapper.and(w -> w.like(Post::getTitle, keyword).or().like(Post::getSummary, keyword));
        }

        if (queryDTO.getTagId() != null) {
            List<Long> postIds = postTagMapper.selectList(
                    new LambdaQueryWrapper<PostTag>().eq(PostTag::getTagId, queryDTO.getTagId())
            ).stream().map(PostTag::getPostId).distinct().toList();
            if (postIds.isEmpty()) {
                return new Page<>(current, size);
            }
            wrapper.in(Post::getId, postIds);
        }

        wrapper.orderByDesc(Post::getIsTop)
                .orderByDesc(Post::getPublishTime)
                .orderByDesc(Post::getCreateTime);

        Page<Post> page = new Page<>(current, size);
        IPage<Post> postPage = postMapper.selectPage(page, wrapper);

        List<PostListItemVO> records = postPage.getRecords().stream().map(this::toListVO).collect(Collectors.toList());
        fillMeta(records);

        Page<PostListItemVO> result = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        result.setRecords(records);
        return result;
    }

    @Override
    public PostEditorOptionsVO getEditorOptions() {
        PostEditorOptionsVO options = new PostEditorOptionsVO();

        List<PostEditorOptionsVO.CategoryOption> categories = categoryMapper.selectList(
                        new LambdaQueryWrapper<Category>()
                                .eq(Category::getStatus, 1)
                                .orderByAsc(Category::getSortOrder)
                                .orderByAsc(Category::getId)
                ).stream()
                .map(category -> {
                    PostEditorOptionsVO.CategoryOption option = new PostEditorOptionsVO.CategoryOption();
                    option.setId(category.getId());
                    option.setName(category.getName());
                    return option;
                })
                .toList();

        List<PostEditorOptionsVO.TagOption> tags = tagMapper.selectList(
                        new LambdaQueryWrapper<Tag>().orderByAsc(Tag::getName)
                ).stream()
                .map(tag -> {
                    PostEditorOptionsVO.TagOption option = new PostEditorOptionsVO.TagOption();
                    option.setId(tag.getId());
                    option.setName(tag.getName());
                    option.setColor(tag.getColor());
                    return option;
                })
                .toList();

        List<PostEditorOptionsVO.StatusOption> statusOptions = Arrays.stream(PostStatusEnum.values())
                .map(status -> {
                    PostEditorOptionsVO.StatusOption option = new PostEditorOptionsVO.StatusOption();
                    option.setValue(status.getCode());
                    option.setLabel(status.getName());
                    return option;
                })
                .toList();

        options.setCategories(categories);
        options.setTags(tags);
        options.setStatusOptions(statusOptions);
        return options;
    }

    private void ensureOwnership(Long userId, Post post) {
        if (!Objects.equals(userId, post.getUserId())) {
            throw new AppException(403, "无权限操作该文章");
        }
    }

    private void validateStatus(Integer status) {
        if (PostStatusEnum.getByCode(status) == null) {
            throw new AppException(400, "文章状态不合法");
        }
    }

    private void validateCategory(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null || !Objects.equals(category.getStatus(), 1)) {
            throw new AppException(400, "分类不存在或不可用");
        }
    }

    private void validateTags(List<Long> tagIds) {
        if (tagIds.isEmpty()) {
            return;
        }
        Long count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>().in(Tag::getId, tagIds));
        if (count == null || count.intValue() != tagIds.size()) {
            throw new AppException(400, "标签不存在或不可用");
        }
    }

    private List<Long> normalizeTagIds(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        return tagIds.stream().filter(Objects::nonNull).distinct().toList();
    }

    private void savePostTags(Long postId, List<Long> tagIds, LocalDateTime now) {
        for (Long tagId : tagIds) {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            postTag.setCreateTime(now);
            postTagMapper.insert(postTag);
        }
    }

    private PostListItemVO toListVO(Post post) {
        PostListItemVO vo = new PostListItemVO();
        vo.setId(post.getId());
        vo.setTitle(post.getTitle());
        vo.setSlug(post.getSlug());
        vo.setSummary(post.getSummary());
        vo.setCoverImage(post.getCoverImage());
        vo.setStatus(post.getStatus());
        vo.setViewCount(post.getViewCount());
        vo.setLikeCount(post.getLikeCount());
        vo.setCommentCount(post.getCommentCount());
        vo.setReadTimeMinutes(calculateReadTimeMinutes(post.getContent()));
        vo.setUserId(post.getUserId());
        vo.setCategoryId(post.getCategoryId());
        vo.setPublishTime(post.getPublishTime());
        vo.setCreateTime(post.getCreateTime());
        vo.setUpdateTime(post.getUpdateTime());
        return vo;
    }

    private PostDetailVO toDetailVO(Post post) {
        PostDetailVO vo = new PostDetailVO();
        vo.setId(post.getId());
        vo.setTitle(post.getTitle());
        vo.setSlug(post.getSlug());
        vo.setSummary(post.getSummary());
        vo.setContent(post.getContent());
        vo.setContentHtml(post.getContentHtml());
        vo.setCoverImage(post.getCoverImage());
        vo.setStatus(post.getStatus());
        vo.setAllowComment(post.getAllowComment());
        vo.setIsTop(post.getIsTop());
        vo.setIsRecommend(post.getIsRecommend());
        vo.setViewCount(post.getViewCount());
        vo.setLikeCount(post.getLikeCount());
        vo.setCommentCount(post.getCommentCount());
        vo.setCollectCount(post.getCollectCount());
        vo.setSortOrder(post.getSortOrder());
        vo.setSeoKeywords(post.getSeoKeywords());
        vo.setSeoDescription(post.getSeoDescription());
        vo.setReadTimeMinutes(calculateReadTimeMinutes(post.getContent()));
        vo.setToc(extractToc(post.getContentHtml(), post.getContent()));
        vo.setRelatedPosts(buildRelatedPosts(post));
        vo.setUserId(post.getUserId());
        vo.setCategoryId(post.getCategoryId());
        vo.setPublishTime(post.getPublishTime());
        vo.setCreateTime(post.getCreateTime());
        vo.setUpdateTime(post.getUpdateTime());
        return vo;
    }

    private void fillMeta(List<?> voList) {
        if (voList == null || voList.isEmpty()) {
            return;
        }

        Set<Long> userIds = new HashSet<>();
        Set<Long> categoryIds = new HashSet<>();
        Set<Long> postIds = new HashSet<>();

        for (Object vo : voList) {
            if (vo instanceof PostListItemVO) {
                PostListItemVO listItem = (PostListItemVO) vo;
                userIds.add(listItem.getUserId());
                categoryIds.add(listItem.getCategoryId());
                postIds.add(listItem.getId());
            }
            if (vo instanceof PostDetailVO) {
                PostDetailVO detailVO = (PostDetailVO) vo;
                userIds.add(detailVO.getUserId());
                categoryIds.add(detailVO.getCategoryId());
                postIds.add(detailVO.getId());
            }
        }

        Map<Long, User> userMap = userIds.isEmpty() ? Collections.emptyMap() :
                userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getUserId, userIds)).stream()
                        .collect(Collectors.toMap(User::getUserId, user -> user, (a, b) -> a));

        Map<Long, Integer> authorPostCountMap = userIds.isEmpty() ? Collections.emptyMap() :
                postMapper.selectList(new LambdaQueryWrapper<Post>()
                                .in(Post::getUserId, userIds)
                                .eq(Post::getStatus, PostStatusEnum.PUBLISHED.getCode()))
                        .stream()
                        .collect(Collectors.groupingBy(Post::getUserId, Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));

        Map<Long, String> categoryNameMap = categoryIds.isEmpty() ? Collections.emptyMap() :
                categoryMapper.selectList(new LambdaQueryWrapper<Category>().in(Category::getId, categoryIds)).stream()
                        .collect(Collectors.toMap(Category::getId, Category::getName, (a, b) -> a));

        Map<Long, List<PostTag>> postTagMap = postIds.isEmpty() ? Collections.emptyMap() :
                postTagMapper.selectList(new LambdaQueryWrapper<PostTag>().in(PostTag::getPostId, postIds)).stream()
                        .collect(Collectors.groupingBy(PostTag::getPostId));

        Set<Long> tagIds = postTagMap.values().stream()
                .flatMap(List::stream)
                .map(PostTag::getTagId)
                .collect(Collectors.toSet());

        Map<Long, String> tagNameMap = tagIds.isEmpty() ? Collections.emptyMap() :
                tagMapper.selectList(new LambdaQueryWrapper<Tag>().in(Tag::getId, tagIds)).stream()
                        .collect(Collectors.toMap(Tag::getId, Tag::getName, (a, b) -> a));

        for (Object vo : voList) {
            if (vo instanceof PostListItemVO) {
                PostListItemVO listItem = (PostListItemVO) vo;
                User author = userMap.get(listItem.getUserId());
                listItem.setAuthorName(resolveDisplayName(author));
                listItem.setAuthorAvatar(author != null ? author.getAvatar() : null);
                listItem.setCategoryName(categoryNameMap.get(listItem.getCategoryId()));
                List<PostTag> relations = postTagMap.getOrDefault(listItem.getId(), Collections.emptyList());
                listItem.setTagIds(relations.stream().map(PostTag::getTagId).toList());
                listItem.setTagNames(relations.stream().map(PostTag::getTagId).map(tagNameMap::get)
                        .filter(Objects::nonNull).toList());
            }
            if (vo instanceof PostDetailVO) {
                PostDetailVO detailVO = (PostDetailVO) vo;
                User author = userMap.get(detailVO.getUserId());
                detailVO.setAuthorName(resolveDisplayName(author));
                detailVO.setAuthorAvatar(author != null ? author.getAvatar() : null);
                detailVO.setAuthorBio(author != null ? author.getIntro() : null);
                detailVO.setAuthorPostCount(authorPostCountMap.getOrDefault(detailVO.getUserId(), 0));
                detailVO.setCategoryName(categoryNameMap.get(detailVO.getCategoryId()));
                List<PostTag> relations = postTagMap.getOrDefault(detailVO.getId(), Collections.emptyList());
                detailVO.setTagIds(relations.stream().map(PostTag::getTagId).toList());
                detailVO.setTagNames(relations.stream().map(PostTag::getTagId).map(tagNameMap::get)
                        .filter(Objects::nonNull).toList());
            }
        }
    }

    private Integer defaultInt(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }

    private String resolveDisplayName(User user) {
        if (user == null) {
            return null;
        }
        if (StringUtils.hasText(user.getNickname())) {
            return user.getNickname().trim();
        }
        return user.getUsername();
    }

    private Integer calculateReadTimeMinutes(String content) {
        if (!StringUtils.hasText(content)) {
            return 1;
        }
        int length = content.replaceAll("\\s+", "").length();
        return Math.max(1, (int) Math.ceil(length / 320.0));
    }

    private List<PostDetailVO.TocItem> extractToc(String contentHtml, String markdownContent) {
        String source = StringUtils.hasText(contentHtml) ? contentHtml : markdownContent;
        if (!StringUtils.hasText(source)) {
            return Collections.emptyList();
        }

        Matcher matcher = HEADING_PATTERN.matcher(source);
        List<PostDetailVO.TocItem> items = new ArrayList<>();
        int index = 1;
        while (matcher.find()) {
            String tag = matcher.group(1).toLowerCase(Locale.ROOT);
            String raw = matcher.group(2).replaceAll("<[^>]+>", "").trim();
            if (!StringUtils.hasText(raw)) {
                continue;
            }
            PostDetailVO.TocItem item = new PostDetailVO.TocItem();
            item.setId("heading-" + index++);
            item.setText(raw);
            item.setLevel(Integer.parseInt(tag.substring(1)));
            items.add(item);
            if (items.size() >= 12) {
                break;
            }
        }
        return items;
    }

    private List<PostDetailVO.RelatedPostItem> buildRelatedPosts(Post post) {
        List<Long> currentTagIds = postTagMapper.selectList(new LambdaQueryWrapper<PostTag>()
                        .eq(PostTag::getPostId, post.getId()))
                .stream().map(PostTag::getTagId).distinct().toList();

        LinkedHashSet<Long> relatedIds = new LinkedHashSet<>();
        if (!currentTagIds.isEmpty()) {
            List<Long> byTags = postTagMapper.selectList(new LambdaQueryWrapper<PostTag>()
                            .in(PostTag::getTagId, currentTagIds))
                    .stream()
                    .map(PostTag::getPostId)
                    .filter(id -> !Objects.equals(id, post.getId()))
                    .distinct()
                    .toList();
            relatedIds.addAll(byTags);
        }

        if (relatedIds.size() < 4 && post.getCategoryId() != null) {
            List<Long> byCategory = postMapper.selectList(new LambdaQueryWrapper<Post>()
                            .eq(Post::getCategoryId, post.getCategoryId())
                            .eq(Post::getStatus, PostStatusEnum.PUBLISHED.getCode())
                            .ne(Post::getId, post.getId())
                            .orderByDesc(Post::getPublishTime)
                            .last("limit 8"))
                    .stream().map(Post::getId).toList();
            relatedIds.addAll(byCategory);
        }

        if (relatedIds.size() < 4) {
            List<Long> latest = postMapper.selectList(new LambdaQueryWrapper<Post>()
                            .eq(Post::getStatus, PostStatusEnum.PUBLISHED.getCode())
                            .ne(Post::getId, post.getId())
                            .orderByDesc(Post::getPublishTime)
                            .orderByDesc(Post::getCreateTime)
                            .last("limit 8"))
                    .stream().map(Post::getId).toList();
            relatedIds.addAll(latest);
        }

        if (relatedIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> selectedIds = relatedIds.stream().limit(4).toList();
        Map<Long, Post> postMap = postMapper.selectBatchIds(selectedIds).stream()
                .collect(Collectors.toMap(Post::getId, item -> item, (a, b) -> a));

        List<PostDetailVO.RelatedPostItem> relatedPosts = new ArrayList<>();
        for (Long id : selectedIds) {
            Post related = postMap.get(id);
            if (related == null) {
                continue;
            }
            PostDetailVO.RelatedPostItem item = new PostDetailVO.RelatedPostItem();
            item.setId(related.getId());
            item.setTitle(related.getTitle());
            item.setCoverImage(related.getCoverImage());
            item.setPublishTime(related.getPublishTime());
            item.setViewCount(related.getViewCount());
            relatedPosts.add(item);
        }
        return relatedPosts;
    }

    private String resolveSlug(String slug, String title, String fallbackSlug) {
        String normalizedSlug = normalizeSlug(slug);
        if (normalizedSlug != null) {
            return normalizedSlug;
        }
        String titleSlug = normalizeSlug(title);
        if (titleSlug != null) {
            return titleSlug;
        }
        String oldSlug = normalizeSlug(fallbackSlug);
        if (oldSlug != null) {
            return oldSlug;
        }
        return "post-" + System.currentTimeMillis();
    }

    private String normalizeSlug(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String normalized = value.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\u4e00-\\u9fa5\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        return StringUtils.hasText(normalized) ? normalized : null;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
