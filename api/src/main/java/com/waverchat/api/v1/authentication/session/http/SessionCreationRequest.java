package com.waverchat.api.v1.authentication.session.http;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SessionCreationRequest {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;

}
