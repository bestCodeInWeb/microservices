package com.sn.snuser.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @ManyToMany
    @JoinTable(name = "chat_user", // промежуточная таблица
            joinColumns = @JoinColumn(name = "chat_id"), // FK на Chat
            inverseJoinColumns = @JoinColumn(name = "user_id") // FK на User
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "chat_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Message>  messages = new ArrayList<>();
}
