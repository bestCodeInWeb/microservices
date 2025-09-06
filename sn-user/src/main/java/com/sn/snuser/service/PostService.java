package com.sn.snuser.service;

import com.sn.snuser.model.Post;

import java.util.List;

public interface PostService {
    Post save(Post user);
    List<Post> findAll();
}
