package com.waverchat.api.v1.applicationresource.usercreationconfirmation;

import com.waverchat.api.v1.applicationresource.user.User;
import com.waverchat.api.v1.applicationresource.user.UserRepository;
import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserCreationConfirmationService {

    @Autowired
    private UserCreationConfirmationRepository userCreationConfirmationRepository;

    @Autowired
    private UserRepository userRepository;

    public UserCreationConfirmation create(UserCreationConfirmation userCreationConfirmation)
            throws ConflictException, ValidationException
    {
        // validate the user provided credentials
        userCreationConfirmation.validate();

        // if a user is already registered with the desired username we can't allow a new
        // user registry with that username
        if (this.userRepository.existsByUsernameIgnoreCase(userCreationConfirmation.getUsername())) {
            throw new ConflictException("Username is already in use.");
        }

        // if a user is already registered with the desired email we cannot allow a new
        // account creation with the same email
        if (this.userRepository.existsByEmailIgnoreCase(userCreationConfirmation.getEmail())) {
            throw new ConflictException("A user already exists with this email.");
        }

        // if we have a user that has not confirmed their account yet with the desired username,
        // we cannot allow a new unconfirmed user creation with the same username
        if (this.userCreationConfirmationRepository.existsByUsernameIgnoreCase(userCreationConfirmation.getUsername())) {
            throw new ConflictException("A user already exists with this username.");
        }

        // hashing supplied user password before passing it on to the db
        userCreationConfirmation.setPasswordHash(
                BCrypt.hashpw(userCreationConfirmation.getPassword(), BCrypt.gensalt())
        );

        return this.userCreationConfirmationRepository.save(userCreationConfirmation);
    }

    public UserCreationConfirmation getById(UUID id)
            throws ResourceNotFoundException
    {
        Optional<UserCreationConfirmation> result = this.userCreationConfirmationRepository.findById(id);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("UserCreationConfirmation", id);
        }
    }

    @Transactional
    public User deleteAllWithEmailUponConfirmation (UUID id) throws ResourceNotFoundException {
        Optional<UserCreationConfirmation> result = this.userCreationConfirmationRepository.findById(id);

        if (result.isPresent()) {
            UserCreationConfirmation toDelete = result.get();
            User userToCreate = new User(
                    toDelete.getEmail(),
                    toDelete.getUsername(),
                    toDelete.getPasswordHash(),
                    toDelete.getFirstName(),
                    toDelete.getLastName(),
                    toDelete.isSuperUser(),
                    toDelete.isDeleted()
            );
            // persist the user as a permanent user in the database
            this.userRepository.save(userToCreate);
            // delete the confirmation
            this.userCreationConfirmationRepository.deleteAllByEmailIgnoreCase(toDelete.getEmail());
            return userToCreate;
        } else {
            throw new ResourceNotFoundException("UserCreationConfirmation", id);
        }
    }

}