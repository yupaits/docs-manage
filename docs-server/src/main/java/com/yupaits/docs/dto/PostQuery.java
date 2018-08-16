package com.yupaits.docs.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Data
public class PostQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String keyword;

    private Long cateId;

    private String tagName;
}
