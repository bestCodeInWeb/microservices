package com.sn.snuser.service;

import com.sn.snuser.dto.PostDto;
import com.sn.snuser.model.Post;
import com.sn.snuser.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PostServicePg implements PostService {
    private final PostRepository postRepository;

    public PostServicePg(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll(); //todo ordered by
    }
}
