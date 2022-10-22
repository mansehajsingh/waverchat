package com.waverchat.api.v1.applicationresource.usercreationconfirmation.http;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserCreationConfirmationResponse {

    private UUID id;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

}
