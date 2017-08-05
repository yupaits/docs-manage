package com.yupaits.docsservice.api;

import com.yupaits.docsservice.common.Response;
import com.yupaits.docsservice.common.ResponseBuilder;
import com.yupaits.docsservice.mapper.DirectoryMapper;
import com.yupaits.docsservice.model.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yupaits on 2017/8/5.
 */
@RestController
@RequestMapping("/projects/{projectId}/directories")
public class DirectoryController {

    @Autowired
    private DirectoryMapper directoryMapper;

    @GetMapping("")
    public Response<List<Directory>> projectDirectories(@PathVariable Integer projectId) {
        Directory directory = new Directory();
        directory.setProjectId(projectId);
        directory.setParentId(0);
        List<Directory> directories = directoryMapper.selectBySelective(directory);
        return ResponseBuilder.success(directories);
    }

    @PostMapping("")
    public Response createDirectory(@RequestBody Directory directory) {
        directoryMapper.insertSelective(directory);
        return ResponseBuilder.success();
    }

    @DeleteMapping("/{directoryId}")
    public Response deleteDirectory(@PathVariable Integer directoryId) {
        directoryMapper.deleteByPrimaryKey(directoryId);
        return ResponseBuilder.success();
    }

    @PutMapping("/{directoryId}")
    public Response updateDirectory(@RequestBody Directory directory) {
        directoryMapper.updateByPrimaryKeySelective(directory);
        return ResponseBuilder.success();
    }
}
