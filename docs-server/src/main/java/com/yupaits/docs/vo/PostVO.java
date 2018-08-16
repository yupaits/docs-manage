package com.yupaits.docs.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Data
public class PostVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String content;

    private Long cateId;

    private List<String> tags;

    private Date createAt;

    private Date lastModifiedAt;
}
