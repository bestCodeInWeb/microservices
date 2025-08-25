package com.sn.snuser.controller;

import com.sn.snuser.dto.PostDto;
import com.sn.snuser.model.Post;
import com.sn.snuser.service.PostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public Post addPost(@RequestBody PostDto postDto) {
        return postService.save(postDto);
    }
}
