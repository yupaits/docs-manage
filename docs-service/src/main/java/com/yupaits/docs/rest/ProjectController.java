package com.yupaits.docs.rest;

import com.yupaits.docs.response.Result;
import com.yupaits.docs.response.ResultCode;
import com.yupaits.docs.config.jwt.JwtHelper;
import com.yupaits.docs.entity.Project;
import com.yupaits.docs.repository.ProjectRepository;
import com.yupaits.docs.util.bean.BeanUtil;
import com.yupaits.docs.util.http.HttpUtil;
import com.yupaits.docs.util.validate.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

/**
 * 项目REST接口
 * Created by yupaits on 2017/8/4.
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/owner/{ownerId}")
    public Result projects(@PathVariable Integer ownerId) {
        if (ValidateUtils.idInvalid(ownerId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (ownerId.compareTo(jwtHelper.getUserId(HttpUtil.getRequest())) != 0) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        return Result.ok(projectRepository.findByOwnerIdAndIsDeletedIsFalseOrderBySortCodeAsc(ownerId));
    }

    @GetMapping("/{projectId}")
    public Result getProject(@PathVariable Integer projectId) {
        if (ValidateUtils.idInvalid(projectId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Project project = projectRepository.findOne(projectId);
        if (project!= null && project.getOwnerId().compareTo(jwtHelper.getUserId(HttpUtil.getRequest())) != 0) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        return Result.ok(project);
    }

    @PostMapping("")
    public Result createProject(@RequestBody Project project) {
        if (project == null || ValidateUtils.idInvalid(project.getOwnerId()) || StringUtils.isBlank(project.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (project.getOwnerId().compareTo(jwtHelper.getUserId(HttpUtil.getRequest())) != 0) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        project.setIsDeleted(false);
        project.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        projectRepository.save(project);
        return Result.ok();
    }

    @DeleteMapping("/{projectId}")
    public Result deleteProject(@PathVariable Integer projectId) {
        if (ValidateUtils.idInvalid(projectId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Project projectInDb = projectRepository.findOne(projectId);
        if (projectInDb == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        if (projectInDb.getOwnerId().compareTo(jwtHelper.getUserId(HttpUtil.getRequest())) != 0) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        projectInDb.setIsDeleted(true);
        projectInDb.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        projectRepository.save(projectInDb);
        return Result.ok();
    }

    @PutMapping("/{projectId}")
    public Result updateProject(@RequestBody Project project) {
        if (project == null || ValidateUtils.idInvalid(project.getId()) || StringUtils.isBlank(project.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Project projectInDb = projectRepository.findOne(project.getId());
        if (projectInDb == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        if (projectInDb.getOwnerId().compareTo(jwtHelper.getUserId(HttpUtil.getRequest())) != 0) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        BeanUtil.copyProperties(project, projectInDb);
        projectInDb.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        projectRepository.save(projectInDb);
        return Result.ok();
    }
}
