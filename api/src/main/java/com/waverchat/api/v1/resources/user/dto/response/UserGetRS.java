package com.waverchat.api.v1.resources.user.dto.response;

import com.waverchat.api.v1.framework.DtoRS;
import com.waverchat.api.v1.resources.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class UserGetRS implements DtoRS<User> {

    private Long id;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    public static UserGetRS from(User user) {
        return new UserGetRS(
                user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
                user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName());
    }

}
