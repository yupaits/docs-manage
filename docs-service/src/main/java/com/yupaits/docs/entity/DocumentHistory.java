package com.yupaits.docs.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "docs_document_history")
public class DocumentHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "saved_time")
    private Timestamp savedTime;

    @Column(name = "content")
    private String content;
}