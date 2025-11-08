package com.sn.snuser.controller;

import com.sn.snmedia.model.enums.MediaOwnerType;
import com.sn.snuser.client.MediaClient; // Імпортуємо Feign-клієнт
import com.sn.snuser.dto.UserDto;
import com.sn.snuser.mapper.UserMapper;
import com.sn.snuser.model.User;
import com.sn.snuser.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final MediaClient mediaClient;

    public UserController(UserService userService, MediaClient mediaClient) {
        this.userService = userService;
        this.mediaClient = mediaClient;
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

    /**
     * Новий ендпоінт для завантаження аватара
     */
    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> uploadAvatar(
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        User user = userService.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 1. Викликаємо sn-media сервіс для завантаження файлу
        var mediaResponse = mediaClient.uploadFile(file, userId, MediaOwnerType.USER_AVATAR);

        if (mediaResponse.getStatusCode().is2xxSuccessful() && mediaResponse.getBody() != null) {
            String mediaUri = mediaResponse.getBody().getUri();

            // 2. Якщо в юзера вже був аватар, видаляємо старий (опціонально)
            if (user.getAvatar() != null && !user.getAvatar().isBlank()) {
                try {
                    // user.getAvatar() має зберігати ID, а не URI.
                    // Припустимо, що ми зберігаємо URI, тоді нам треба витягнути ID з URI.
                    // Або простіше - зберігати ID.
                    // Для простоти, припустимо, що 'avatar' зберігає ID медіа.
                    mediaClient.deleteFile(user.getAvatar());
                } catch (Exception e) {
                    // Логуємо помилку, але продовжуємо, оскільки новий аватар вже завантажено
                    System.err.println("Could not delete old avatar: " + e.getMessage());
                }
            }

            // 3. Зберігаємо ID нового аватара
            user.setAvatar(mediaResponse.getBody().getId()); // Зберігаємо ID
            User updatedUser = userService.save(user); // 'save' тут діє як 'update'

            return ResponseEntity.ok(UserMapper.INSTANCE.toDto(updatedUser));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
