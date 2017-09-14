package com.yupaits.docs.rest;

import com.yupaits.docs.bean.DirectoryDTO;
import com.yupaits.docs.bean.DocumentDTO;
import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
import com.yupaits.docs.entity.Directory;
import com.yupaits.docs.entity.Document;
import com.yupaits.docs.repository.DirectoryRepository;
import com.yupaits.docs.repository.DocumentRepository;
import com.yupaits.docs.util.validate.ValidateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档目录REST接口
 * Created by yupaits on 2017/8/5.
 */
@RestController
@RequestMapping("/api/directories")
public class DirectoryController {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/projects/{projectId}")
    public Result projectDirectories(@PathVariable Integer projectId) {
        if (ValidateUtils.idInvalid(projectId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        List<Directory> directoryList = directoryRepository.findByProjectIdAndParentIdOrderBySortCodeAsc(projectId, 0);
        return Result.ok(transferDirectories(directoryList));
    }

    /**
     * 递归获取文档目录属性结构
     * @param parentId 父级目录ID
     * @param directoryDTO 文档目录DTO对象
     */
    private void getDirectoryTree(Integer parentId, DirectoryDTO directoryDTO) {
        List<Directory> subDirectories = directoryRepository.findByParentIdOrderBySortCodeAsc(parentId);
        if (CollectionUtils.isNotEmpty(subDirectories)) {
            directoryDTO.setSubDirectories(transferDirectories(subDirectories));
        }
    }

    /**
     * DirectoryList转换成DirectoryDTOList
     * @param directoryList directoryList
     * @return directoryDTOList
     */
    private List<DirectoryDTO> transferDirectories(List<Directory> directoryList) {
        return directoryList.stream().map(directory -> {
            DirectoryDTO directoryDTO = new DirectoryDTO();
            BeanUtils.copyProperties(directory, directoryDTO);
            directoryDTO.setDocuments(transferDocument(documentRepository.findByDirectoryIdOrderBySortCodeAsc(directoryDTO.getId())));
            getDirectoryTree(directoryDTO.getId(), directoryDTO);
            return directoryDTO;
        }).collect(Collectors.toList());
    }

    /**
     * DocumentList转成DocumentDTOList
     * @param documentList documentList
     * @return documentDTOList
     */
    private List<DocumentDTO> transferDocument(List<Document> documentList) {
        return documentList.stream().map(document -> {
            DocumentDTO documentDTO = new DocumentDTO();
            BeanUtils.copyProperties(document, documentDTO);
            return documentDTO;
        }).collect(Collectors.toList());
    }


    @GetMapping("/parentId/{parentId}")
    public Result getDirectoriesByParentId(@PathVariable Integer projectId, @PathVariable Integer parentId) {
        if (ValidateUtils.idInvalid(projectId) || ValidateUtils.idInvalid(parentId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        return Result.ok(directoryRepository.findByParentIdOrderBySortCodeAsc(parentId));
    }

    @GetMapping("/{directoryId}")
    public Result getDirectory(@PathVariable Integer directoryId) {
        if (ValidateUtils.idInvalid(directoryId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        return Result.ok(directoryRepository.findOne(directoryId));
    }

    @PostMapping("")
    public Result createDirectory(@RequestBody Directory directory) {
        if (directory == null || ValidateUtils.idInvalid(directory.getOwnerId()) || !ValidateUtils.isUnsignedInteger(directory.getParentId())
                || ValidateUtils.idInvalid(directory.getProjectId()) || StringUtils.isBlank(directory.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        directoryRepository.save(directory);
        return Result.ok();
    }

    @DeleteMapping("/{directoryId}")
    public Result deleteDirectory(@PathVariable Integer directoryId) {
        if (ValidateUtils.idInvalid(directoryId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        //TODO 删除之前检查该目录下是否存在子目录或文档
        directoryRepository.delete(directoryId);
        return Result.ok();
    }

    @PutMapping("/{directoryId}")
    public Result updateDirectory(@RequestBody Directory directory) {
        if (directory == null || ValidateUtils.idInvalid(directory.getId()) || ValidateUtils.idInvalid(directory.getOwnerId())
                || !ValidateUtils.isUnsignedInteger(directory.getParentId()) || ValidateUtils.idInvalid(directory.getProjectId())
                || StringUtils.isBlank(directory.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Directory directoryInDb = directoryRepository.findOne(directory.getId());
        if (directoryInDb == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        BeanUtils.copyProperties(directory, directoryInDb);
        directoryRepository.save(directory);
        return Result.ok();
    }
}
