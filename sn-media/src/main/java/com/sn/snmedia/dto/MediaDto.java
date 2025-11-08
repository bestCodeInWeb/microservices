package com.sn.snmedia.dto;

import com.sn.snmedia.model.enums.MediaOwnerType;
import com.sn.snmedia.model.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
    private String id;
    private String uri;
    private String mimeType;
    private MediaType type;
    private MediaOwnerType ownerType;
    private String ownerId;
    private String creatorId;
}