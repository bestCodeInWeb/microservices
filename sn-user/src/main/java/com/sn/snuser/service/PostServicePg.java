package com.sn.snuser.service;

import com.sn.snuser.model.Post;
import com.sn.snuser.repository.PostRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<Post> findAll(Map<String, String> queryParams) {
        Specification<Post> specification = null;

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {

        }

        return postRepository.findAll(specification);
    }
}
