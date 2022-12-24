package com.waverchat.api.v1.resources.usercreationconfirmation.dto.response;

import com.waverchat.api.v1.framework.DtoRS;
import com.waverchat.api.v1.resources.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class UCCConfirmRS implements DtoRS {

    private Long userId;

    private String email;

    private String username;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String firstName;

    private String lastName;

    public static UCCConfirmRS from(User user) {
        return new UCCConfirmRS(
                user.getId(), user.getEmail(), user.getUsername(), user.getCreatedAt(),
                user.getUpdatedAt(), user.getFirstName(), user.getLastName()
        );
    }

}
