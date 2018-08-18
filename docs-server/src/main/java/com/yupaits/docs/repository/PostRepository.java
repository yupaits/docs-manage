package com.yupaits.docs.repository;

import com.yupaits.docs.common.base.BaseRepository;
import com.yupaits.docs.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Repository
public interface PostRepository extends BaseRepository<Post, Long> {

    Page<Post> findAllByTitleLikeAndCateIdAndIdIsIn(String title, Long cateId, List<Long> postIds, Pageable pageable);

    Page<Post> findAllByTitleLikeAndIdIsIn(String title, List<Long> postIds, Pageable pageable);

    Page<Post> findAllByTitleLikeAndCateId(String title, Long cateId, Pageable pageable);

    Page<Post> findAllByTitleLike(String title, Pageable pageable);

    long countAllByCateId(Long cateId);
}
