package com.yupaits.docs.service;

import com.yupaits.docs.common.dto.PostCateCreate;
import com.yupaits.docs.common.dto.PostCateUpdate;
import com.yupaits.docs.common.result.Result;

/**
 * @author yupaits
 * @date 2018/8/16
 */
public interface PostCateService {
    Result listPostCate();

    Result addPostCate(PostCateCreate postCateCreate);

    Result updatePostCate(PostCateUpdate postCateUpdate);

    Result deletePostCate(Long cateId);
}
