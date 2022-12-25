package com.waverchat.api.v1.resources.user.service;


import com.waverchat.api.v1.exceptions.ForbiddenException;
import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.framework.BaseService;
import com.waverchat.api.v1.resources.user.UserRepository;
import com.waverchat.api.v1.resources.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends BaseService<User, UserRepository> {

    protected final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    public User findById(Long id) throws NotFoundException {
        return this.findById(id, false);
    }

    public User findById(Long id, boolean includeDeleted) throws NotFoundException {
        Optional<User> userOpt;

        if (includeDeleted) {
            userOpt = this.repository.findById(id);
        } else {
            userOpt = this.repository.findByIdAndDeleted(id, false);
        }

        if (userOpt.isEmpty()) {
            String deletionState = includeDeleted ? "deleted/undeleted" : "undeleted";
            throw new NotFoundException("No " + deletionState + " user exists with id " + id);
        }

        return userOpt.get();
    }

    public User findByEmail(String email) throws NotFoundException {
        return this.findByEmail(email, false);
    }

    public User findByEmail(String email, boolean includeDeleted) throws NotFoundException {
        Optional<User> userOpt = this.repository.findByEmailIgnoreCaseAndDeleted(email, includeDeleted);

        if (userOpt.isEmpty()) {
            String deletionState = includeDeleted ? "deleted/undeleted" : "undeleted";
            throw new NotFoundException("No " + deletionState + " user exists with email " + email);
        }

        return userOpt.get();
    }

}
