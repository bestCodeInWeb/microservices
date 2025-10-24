package com.sn.snuser.service;

import com.sn.snuser.model.Post;
import com.sn.snuser.repository.PostRepository;
import com.sn.snuser.repository.specification.SpecificationManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specification.allOf;

@Service
public class PostServicePg implements PostService {
    private final PostRepository postRepository;
    private final SpecificationManager<Post> postSpecificationManager;

    public PostServicePg(PostRepository postRepository,
                         SpecificationManager<Post> specificationManager) {
        this.postRepository = postRepository;
        this.postSpecificationManager = specificationManager;
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> findAll(Map<String, String> queryParams) {
        List<Specification<Post>> specifications = queryParams.entrySet().stream()
                .map(entry -> postSpecificationManager.get(entry.getKey(), entry.getValue().split(",")))
                .toList();

        return postRepository.findAll(allOf(specifications));
    }
}
