package com.sn.snuser.controller;

import com.sn.snuser.dto.PostDto;
import com.sn.snuser.mapper.PostMapper;
import com.sn.snuser.model.Post;
import com.sn.snuser.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public PostDto addPost(@RequestBody PostDto postDto) {
        Post newPost = PostMapper.INSTANCE.toEntity(postDto);
        return PostMapper.INSTANCE.toDto(postService.save(newPost));
    }

    @GetMapping
    public List<PostDto> getPosts() {
        return postService.findAll().stream().map(PostMapper.INSTANCE::toDto).collect(toList());
    }
}
