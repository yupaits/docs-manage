package com.yupaits.docs.vo.impl;

import com.yupaits.docs.vo.PostTagCount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yupaits
 * @date 2018/8/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTagCountImpl implements PostTagCount {
    private static final long serialVersionUID = 1L;

    private String tagName;

    private Integer counts;
}
