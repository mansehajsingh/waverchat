package com.waverchat.api.v1.user;

import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User getByEmail(String email) throws ResourceNotFoundException {
        Optional<User> result = this.userRepository.findByEmail(email);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException(
                    "User with email " + email + " does not exist or could not be found."
            );
        }
    }

}
