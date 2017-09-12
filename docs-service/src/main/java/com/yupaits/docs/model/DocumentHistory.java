package com.yupaits.docs.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DocumentHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer documentId;
    private Date savedTime;
    private String content;
}