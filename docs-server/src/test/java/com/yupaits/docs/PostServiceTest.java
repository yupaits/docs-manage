package com.yupaits.docs;

import com.google.common.collect.Lists;
import com.yupaits.docs.common.result.ResultCode;
import com.yupaits.docs.dto.PostCateCreate;
import com.yupaits.docs.dto.PostCreate;
import com.yupaits.docs.dto.PostUpdate;
import com.yupaits.docs.entity.Post;
import com.yupaits.docs.repository.PostCateRepository;
import com.yupaits.docs.repository.PostRepository;
import com.yupaits.docs.service.PostCateService;
import com.yupaits.docs.service.PostService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author yupaits
 * @date 2018/8/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCateService postCateService;

    @Autowired
    private PostCateRepository postCateRepository;

    @Test
    public void test01AddPostCates() {
        postCateService.addPostCate(new PostCateCreate("Java"));
        postCateService.addPostCate(new PostCateCreate("Spring Cloud"));
    }

    @Test
    public void test02AddPost() {
        Long cateId = postCateRepository.findAll().get(0).getId();
        postService.addPost(new PostCreate("Java基础", "Java集合类；Java反射", cateId, true, Lists.newArrayList("Java", "集合", "JVM虚拟机")));
        postService.addPost(new PostCreate("List和Map详解", "ArrayList和HashMap是最常用的集合类", cateId, true, Lists.newArrayList("集合", "数据结构")));
        postService.addPost(new PostCreate("测试文章", "仅用作测试", cateId, true, Lists.newArrayList("test", "测试")));
    }

    @Test
    public void test03UpdatePost() {
        Post post = postRepository.findAll().get(0);
        PostUpdate postUpdate = new PostUpdate();
        BeanUtils.copyProperties(post, postUpdate);
        postUpdate.setId(post.getId());
        postUpdate.setContent("修改之后的内容");
        postUpdate.setTags(Lists.newArrayList("Java", "反射", "集合"));
        postService.updatePost(postUpdate);
    }

    @Test
    public void test04DeletePost() {
        List<Post> postList = postRepository.findAll();
        Post post = postList.get(postList.size() - 1);
        Assert.assertEquals(postService.deletePost(post.getId()).getCode(), ResultCode.OK.getCode());
    }
}
