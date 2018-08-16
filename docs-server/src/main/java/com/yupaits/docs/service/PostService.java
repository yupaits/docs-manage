package com.yupaits.docs.service;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.dto.PostCreate;
import com.yupaits.docs.dto.PostQuery;
import com.yupaits.docs.dto.PostUpdate;
import org.springframework.data.domain.Pageable;

/**
 * @author yupaits
 * @date 2018/8/16
 */
public interface PostService {
    Result getPostPage(PostQuery postQuery, Pageable pageable);

    Result addPost(PostCreate postCreate);

    Result deletePost(Long docsId);

    Result updatePost(PostUpdate postUpdate);
}
