package com.yupaits.docs.common.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Lists;
import com.yupaits.docs.common.utils.ValidateUtils;
import com.yupaits.docs.common.utils.serializer.LongJsonDeserializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreate implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;

    private String content;

    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long cateId;

    private Boolean open;

    private List<String> tags = Lists.newArrayList();

    @ApiModelProperty(hidden = true)
    public boolean isValid() {
        return ValidateUtils.idValid(cateId) && !StringUtils.isAnyBlank(title, content);
    }
}
