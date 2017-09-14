package com.yupaits.docs.bean;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 文档目录DTO
 * Created by yupaits on 2017/9/14.
 */
@Data
public class DirectoryDTO {
    private Integer id;
    private String name;
    private Integer sortCode;
    private List<DirectoryDTO> subDirectories = new LinkedList<>();
    private List<DocumentDTO> documents = new LinkedList<>();
}
