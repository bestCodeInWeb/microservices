package com.sn.snuser.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ProfileSettings {
    @Column(name = "is_private")
    private boolean isPrivate;
}
