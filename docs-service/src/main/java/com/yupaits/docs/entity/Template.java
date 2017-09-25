package com.yupaits.docs.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "docs_template")
public class Template implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "content")
    private String content;

    @Column(name = "category")
    private String category;

    @Column(name = "tags")
    private String tags;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "sort_code")
    private Integer sortCode;

    @Column(name = "visit_count")
    private Integer visitCount;

    @Column(name = "likings")
    private Integer likings;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "create_at")
    private Timestamp createdAt;

    @Column(name = "update_at")
    private Timestamp updatedAt;
}
