package com.yupaits.docs.entity;

import com.yupaits.docs.entity.keys.PostTagKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yupaits
 * @date 2018/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(PostTagKey.class)
@Table(name = "docs_post_tag")
public class PostTag implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    private Long postId;

    @Id
    @Column(nullable = false)
    private String tagName;
}
