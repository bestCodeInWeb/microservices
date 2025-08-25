package com.sn.snuser.controller;

import com.sn.snuser.dto.UserDto;
import com.sn.snuser.model.User;
import com.sn.snuser.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        return userService.findAll().stream()
                .map(user -> UserDto.builder().build())
                .collect(toList());
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        User newUser  = User.builder().id("").build(); //todo
        User saved = userService.save(newUser);
        return UserDto.builder()
                .id(saved.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .gender(saved.getGender())
                .country(saved.getAddress().getCountry())
                .city(saved.getAddress().getCity())
                .street(saved.getAddress().getStreet())
                .street(userDto.getStreet())
                .build();
    }
}
