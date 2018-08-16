package com.yupaits.docs.entity;

import com.yupaits.docs.common.base.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yupaits
 * @date 2018/8/12
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "docs_post_cate")
public class PostCate extends BaseEntity<Long> {
    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    private String cateName;
}
