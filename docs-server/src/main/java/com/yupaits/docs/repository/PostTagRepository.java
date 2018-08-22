package com.yupaits.docs.repository;

import com.yupaits.docs.common.vo.PostTagCount;
import com.yupaits.docs.entity.PostTag;
import com.yupaits.docs.entity.keys.PostTagKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Repository
public interface PostTagRepository extends JpaRepository<PostTag, PostTagKey> {
    List<PostTag> findAllByTagName(String tagName);

    List<PostTag> findAllByPostId(Long postId);

    @Query("select tagName as tagName, count(postId) as counts from PostTag group by tagName order by counts desc")
    List<PostTagCount> findAllPostTagCount();
}
