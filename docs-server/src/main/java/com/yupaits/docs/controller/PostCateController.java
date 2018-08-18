package com.yupaits.docs.controller;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.dto.PostCateCreate;
import com.yupaits.docs.dto.PostCateUpdate;
import com.yupaits.docs.service.PostCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@RestController
public class PostCateController {

    private final PostCateService postCateService;

    @Autowired
    public PostCateController(PostCateService postCateService) {
        this.postCateService = postCateService;
    }

    @GetMapping("/docs/cate/list")
    public Result listDocsCate() {
        return postCateService.listPostCate();
    }

    @PostMapping("/docs/cate")
    public Result addDocsCate(@RequestBody PostCateCreate postCateCreate) {
        return postCateService.addPostCate(postCateCreate);
    }

    @PutMapping("/docs/cate/{cateId}")
    public Result updateDocsCate(@RequestBody PostCateUpdate postCateUpdate) {
        return postCateService.updatePostCate(postCateUpdate);
    }

    @DeleteMapping("/docs/cate/{cateId}")
    public Result deleteDocsCate(@PathVariable Long cateId) {
        return postCateService.deletePostCate(cateId);
    }
}
