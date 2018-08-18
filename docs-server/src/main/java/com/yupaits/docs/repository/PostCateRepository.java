package com.yupaits.docs.repository;

import com.yupaits.docs.entity.PostCate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Repository
public interface PostCateRepository extends JpaRepository<PostCate, Long> {
}
