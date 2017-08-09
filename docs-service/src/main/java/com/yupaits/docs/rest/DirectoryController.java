package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Response;
import com.yupaits.docs.common.response.ResponseBuilder;
import com.yupaits.docs.mapper.DirectoryMapper;
import com.yupaits.docs.model.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yupaits on 2017/8/5.
 */
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/projects/{projectId}/directories")
public class DirectoryController {

    @Autowired
    private DirectoryMapper directoryMapper;

    @GetMapping("")
    public Response<List<Directory>> projectDirectories(@PathVariable Integer projectId) {
        Directory directory = new Directory();
        directory.setProjectId(projectId);
        directory.setParentId(0);
        return ResponseBuilder.ok(directoryMapper.selectBySelective(directory));
    }

    @GetMapping("/parentId/{parentId}")
    public Response getDirectoriesByParentId(@PathVariable Integer projectId, @PathVariable Integer parentId) {
        Directory directory = new Directory();
        directory.setProjectId(projectId);
        directory.setParentId(parentId);
        return ResponseBuilder.ok(directoryMapper.selectBySelective(directory));
    }

    @PostMapping("")
    public Response createDirectory(@RequestBody Directory directory) {
        directoryMapper.insertSelective(directory);
        return ResponseBuilder.ok();
    }

    @DeleteMapping("/{directoryId}")
    public Response deleteDirectory(@PathVariable Integer directoryId) {
        directoryMapper.deleteByPrimaryKey(directoryId);
        return ResponseBuilder.ok();
    }

    @PutMapping("/{directoryId}")
    public Response updateDirectory(@RequestBody Directory directory) {
        directoryMapper.updateByPrimaryKeySelective(directory);
        return ResponseBuilder.ok();
    }
}
