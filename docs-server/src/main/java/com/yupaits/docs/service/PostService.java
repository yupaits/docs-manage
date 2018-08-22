package com.yupaits.docs.service;

import com.yupaits.docs.common.dto.PostCreate;
import com.yupaits.docs.common.dto.PostQuery;
import com.yupaits.docs.common.dto.PostUpdate;
import com.yupaits.docs.common.result.Result;
import org.springframework.data.domain.Pageable;

/**
 * @author yupaits
 * @date 2018/8/16
 */
public interface PostService {

    Result getPostOptions();

    Result getPostPage(PostQuery postQuery, Pageable pageable);

    Result getPost(Long postId);

    Result addPost(PostCreate postCreate);

    Result deletePost(Long docsId);

    Result updatePost(PostUpdate postUpdate);
}
