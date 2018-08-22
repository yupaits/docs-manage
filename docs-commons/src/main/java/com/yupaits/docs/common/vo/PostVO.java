package com.yupaits.docs.common.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yupaits.docs.common.utils.serializer.LongJsonSerializer;
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

    @JsonSerialize(using = LongJsonSerializer.class)
    private Long id;

    private String title;

    private String content;

    @JsonSerialize(using = LongJsonSerializer.class)
    private Long cateId;

    private List<String> tags;

    private Date createAt;

    private Date lastModifiedAt;
}
