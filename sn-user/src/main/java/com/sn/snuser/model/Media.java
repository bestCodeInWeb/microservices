package com.sn.snuser.model;

import com.sn.snuser.model.enums.MediaOwnerType;
import com.sn.snuser.model.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Media {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "uri", nullable = false)
    private String uri;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type")
    private MediaOwnerType ownerType;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;
}
