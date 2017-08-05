package com.yupaits.docs.api;

import com.yupaits.docs.common.ResponseBuilder;
import com.yupaits.docs.mapper.ProjectMapper;
import com.yupaits.docs.common.Response;
import com.yupaits.docs.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yupaits on 2017/8/4.
 */
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectMapper projectMapper;

    @GetMapping("/owner/{ownerId}")
    public Response<List<Project>> projects(@PathVariable Integer ownerId) {
        Project project = new Project();
        project.setOwnerId(ownerId);
        return ResponseBuilder.success(projectMapper.selectBySelective(project));
    }

    @PostMapping("")
    public Response createProject(@RequestBody Project project) {
        projectMapper.insertSelective(project);
        return ResponseBuilder.success();
    }

    @DeleteMapping("/{projectId}")
    public Response deleteProject(@PathVariable Integer projectId) {
        projectMapper.deleteByPrimaryKey(projectId);
        return ResponseBuilder.success();
    }

    @PutMapping("/{projectId}")
    public Response updateProject(@RequestBody Project project) {
        projectMapper.updateByPrimaryKeySelective(project);
        return ResponseBuilder.success();
    }
}
