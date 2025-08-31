package com.sn.snuser.controller;

import com.sn.snuser.dto.PostDto;
import com.sn.snuser.model.Post;
import com.sn.snuser.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post addPost(@RequestBody PostDto postDto) {
        System.out.println("add POst");
        return postService.save(postDto);
    }

    @GetMapping("/{id}")
    public Post createRandomPost(@PathVariable String id) {
        System.out.println("rcreate POst");
        return postService.save(PostDto.builder().id(id).description("ds fds fds  ").content("dsf dsoi ").build());
    }

    @GetMapping
    public List<Post> getPosts() {
        System.out.println("get POsts");
        return postService.findAll();
    }
}
