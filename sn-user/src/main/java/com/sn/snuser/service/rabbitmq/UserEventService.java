package com.sn.snuser.service.rabbitmq;

import com.sn.snuser.model.User;
import com.sn.snuser.model.embeddable.Address;
import com.sn.snuser.model.embeddable.Contacts;
import com.sn.snuser.model.embeddable.ProfileSettings;
import com.sn.snuser.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sn.snuser.model.enums.Gender.MALE;
import static java.time.LocalDateTime.now;

@Service
@Transactional
public class UserEventService {

    private final UserService userService;

    public UserEventService(UserService userService) {
        this.userService = userService;
    }

    public void createUser(com.sn.events.User user, long timestamp) {
        //todo use timestamd or remove it
        User snUser = new User(
                user.getId(), now(), now(),
                user.getUsername(), user.getFirstName(), user.getLastName(),
                MALE, null, new Address(), new Contacts(), new ProfileSettings(), null, null);
        userService.save(snUser);
    }

    public void updateUser(com.sn.events.User user, long timestamp) {
        //todo use timestamd or remove it
        userService.findById(user.getId()).map(snUser -> {
            snUser.setUpdatedAt(now());
            snUser.setFirstName(user.getFirstName());
            snUser.setLastName(user.getLastName());

            return snUser;
        }).orElseThrow();
    }

    public void deleteUser(com.sn.events.User user, long timestamp) {
        userService.deleteById(user.getId());
    }

    public void action(com.sn.events.User user, long timestamp) {
        System.out.println("actions??");
    }
}
