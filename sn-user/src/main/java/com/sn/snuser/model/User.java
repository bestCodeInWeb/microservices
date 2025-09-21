package com.sn.snuser.model;

import com.sn.snuser.model.embeddable.Address;
import com.sn.snuser.model.embeddable.Contacts;
import com.sn.snuser.model.embeddable.ProfileSettings;
import com.sn.snuser.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @Embedded
    private ProfileSettings profileSettings;

    private String avatar; //todo move to profile entity

    private String background;
}
