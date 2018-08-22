package com.yupaits.docs.common.vo;

import java.io.Serializable;

/**
 * @author yupaits
 * @date 2018/8/17
 */
public interface PostTagCount extends Serializable {

    String getTagName();

    Integer getCounts();
}
