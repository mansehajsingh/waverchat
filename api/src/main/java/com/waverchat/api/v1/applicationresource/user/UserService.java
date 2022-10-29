package com.waverchat.api.v1.applicationresource.user;

import com.waverchat.api.v1.customframework.AbstractApplicationService;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends AbstractApplicationService<User> {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<User> create(User entityToCreate) {
        return Optional.of(this.userRepository.save(entityToCreate));
    }

    public User getByEmailIgnoreCase(String email) throws ResourceNotFoundException {
        Optional<User> result = this.userRepository.findByEmailIgnoreCase(email);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException(
                    "User with email " + email + " does not exist or could not be found."
            );
        }
    }

    @Override
    public Optional<User> getById(UUID id) {
        return this.userRepository.findById(id);
    }

}
