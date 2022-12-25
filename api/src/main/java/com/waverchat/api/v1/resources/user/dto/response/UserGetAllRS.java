package com.waverchat.api.v1.resources.user.dto.response;

import com.waverchat.api.v1.framework.DtoRS;
import com.waverchat.api.v1.resources.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class UserGetAllRS implements DtoRS {

    private Long id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    public static UserGetAllRS from(User user) {
        return new UserGetAllRS(
                user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
                user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName());
    }

}
