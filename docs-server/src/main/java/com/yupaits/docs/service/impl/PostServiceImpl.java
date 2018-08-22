package com.yupaits.docs.service.impl;

import com.yupaits.docs.common.constants.DocsConsts;
import com.yupaits.docs.common.dto.PostCreate;
import com.yupaits.docs.common.dto.PostQuery;
import com.yupaits.docs.common.dto.PostUpdate;
import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.common.utils.ValidateUtils;
import com.yupaits.docs.common.vo.PostCateVO;
import com.yupaits.docs.common.vo.PostOptions;
import com.yupaits.docs.common.vo.PostTagCount;
import com.yupaits.docs.common.vo.PostVO;
import com.yupaits.docs.common.vo.impl.PostTagCountImpl;
import com.yupaits.docs.entity.Post;
import com.yupaits.docs.entity.PostTag;
import com.yupaits.docs.repository.PostRepository;
import com.yupaits.docs.repository.PostTagRepository;
import com.yupaits.docs.service.PostCateService;
import com.yupaits.docs.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final RedisTemplate redisTemplate;
    private final PostCateService postCateService;
    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostTagRepository postTagRepository,
                           RedisTemplate redisTemplate, PostCateService postCateService) {
        this.postRepository = postRepository;
        this.postCateService = postCateService;
        this.postTagRepository = postTagRepository;
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Result getPostOptions() {
        PostOptions postOptions = new PostOptions();
        Result catesResult = postCateService.listPostCate();
        if (catesResult.getCode() == ResultCode.OK.getCode()) {
            postOptions.setCates((List<PostCateVO>) catesResult.getData());
        }
        List<PostTagCount> tags = (List<PostTagCount>) redisTemplate.opsForValue().get(DocsConsts.POST_TAG_SET);
        if (tags == null) {
            tags = postTagRepository.findAllPostTagCount();
            List<PostTagCountImpl> tagCountList = tags.stream()
                    .map(postTagCount -> new PostTagCountImpl(postTagCount.getTagName(), postTagCount.getCounts()))
                    .collect(Collectors.toList());
            redisTemplate.opsForValue().set(DocsConsts.POST_TAG_SET, tagCountList);
        }
        postOptions.setTags(tags);
        return Result.ok(postOptions);
    }

    @Override
    public Result getPostPage(PostQuery postQuery, Pageable pageable) {
        Page<Post> postPage;
        if (StringUtils.isBlank(postQuery.getKeyword())) {
            postQuery.setKeyword("%%");
        } else {
            postQuery.setKeyword("%" + postQuery.getKeyword() + "%");
        }
        if (StringUtils.isNotBlank(postQuery.getTagName())) {
            List<Long> postIds = postTagRepository.findAllByTagName(postQuery.getTagName())
                    .stream().map(PostTag::getPostId).collect(Collectors.toList());
            if (ValidateUtils.idValid(postQuery.getCateId())) {
                postPage = postRepository.findAllByTitleLikeAndCateIdAndIdIsIn(postQuery.getKeyword(), postQuery.getCateId(), postIds, pageable);
            } else {
                postPage = postRepository.findAllByTitleLikeAndIdIsIn(postQuery.getKeyword(), postIds, pageable);
            }
        } else if (ValidateUtils.idValid(postQuery.getCateId())) {
            postPage = postRepository.findAllByTitleLikeAndCateId(postQuery.getKeyword(), postQuery.getCateId(), pageable);
        } else {
            postPage = postRepository.findAllByTitleLike(postQuery.getKeyword(), pageable);
        }
        Page<PostVO> postVOPage = postPage == null ? null : postPage.map(post -> {
            PostVO postVO = new PostVO();
            BeanUtils.copyProperties(post, postVO);
            postVO.setId(post.getId());
            postVO.setCreateAt(post.getCreatedAt());
            postVO.setLastModifiedAt(post.getLastModifiedAt());
            postVO.setTags(postTagRepository.findAllByPostId(post.getId()).stream().map(PostTag::getTagName).collect(Collectors.toList()));
            return postVO;
        });
        return Result.ok(postVOPage);
    }

    @Override
    public Result getPost(Long postId) {
        if (!ValidateUtils.idValid(postId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Post post = postRepository.findOne(postId);
        if (post == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post, postVO);
        postVO.setId(post.getId());
        postVO.setCreateAt(post.getCreatedAt());
        postVO.setLastModifiedAt(post.getLastModifiedAt());
        postVO.setTags(postTagRepository.findAllByPostId(post.getId()).stream().map(PostTag::getTagName).collect(Collectors.toList()));
        return Result.ok(postVO);
    }

    @Override
    public Result addPost(PostCreate postCreate) {
        if (!postCreate.isValid()) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Post post = new Post();
        BeanUtils.copyProperties(postCreate, post);
        Post flushedPost = postRepository.save(post);
        if (CollectionUtils.isNotEmpty(postCreate.getTags())) {
            List<PostTag> postTagList = postCreate.getTags().stream().map(tag -> {
                PostTag postTag = new PostTag();
                postTag.setPostId(flushedPost.getId());
                postTag.setTagName(tag);
                return postTag;
            }).collect(Collectors.toList());
            postTagRepository.save(postTagList);
            //noinspection unchecked
            redisTemplate.delete(DocsConsts.POST_TAG_SET);
        }
        return Result.ok();
    }

    @Override
    public Result deletePost(Long postId) {
        if (!ValidateUtils.idValid(postId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Post post = postRepository.findOne(postId);
        if (post == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        postRepository.delete(postId);
        postTagRepository.delete(postTagRepository.findAllByPostId(postId));
        //noinspection unchecked
        redisTemplate.delete(DocsConsts.POST_TAG_SET);
        return Result.ok();
    }

    @Override
    public Result updatePost(PostUpdate postUpdate) {
        if (!postUpdate.isValid()) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Post post = postRepository.findOne(postUpdate.getId());
        if (post == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        BeanUtils.copyProperties(postUpdate, post);
        List<PostTag> dbPostTagList = postTagRepository.findAllByPostId(postUpdate.getId());
        List<PostTag> postTagList = postUpdate.getTags().stream().map(tag -> new PostTag(postUpdate.getId(), tag)).collect(Collectors.toList());
        List<PostTag> removePostTagList = ListUtils.subtract(dbPostTagList, postTagList);
        List<PostTag> deltaPostTagList = ListUtils.subtract(postTagList, dbPostTagList);
        boolean removeListNotEmpty = CollectionUtils.isNotEmpty(removePostTagList);
        boolean deltaListNotEmpty = CollectionUtils.isNotEmpty(deltaPostTagList);
        if (removeListNotEmpty) {
            //删除更新后无用的文章标签
            postTagRepository.delete(removePostTagList);
        }
        if (deltaListNotEmpty) {
            //增量更新文章标签
            postTagRepository.save(deltaPostTagList);
        }
        postRepository.save(post);
        if (removeListNotEmpty || deltaListNotEmpty) {
            //noinspection unchecked
            redisTemplate.delete(DocsConsts.POST_TAG_SET);
        }
        return Result.ok();
    }
}
