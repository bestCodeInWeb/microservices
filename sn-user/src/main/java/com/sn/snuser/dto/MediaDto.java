package com.sn.snuser.dto;

import com.sn.snuser.model.enums.MediaOwnerType;
import com.sn.snuser.model.enums.MediaType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
    private String id;
    private String uri;
    private MediaType type;
    private MediaOwnerType ownerType;
    private String ownerId;
}
