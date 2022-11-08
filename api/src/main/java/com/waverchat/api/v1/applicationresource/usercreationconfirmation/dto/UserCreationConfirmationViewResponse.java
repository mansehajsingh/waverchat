package com.waverchat.api.v1.applicationresource.usercreationconfirmation.dto;

import com.waverchat.api.v1.customframework.dto.ViewResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreationConfirmationViewResponse extends ViewResponse {
    private String message;
}
