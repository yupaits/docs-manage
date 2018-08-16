package com.yupaits.docs.repository;

import com.yupaits.docs.entity.PostTag;
import com.yupaits.docs.entity.keys.PostTagKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostTagKey> {
    List<PostTag> findAllByTagName(String tagName);

    List<PostTag> findAllByPostId(Long postId);
}
