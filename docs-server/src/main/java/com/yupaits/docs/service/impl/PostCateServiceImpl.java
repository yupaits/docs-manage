package com.yupaits.docs.service.impl;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.common.utils.ValidateUtils;
import com.yupaits.docs.dto.PostCateCreate;
import com.yupaits.docs.dto.PostCateUpdate;
import com.yupaits.docs.entity.PostCate;
import com.yupaits.docs.repository.PostCateRepository;
import com.yupaits.docs.repository.PostRepository;
import com.yupaits.docs.service.PostCateService;
import com.yupaits.docs.vo.PostCateVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Service
public class PostCateServiceImpl implements PostCateService {

    private final PostRepository postRepository;
    private final PostCateRepository postCateRepository;

    @Autowired
    public PostCateServiceImpl(PostRepository postRepository, PostCateRepository postCateRepository) {
        this.postRepository = postRepository;
        this.postCateRepository = postCateRepository;
    }

    @Override
    public Result listDocsCate() {
        return Result.ok(postCateRepository.findAll().stream().map(postCate -> {
            PostCateVO postCateVO = new PostCateVO();
            BeanUtils.copyProperties(postCate, postCateVO);
            postCateVO.setId(postCate.getId());
            return postCateVO;
        }).collect(Collectors.toList()));
    }

    @Override
    public Result addDocsCate(PostCateCreate postCateCreate) {
        if (!postCateCreate.isValid()) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        PostCate postCate = new PostCate();
        BeanUtils.copyProperties(postCateCreate, postCate);
        postCateRepository.save(postCate);
        return Result.ok();
    }

    @Override
    public Result updateDocsCate(PostCateUpdate postCateUpdate) {
        if (!postCateUpdate.isValid()) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        PostCate postCate = postCateRepository.findOne(postCateUpdate.getId());
        if (postCate == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        BeanUtils.copyProperties(postCateUpdate, postCate);
        postCateRepository.save(postCate);
        return Result.ok();
    }

    @Override
    public Result deleteDocsCate(Long cateId) {
        if (!ValidateUtils.idValid(cateId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        PostCate postCate = postCateRepository.findOne(cateId);
        if (postCate == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        if (postRepository.countAllByCateId(cateId) > 0) {
            return Result.fail(ResultCode.DATA_CANNOT_DELETE);
        }
        postCateRepository.delete(cateId);
        return Result.ok();
    }
}
