package com.sn.snuser.controller;

import com.sn.snuser.dto.PostDto;
import com.sn.snuser.mapper.PostMapper;
import com.sn.snuser.model.Post;
import com.sn.snuser.model.User;
import com.sn.snuser.repository.PostRepository;
import com.sn.snuser.repository.UserRepository;
import com.sn.snuser.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostService postService,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Transactional
    public PostDto addPost(@RequestBody PostDto postDto, @AuthenticationPrincipal Jwt jwt) {
        Post newPost = PostMapper.INSTANCE.toEntity(postDto);
        User currentUser = userRepository.findById(jwt.getSubject()).orElseThrow();
        newPost.setId(UUID.randomUUID().toString());
        newPost.setCreator(currentUser);
        newPost.setCreatedAt(now()); //todo move to some common login
        newPost.setUpdatedAt(newPost.getCreatedAt()); //todo transaction?
        return PostMapper.INSTANCE.toDto(postService.save(newPost));
    }

    //todo patch everywhere
    @PutMapping
    @Transactional
    public PostDto updatePost(@RequestBody PostDto postDto, @AuthenticationPrincipal Jwt jwt) {
        Post currentPost = postRepository.findById(postDto.getId()).orElseThrow();
        //todo assert current user is updating its post
        currentPost.setUpdatedAt(now()); //todo transaction?
        currentPost.setText(postDto.getText());
        currentPost.getHashtags().clear(); //todo
        currentPost.setHashtags(postDto.getHashtags());
        return PostMapper.INSTANCE.toDto(currentPost);
    }

    @DeleteMapping(value = "/{postId}")
    @Transactional
    public void deletePost(@PathVariable String postId, @AuthenticationPrincipal Jwt jwt) {
        postRepository.deleteById(postId);
        //todo assert current user is updating its post
    }

    //todo pageable by filter
    @GetMapping
    public List<PostDto> getPosts(@RequestParam Map<String, String> queryParams) {
        return postService.findAll(queryParams).stream().map(PostMapper.INSTANCE::toDto).collect(toList());
    }
}
