package com.waverchat.api.v1.resources.usercreationconfirmation.dto.response;

import com.waverchat.api.v1.framework.DtoRS;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UCCCreateRS implements DtoRS<UserCreationConfirmation> {

    private Long id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    public static UCCCreateRS from(UserCreationConfirmation ucc) {
        return new UCCCreateRS(
                ucc.getId(),
                ucc.getCreatedAt(),
                ucc.getUpdatedAt(),
                ucc.getEmail(),
                ucc.getUsername(),
                ucc.getFirstName(),
                ucc.getLastName()
        );
    }

    public UCCCreateRS(
            Long id,
            ZonedDateTime createdAt,
            ZonedDateTime updatedAt,
            String email,
            String username,
            String firstName,
            String lastName
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
