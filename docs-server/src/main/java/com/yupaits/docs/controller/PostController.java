package com.yupaits.docs.controller;

import com.yupaits.docs.common.dto.PostCreate;
import com.yupaits.docs.common.dto.PostQuery;
import com.yupaits.docs.common.dto.PostUpdate;
import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/docs/options")
    public Result getPostOptions() {
        return postService.getPostOptions();
    }

    @PostMapping("/docs/page")
    public Result getPostPage(@PageableDefault Pageable pageable, @RequestBody PostQuery postQuery) {
        return postService.getPostPage(postQuery, pageable);
    }

    @GetMapping("/docs/{postId}")
    public Result getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @PostMapping("/docs")
    public Result addPost(@RequestBody PostCreate postCreate) {
        return postService.addPost(postCreate);
    }

    @DeleteMapping("/docs/{postId}")
    public Result deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }

    @PutMapping("/docs/{postId}")
    public Result updatePost(@RequestBody PostUpdate postUpdate) {
        return postService.updatePost(postUpdate);
    }
}
