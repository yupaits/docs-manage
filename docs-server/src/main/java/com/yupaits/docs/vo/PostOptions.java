package com.yupaits.docs.vo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/17
 */
@Data
public class PostOptions implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<PostCateVO> cates = Lists.newArrayList();

    private List<PostTagCount> tags = Lists.newArrayList();
}
