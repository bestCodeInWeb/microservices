package com.sn.snuser.controller;

import com.sn.snuser.dto.PostDto;
import com.sn.snuser.model.Post;
import com.sn.snuser.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

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

    @GetMapping("/{id}")
    public Post createRandomPost(@PathVariable String id) {
        return postService.save(PostDto.builder().id(id).description("ds fds fds  ").content("dsf dsoi ").build());
    }

    @GetMapping
    public List<Post> getPosts() {
        return postService.findAll();
    }
}
