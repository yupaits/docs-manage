package com.yupaits.docs.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Directory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer projectId;
    private Integer parentId;
    private String name;
    private Integer sortCode;
}