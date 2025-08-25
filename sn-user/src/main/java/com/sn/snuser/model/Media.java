package com.sn.snuser.model;

import com.sn.snuser.model.enums.MediaOwnerType;
import com.sn.snuser.model.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "media")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Media {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "media_uri", nullable = false)
    private String mediaUri;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Enumerated(EnumType.STRING)
    private MediaOwnerType ownerType;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;
}
