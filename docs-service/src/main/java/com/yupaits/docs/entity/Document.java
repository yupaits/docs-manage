package com.yupaits.docs.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "docs_document")
public class Document implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "directory_id")
    private Integer directoryId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "sort_code")
    private Integer sortCode;

    @Column(name = "content")
    private String content;

    @Column(name = "visit_code")
    private String visitCode;
}