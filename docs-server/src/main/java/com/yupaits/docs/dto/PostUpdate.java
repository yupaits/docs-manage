package com.yupaits.docs.dto;

import com.yupaits.docs.common.utils.ValidateUtils;
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
public class PostUpdate implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String content;

    private Long cateId;

    private Boolean open;

    private List<String> tags;

    @ApiModelProperty(hidden = true)
    public boolean isValid() {
        return ValidateUtils.idValid(id) && ValidateUtils.idValid(cateId) && !StringUtils.isAnyBlank(title, content);
    }
}
