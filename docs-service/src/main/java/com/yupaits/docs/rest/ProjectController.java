package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Response;
import com.yupaits.docs.common.response.ResponseBuilder;
import com.yupaits.docs.mapper.ProjectMapper;
import com.yupaits.docs.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yupaits on 2017/8/4.
 */
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectMapper projectMapper;

    @GetMapping("/owner/{ownerId}")
    public Response<List<Project>> projects(@PathVariable Integer ownerId) {
        Project project = new Project();
        project.setOwnerId(ownerId);
        return ResponseBuilder.ok(projectMapper.selectBySelective(project));
    }

    @PostMapping("")
    public Response createProject(@RequestBody Project project) {
        projectMapper.insertSelective(project);
        return ResponseBuilder.ok();
    }

    @DeleteMapping("/{projectId}")
    public Response deleteProject(@PathVariable Integer projectId) {
        projectMapper.deleteByPrimaryKey(projectId);
        return ResponseBuilder.ok();
    }

    @PutMapping("/{projectId}")
    public Response updateProject(@RequestBody Project project) {
        projectMapper.updateByPrimaryKeySelective(project);
        return ResponseBuilder.ok();
    }
}
