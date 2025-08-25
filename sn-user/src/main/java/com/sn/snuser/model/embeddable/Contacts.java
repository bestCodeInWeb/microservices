package com.sn.snuser.model.embeddable;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contacts {

    @ElementCollection
    @CollectionTable(name = "user_mobiles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "mobile")
    private List<String> mobiles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_emails", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "email")
    private List<String> emails = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_sites", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "site")
    private List<String> sites = new ArrayList<>();
}
