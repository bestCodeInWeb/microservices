package com.sn.snuser.service;

import com.sn.snuser.model.Post;

import java.util.List;
import java.util.Map;

public interface PostService {
    Post save(Post user);
    List<Post> findAll(Map<String, String> queryParams);
}
