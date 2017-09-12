package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
import com.yupaits.docs.mapper.DirectoryMapper;
import com.yupaits.docs.model.Directory;
import com.yupaits.docs.util.validate.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文档目录REST接口
 * Created by yupaits on 2017/8/5.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/directories")
public class DirectoryController {

    @Autowired
    private DirectoryMapper directoryMapper;

    @GetMapping("")
    public Result projectDirectories(@PathVariable Integer projectId) {
        if (ValidateUtils.idInvalid(projectId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Directory directory = new Directory();
        directory.setProjectId(projectId);
        directory.setParentId(0);
        return Result.ok(directoryMapper.selectBySelective(directory));
    }

    @GetMapping("/parentId/{parentId}")
    public Result getDirectoriesByParentId(@PathVariable Integer projectId, @PathVariable Integer parentId) {
        if (ValidateUtils.idInvalid(projectId) || ValidateUtils.idInvalid(parentId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Directory directory = new Directory();
        directory.setProjectId(projectId);
        directory.setParentId(parentId);
        return Result.ok(directoryMapper.selectBySelective(directory));
    }

    @PostMapping("")
    public Result createDirectory(@RequestBody Directory directory) {
        if (directory == null|| ValidateUtils.idInvalid(directory.getParentId())
                || ValidateUtils.idInvalid(directory.getProjectId()) || StringUtils.isBlank(directory.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        directoryMapper.insertSelective(directory);
        return Result.ok();
    }

    @DeleteMapping("/{directoryId}")
    public Result deleteDirectory(@PathVariable Integer directoryId) {
        if (ValidateUtils.idInvalid(directoryId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        directoryMapper.deleteByPrimaryKey(directoryId);
        return Result.ok();
    }

    @PutMapping("/{directoryId}")
    public Result updateDirectory(@RequestBody Directory directory) {
        if (directory == null || ValidateUtils.idInvalid(directory.getId()) || ValidateUtils.idInvalid(directory.getParentId())
                || ValidateUtils.idInvalid(directory.getProjectId()) || StringUtils.isBlank(directory.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        directoryMapper.updateByPrimaryKeySelective(directory);
        return Result.ok();
    }
}
