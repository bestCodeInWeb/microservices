package com.sn.snmedia.model;

import com.sn.snmedia.model.enums.MediaOwnerType;
import com.sn.snmedia.model.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "media")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Media {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "file_path", nullable = false)
    private String filePath; // Фізичний шлях до файлу

    @Column(name = "uri", nullable = false)
    private String uri; // Логічний URI (/media/download/...)

    @Column(name = "mime_type")
    private String mimeType;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type")
    private MediaOwnerType ownerType;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "creator_id", nullable = false)
    private String creatorId;
}
