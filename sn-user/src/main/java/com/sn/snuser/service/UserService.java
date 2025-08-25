package com.sn.snuser.service;


import com.sn.snuser.model.User;

import java.util.List;

public interface UserService {
    User save(User user);
    List<User> findAll();
}
