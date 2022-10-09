package com.waverchat.api.v1.usercreationconfirmation.http;

import com.waverchat.api.v1.user.UserConstants;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserCreationConfirmationRequest {

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @Length(min = UserConstants.MIN_USERNAME_LENGTH, max = UserConstants.MAX_USERNAME_LENGTH)
    private String username;

    @NotNull
    @NotEmpty
    @Length(min = UserConstants.MIN_PASSWORD_LENGTH, max = UserConstants.MAX_PASSWORD_LENGTH)
    private String password;

    @NotNull
    @NotEmpty
    @Length(min = UserConstants.MIN_FIRST_NAME_LENGTH, max = UserConstants.MAX_FIRST_NAME_LENGTH)
    private String firstName;

    @NotNull
    @Length(max = UserConstants.MAX_LAST_NAME_LENGTH)
    private String lastName;

}
