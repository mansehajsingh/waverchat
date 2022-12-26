package com.waverchat.api.v1.resources.user.service;


import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.ForbiddenException;
import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.framework.BaseService;
import com.waverchat.api.v1.resources.user.UserConstants;
import com.waverchat.api.v1.resources.user.UserRepository;
import com.waverchat.api.v1.resources.user.entity.QUser;
import com.waverchat.api.v1.resources.user.entity.User;
import com.waverchat.api.v1.resources.usercreationconfirmation.UserCreationConfirmationRepository;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import com.waverchat.api.v1.util.query.AppQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService extends BaseService<User, UserRepository> {

    protected final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserCreationConfirmationRepository uccRepository;

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

    public Page<User> findAll(Map<String, String> queryParams) {
        QUser quser = QUser.user;
        AppQuery query = new AppQuery();

        query.andAllStringQueries(UserConstants.QUERYABLE_STR_PATHS, queryParams);

        query.andDefaultDatePathBehaviour(quser.createdAt, quser.updatedAt, queryParams);

        Sort sort = this.createSort(UserConstants.DEFAULT_SORT_IS_ASCENDING,
                UserConstants.DEFAULT_SORT_FIELD, UserConstants.SUPPORTED_SORT_FIELDS, queryParams);

        Pageable pageable = this.createPageable(
                0, 100, UserConstants.MAX_PAGE_SIZE, sort, queryParams);

        return this.repository.findAll(query, pageable);
    }

    @Override
    public void auditForUpdate(User prevEntity, User candidate) throws ConflictException {
        String failureMessage = "User failed update audit. Reason: {}";

        if (prevEntity.getUsername() == candidate.getUsername())
            return;

        if (this.repository.existsByUsernameIgnoreCase(candidate.getUsername())) {
            log.warn(failureMessage, "User exists with given username.");
            throw new ConflictException("Username is already in use by an existing user.");
        }

        if (this.uccRepository.existsByUsernameIgnoreCase(candidate.getUsername())) {
            log.warn(failureMessage,
                    "UserCreationConfirmation exists with given username");
            throw new ConflictException("Username is already in use by an existing user creation confirmation.");
        }
    }
}
