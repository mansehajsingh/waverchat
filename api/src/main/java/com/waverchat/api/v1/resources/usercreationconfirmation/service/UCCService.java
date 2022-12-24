package com.waverchat.api.v1.resources.usercreationconfirmation.service;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.BaseService;
import com.waverchat.api.v1.resources.usercreationconfirmation.UserCreationConfirmationRepository;
import com.waverchat.api.v1.resources.usercreationconfirmation.dto.request.UCCCreateRQ;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import com.waverchat.api.v1.resources.usercreationconfirmation.resource.UCCResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UCCService extends BaseService<UserCreationConfirmation, UserCreationConfirmationRepository> {

    protected final static Logger log = LoggerFactory.getLogger(UCCResource.class);

    public void auditForCreate(UCCCreateRQ createRQ) throws ConflictException {
        String failureMessage = "UserCreationConfirmation failed creation audit. Reason: {},\n value: {}";

        UserCreationConfirmation candidate = createRQ.toEntity();

        if (this.repository.existsByUsernameIgnoreCase(candidate.getUsername())) {
            log.warn(failureMessage,
                    "UserCreationConfirmation exists with given username.", candidate.loggable());
            throw new ConflictException("Email is already in use by an existing user creation confirmation.");
        }
    }

    public UserCreationConfirmation create(UCCCreateRQ createRQ) throws ValidationException {
        UserCreationConfirmation candidate = createRQ.toEntity();

        candidate.validateForCreate();

        return this.repository.save(candidate);
    }

}
