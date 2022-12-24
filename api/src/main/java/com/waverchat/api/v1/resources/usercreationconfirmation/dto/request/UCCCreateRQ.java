package com.waverchat.api.v1.resources.usercreationconfirmation.dto.request;

import com.waverchat.api.v1.framework.DtoRQ;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import lombok.Data;

@Data
public class UCCCreateRQ implements DtoRQ<UserCreationConfirmation> {

    private String email;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Override
    public UserCreationConfirmation toEntity() {
        return new UserCreationConfirmation(
                this.email, this.username, this.password, this.firstName, this.lastName);
    }

}
