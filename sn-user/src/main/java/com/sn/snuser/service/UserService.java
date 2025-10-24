package com.sn.snuser.service;


import com.sn.snuser.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    User update(String userId, User userUpdates);
    List<User> findAll();
    Optional<User> findById(String  userId);
    void deleteById(String  userId);
}
