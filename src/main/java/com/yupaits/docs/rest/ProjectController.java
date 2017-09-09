package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
import com.yupaits.docs.mapper.ProjectMapper;
import com.yupaits.docs.model.Project;
import com.yupaits.docs.util.validate.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目REST接口
 * Created by yupaits on 2017/8/4.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectMapper projectMapper;

    @GetMapping("/owner/{ownerId}")
    public Result projects(@PathVariable Integer ownerId) {
        if (ValidateUtils.idInvalid(ownerId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Project project = new Project();
        project.setOwnerId(ownerId);
        return Result.ok(projectMapper.selectBySelective(project));
    }

    @PostMapping("")
    public Result createProject(@RequestBody Project project) {
        if (project == null || ValidateUtils.idInvalid(project.getOwnerId()) || StringUtils.isBlank(project.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        projectMapper.insertSelective(project);
        return Result.ok();
    }

    @DeleteMapping("/{projectId}")
    public Result deleteProject(@PathVariable Integer projectId) {
        if (ValidateUtils.idInvalid(projectId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        projectMapper.deleteByPrimaryKey(projectId);
        return Result.ok();
    }

    @PutMapping("/{projectId}")
    public Result updateProject(@RequestBody Project project) {
        if (project == null || ValidateUtils.idInvalid(project.getId()) || ValidateUtils.idInvalid(project.getOwnerId())
                || StringUtils.isBlank(project.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        projectMapper.updateByPrimaryKeySelective(project);
        return Result.ok();
    }
}
