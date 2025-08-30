package com.sn.snuser.service;

import com.sn.snuser.dto.PostDto;
import com.sn.snuser.model.Post;

import java.util.List;

public interface PostService {
    Post save(PostDto user); //todo Post??
    List<Post> findAll();
}
