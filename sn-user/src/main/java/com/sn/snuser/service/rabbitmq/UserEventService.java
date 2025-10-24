package com.sn.snuser.service.rabbitmq;

import com.sn.snuser.model.User;
import com.sn.snuser.model.embeddable.Address;
import com.sn.snuser.model.embeddable.Contacts;
import com.sn.snuser.model.embeddable.ProfileSettings;
import com.sn.snuser.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sn.snuser.model.enums.Gender.MALE;
import static java.time.LocalDateTime.now;

@Service
@Transactional
public class UserEventService {
    private static final Logger log = LoggerFactory.getLogger(UserEventService.class);
    private final UserService userService;

    public UserEventService(UserService userService) {
        this.userService = userService;
    }

    public void createUser(com.sn.events.User user, long timestamp) {
        log.info("Processing CREATE event for user ID: {}", user.getId());
        upsertUser(user, timestamp);
    }

    public void updateUser(com.sn.events.User user, long timestamp) {
        log.info("Processing UPDATE event for user ID: {}", user.getId());
        upsertUser(user, timestamp);
    }

    public void deleteUser(com.sn.events.User user, long timestamp) {
        log.info("Processing DELETE event for user ID: {}", user.getId());
        userService.deleteById(user.getId());
    }

    public void handleAction(com.sn.events.User user, long timestamp) {
        log.debug("Processing ACTION event for user ID: {}", user.getId());
    }

    private void upsertUser(com.sn.events.User user, long timestamp) {
        Optional<User> existingUserOpt = userService.findById(user.getId());

        if (existingUserOpt.isPresent()) {
            User snUser = existingUserOpt.get();
            log.debug("User {} found, updating...", user.getId());
            snUser.setUpdatedAt(now());
            snUser.setFirstName(user.getFirstName());
            snUser.setLastName(user.getLastName());
            snUser.setUserName(user.getUsername());

            if (user.getEmail() != null && !snUser.getContacts().getEmails().contains(user.getEmail())) {
                snUser.getContacts().getEmails().clear();
                snUser.getContacts().getEmails().add(user.getEmail());
            }
            userService.save(snUser);

        } else {
            log.debug("User {} not found, creating new...", user.getId());
            User snUser = new User(
                    user.getId(), now(), now(),
                    user.getUsername(), user.getFirstName(), user.getLastName(),
                    MALE, null, new Address(), new Contacts(), new ProfileSettings(), null, null);
            if (user.getEmail() != null) {
                snUser.getContacts().getEmails().add(user.getEmail());
            }
            userService.save(snUser);
        }
    }
}
