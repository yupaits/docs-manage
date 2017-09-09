package com.yupaits.docs.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer ownerId;
    private String name;
    private String description;
    private String visitCode;
    private Date createdAt;
    private Date updatedAt;
}