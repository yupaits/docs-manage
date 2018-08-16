package com.yupaits.docs.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Data
public class PostCateVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String cateName;
}
