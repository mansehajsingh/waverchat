package com.waverchat.api.v1.resources.usercreationconfirmation.service;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.ForbiddenException;
import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.BaseService;
import com.waverchat.api.v1.resources.user.UserRepository;
import com.waverchat.api.v1.resources.user.entity.User;
import com.waverchat.api.v1.resources.usercreationconfirmation.UCCConstants;
import com.waverchat.api.v1.resources.usercreationconfirmation.UserCreationConfirmationRepository;
import com.waverchat.api.v1.resources.usercreationconfirmation.dto.request.UCCCreateRQ;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UCCService extends BaseService<UserCreationConfirmation, UserCreationConfirmationRepository> {

    protected final static Logger log = LoggerFactory.getLogger(UCCService.class);

    private static final DateTimeFormatter batchDeleteExpiredDTF
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    protected UserRepository userRepository;

    public void auditForCreate(UCCCreateRQ createRQ) throws ConflictException {
        String failureMessage = "UserCreationConfirmation failed creation audit. Reason: {},\n value: {}";

        UserCreationConfirmation candidate = createRQ.toEntity();

        if (this.repository.existsByUsernameIgnoreCase(candidate.getUsername())) {
            log.warn(failureMessage,
                    "UserCreationConfirmation exists with given username.", candidate.loggable());
            throw new ConflictException("Username is already in use by an existing user creation confirmation.");
        }

        if (this.userRepository.existsByEmailIgnoreCase(candidate.getEmail())) {
            log.warn(failureMessage,
                    "User exists with given email", candidate.loggable());
            throw new ConflictException("Email is already in use by an existing user.");
        }

        if (this.userRepository.existsByUsernameIgnoreCase(candidate.getUsername())) {
            log.warn(failureMessage,
                    "User exists with given username", candidate.loggable());
            throw new ConflictException("Username is already in use by an existing user.");
        }
    }

    public UserCreationConfirmation create(UCCCreateRQ createRQ) throws ValidationException {
        UserCreationConfirmation candidate = createRQ.toEntity();

        candidate.validateForCreate();

        return this.repository.save(candidate);
    }

    @Transactional
    public User convertUserCreationConfirmationToUser(Long id, int verificationCode)
            throws NotFoundException, ForbiddenException
    {
        UserCreationConfirmation candidate = this.findById(id);

        if (candidate.getVerificationCode() != verificationCode)
            throw new ForbiddenException("Incorrect verification code.");

        User user = new User(
                candidate.getEmail(),
                candidate.getUsername(),
                candidate.getPasswordHash(),
                candidate.getFirstName(),
                candidate.getLastName()
        );

        User createdUser = this.userRepository.save(user);

        this.repository.deleteAllByEmailIgnoreCase(candidate.getEmail());

        return createdUser;
    }

    /**
     * Performs batch delete of all user creation confirmations that were created
     * X or more days ago defined by UCCConstants.EXPIRY_LENGTH_DAYS
     */
    @Transactional
    public void batchDeleteExpired() {
        String expiryTimeStamp = ZonedDateTime
                .now(ZoneId.of("UTC"))
                .minusDays(UCCConstants.EXPIRY_LENGTH_DAYS)
                .format(batchDeleteExpiredDTF);

        this.repository.batchDeleteExpired(expiryTimeStamp);
    }

}
