package com.sn.snuser.service;

import com.sn.snuser.model.User;
import com.sn.snuser.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServicePg implements UserService {
    private final UserRepository userRepository;

    public UserServicePg(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(String userId, User userUpdates) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Update only non-null fields from userUpdates
        if (userUpdates.getUserName() != null) {
            existingUser.setUserName(userUpdates.getUserName());
        }
        if (userUpdates.getFirstName() != null) {
            existingUser.setFirstName(userUpdates.getFirstName());
        }
        if (userUpdates.getLastName() != null) {
            existingUser.setLastName(userUpdates.getLastName());
        }
        if (userUpdates.getGender() != null) {
            existingUser.setGender(userUpdates.getGender());
        }
        if (userUpdates.getBirthDate() != null) {
            existingUser.setBirthDate(userUpdates.getBirthDate());
        }
        if (userUpdates.getAddress() != null) {
            existingUser.setAddress(userUpdates.getAddress());
        }
        if (userUpdates.getContacts() != null) {
            existingUser.setContacts(userUpdates.getContacts());
        }
        if (userUpdates.getProfileSettings() != null) {
            existingUser.setProfileSettings(userUpdates.getProfileSettings());
        }
        if (userUpdates.getAvatar() != null) {
            existingUser.setAvatar(userUpdates.getAvatar());
        }
        if (userUpdates.getBackground() != null) {
            existingUser.setBackground(userUpdates.getBackground());
        }

        // Update timestamp
        existingUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(existingUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteById(String userId) {
        userRepository.deleteById(userId);
    }
}
