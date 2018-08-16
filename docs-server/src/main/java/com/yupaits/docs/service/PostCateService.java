package com.yupaits.docs.service;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.dto.PostCateCreate;
import com.yupaits.docs.dto.PostCateUpdate;

/**
 * @author yupaits
 * @date 2018/8/16
 */
public interface PostCateService {
    Result listDocsCate();

    Result addDocsCate(PostCateCreate postCateCreate);

    Result updateDocsCate(PostCateUpdate postCateUpdate);

    Result deleteDocsCate(Long cateId);
}
