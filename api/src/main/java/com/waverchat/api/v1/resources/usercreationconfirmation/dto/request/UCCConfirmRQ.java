package com.waverchat.api.v1.resources.usercreationconfirmation.dto.request;

import com.waverchat.api.v1.framework.AbstractEntity;
import com.waverchat.api.v1.framework.DtoRQ;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import lombok.Data;

@Data
public class UCCConfirmRQ implements DtoRQ<UserCreationConfirmation> {

    private int verificationCode;

    @Override
    public UserCreationConfirmation toEntity() {
        UserCreationConfirmation ucc = new UserCreationConfirmation();
        ucc.setVerificationCode(verificationCode);
        return ucc;
    }

}
