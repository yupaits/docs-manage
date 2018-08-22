package com.yupaits.docs.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yupaits.docs.common.utils.serializer.LongJsonDeserializer;
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

    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long cateId;

    private String tagName;
}
