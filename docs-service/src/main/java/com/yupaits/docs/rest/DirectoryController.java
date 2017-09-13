package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
import com.yupaits.docs.entity.Directory;
import com.yupaits.docs.repository.DirectoryRepository;
import com.yupaits.docs.util.validate.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文档目录REST接口
 * Created by yupaits on 2017/8/5.
 */
@RestController
@RequestMapping("/api/directories")
public class DirectoryController {

    @Autowired
    private DirectoryRepository directoryRepository;

    @GetMapping("/projects/{projectId}")
    public Result projectDirectories(@PathVariable Integer projectId) {
        if (ValidateUtils.idInvalid(projectId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        return Result.ok(directoryRepository.findByProjectIdAndParentId(projectId, 0));
    }

    @GetMapping("/parentId/{parentId}")
    public Result getDirectoriesByParentId(@PathVariable Integer projectId, @PathVariable Integer parentId) {
        if (ValidateUtils.idInvalid(projectId) || ValidateUtils.idInvalid(parentId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        return Result.ok(directoryRepository.findByProjectIdAndParentId(projectId, parentId));
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
