package com.yupaits.docs.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer directoryId;
    private String name;
    private Integer sortCode;
    private String content;
}