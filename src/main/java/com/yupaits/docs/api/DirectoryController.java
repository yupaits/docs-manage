package com.yupaits.docs.api;

import com.yupaits.docs.common.response.Response;
import com.yupaits.docs.common.response.ResponseBuilder;
import com.yupaits.docs.mapper.DirectoryMapper;
import com.yupaits.docs.model.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yupaits on 2017/8/5.
 */
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
        List<Directory> directories = directoryMapper.selectBySelective(directory);
        return ResponseBuilder.build(HttpStatus.OK, directories);
    }

    @PostMapping("")
    public Response createDirectory(@RequestBody Directory directory) {
        directoryMapper.insertSelective(directory);
        return ResponseBuilder.buildWithNoData(HttpStatus.OK);
    }

    @DeleteMapping("/{directoryId}")
    public Response deleteDirectory(@PathVariable Integer directoryId) {
        directoryMapper.deleteByPrimaryKey(directoryId);
        return ResponseBuilder.buildWithNoData(HttpStatus.OK);
    }

    @PutMapping("/{directoryId}")
    public Response updateDirectory(@RequestBody Directory directory) {
        directoryMapper.updateByPrimaryKeySelective(directory);
        return ResponseBuilder.buildWithNoData(HttpStatus.OK);
    }
}
