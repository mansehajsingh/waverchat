package com.waverchat.api.v1.usercreationconfirmation;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.user.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserCreationConfirmationService {

    @Autowired
    private UserCreationConfirmationRepository userCreationConfirmationRepository;

    @Autowired
    private UserRepository userRepository;

    public UserCreationConfirmation create(UserCreationConfirmation userCreationConfirmation)
            throws ConflictException
    {
        // if a user is already registered with the desired username we can't allow a new
        // user registry with that username
        if (userRepository.existsByUsername(userCreationConfirmation.getUsername())) {
            throw new ConflictException("Username is already in use.");
        }

        // if a user is already registered with the desired email we cannot allow a new
        // account creation with the same email
        if (userRepository.existsByEmail(userCreationConfirmation.getEmail())) {
            throw new ConflictException("A user already exists with this email.");
        }

        // if we have a user that has not confirmed their account yet with the desired username,
        // we cannot allow a new unconfirmed user creation with the same username
        if (userCreationConfirmationRepository.existsByUsername(userCreationConfirmation.getUsername())) {
            throw new ConflictException("A user already exists with this username.");
        }

        // hashing supplied user password before passing it on to the db
        userCreationConfirmation.setPasswordHash(
                BCrypt.hashpw(userCreationConfirmation.getPasswordHash(), BCrypt.gensalt())
        );

        return userCreationConfirmationRepository.save(userCreationConfirmation);
    }

    public UserCreationConfirmation getById(UUID id)
            throws ResourceNotFoundException
    {
        Optional<UserCreationConfirmation> result = userCreationConfirmationRepository.findById(id);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("UserCreationConfirmation", id);
        }
    }

    public void deleteById(UUID id) {
        userCreationConfirmationRepository.deleteById(id);
    }

}
