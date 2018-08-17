package com.yupaits.docs.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yupaits.docs.common.utils.serializer.LongJsonSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Data
public class PostCateVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = LongJsonSerializer.class)
    private Long id;

    private String cateName;
}
