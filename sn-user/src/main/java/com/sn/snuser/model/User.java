package com.sn.snuser.model;

import com.sn.snuser.model.embeddable.Address;
import com.sn.snuser.model.embeddable.Contacts;
import com.sn.snuser.model.embeddable.Settings;
import com.sn.snuser.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "users")
@Builder
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

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
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
    private Settings settings;

    private String avatar;

    private String background;
}
