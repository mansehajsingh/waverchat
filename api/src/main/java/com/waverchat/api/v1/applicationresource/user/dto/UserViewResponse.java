package com.waverchat.api.v1.applicationresource.user.dto;

import com.waverchat.api.v1.customframework.dto.ViewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserViewResponse extends ViewResponse {

    private UUID id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

}
