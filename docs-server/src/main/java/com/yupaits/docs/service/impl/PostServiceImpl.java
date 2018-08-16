package com.yupaits.docs.service.impl;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.common.utils.ValidateUtils;
import com.yupaits.docs.dto.PostCreate;
import com.yupaits.docs.dto.PostQuery;
import com.yupaits.docs.dto.PostUpdate;
import com.yupaits.docs.entity.Post;
import com.yupaits.docs.entity.PostTag;
import com.yupaits.docs.repository.PostRepository;
import com.yupaits.docs.repository.PostTagRepository;
import com.yupaits.docs.service.PostService;
import com.yupaits.docs.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PostTagRepository postTagRepository) {
        this.postRepository = postRepository;
        this.postTagRepository = postTagRepository;
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
    public Result addPost(PostCreate postCreate) {
        if (!postCreate.isValid()) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Post post = new Post();
        BeanUtils.copyProperties(postCreate, post);
        Post flushedPost = postRepository.save(post);
        List<PostTag> postTagList = postCreate.getTags().stream().map(tag -> {
            PostTag postTag = new PostTag();
            postTag.setPostId(flushedPost.getId());
            postTag.setTagName(tag);
            return postTag;
        }).collect(Collectors.toList());
        postTagRepository.save(postTagList);
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
        //删除更新后无用的文章标签
        postTagRepository.delete(removePostTagList);
        //增量更新文章标签
        postTagRepository.save(deltaPostTagList);
        postRepository.save(post);
        return Result.ok();
    }
}
