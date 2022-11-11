package com.waverchat.api.v1.authentication.session.http;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AllSessionsDeletionRequest {
    @NotNull
    @NotBlank
    private String userId;
}
