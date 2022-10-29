package com.waverchat.api.v1.applicationresource.usercreationconfirmation;

import com.waverchat.api.v1.applicationresource.user.UserRepository;
import com.waverchat.api.v1.customframework.AbstractApplicationService;
import com.waverchat.api.v1.exceptions.ConflictException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserCreationConfirmationService extends AbstractApplicationService<UserCreationConfirmation> {

    @Autowired
    private UserCreationConfirmationRepository userCreationConfirmationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void auditForCreate(UserCreationConfirmation entityToCreate) throws ConflictException {
        // Verifying no existing user uses the provided username
        if (this.userRepository.existsByUsernameIgnoreCase(entityToCreate.getUsername())) {
            throw new ConflictException("Username already in use by existing user.");
        }
        // Verifying no existing user uses the provided email
        if (this.userRepository.existsByEmailIgnoreCase(entityToCreate.getEmail())) {
            throw new ConflictException("Email is already in use by existing user.");
        }
        // Verifying no existing user creation confirmation uses the provided username
        if (this.userCreationConfirmationRepository.existsByUsernameIgnoreCase(entityToCreate.getUsername())) {
            throw new ConflictException("Username is already in use by existing user.");
        }
        // if none of the above are triggered, we are good to create this resource
    }

    @Override
    public Optional<UserCreationConfirmation> create(UserCreationConfirmation entityToCreate) {
        // hashing supplied user password before passing it on to the db
        entityToCreate.setPasswordHash(
                BCrypt.hashpw(entityToCreate.getPassword(), BCrypt.gensalt())
        );

        UserCreationConfirmation createdEntity = this.userCreationConfirmationRepository.save(entityToCreate);

        return Optional.of(createdEntity);
    }

    @Override
    public Optional<UserCreationConfirmation> getById(UUID id) {
        return this.userCreationConfirmationRepository.findById(id);
    }

    @Transactional
    public void deleteAllByEmail(String email) {
        this.userCreationConfirmationRepository.deleteAllByEmailIgnoreCase(email);
    }

}
