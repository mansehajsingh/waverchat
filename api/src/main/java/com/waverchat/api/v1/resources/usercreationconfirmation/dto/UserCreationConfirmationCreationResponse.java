package com.waverchat.api.v1.resources.usercreationconfirmation.dto;

import com.waverchat.api.v1.customframework.dto.CreationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreationConfirmationCreationResponse extends CreationResponse {

    private String email;
    private String username;
    private String firstName;
    private String lastName;

}
