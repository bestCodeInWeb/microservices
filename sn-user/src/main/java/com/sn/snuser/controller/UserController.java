package com.sn.snuser.controller;

import com.sn.snuser.dto.UserDto;
import com.sn.snuser.mapper.UserMapper;
import com.sn.snuser.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll().stream().map(UserMapper.INSTANCE::toDto).collect(toList());
    }

    @GetMapping("/{userId}")
    public UserDto getUserInfo(@PathVariable String userId) {
        return userService.findById(userId).map(UserMapper.INSTANCE::toDto).orElseThrow(); //todo
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        return UserMapper.INSTANCE.toDto(userService.save(UserMapper.INSTANCE.toEntity(userDto)));
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable String userId, @RequestBody UserDto userDto) {
        return UserMapper.INSTANCE.toDto(userService.update(userId, UserMapper.INSTANCE.toEntity(userDto)));
    }
}
