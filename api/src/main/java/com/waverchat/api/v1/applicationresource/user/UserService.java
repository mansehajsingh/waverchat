package com.waverchat.api.v1.applicationresource.user;

import com.querydsl.core.types.dsl.StringPath;
import com.waverchat.api.v1.customframework.AbstractApplicationService;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.util.query.AppQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;
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

    @Override
    public Page<User> getAll(Map<String, String> pathVariables, Map<String, String> queryParams) {
        QUser qUser = QUser.user;
        AppQuery query = new AppQuery();

        // the following fields can be used to query with the supporting string query features
        // like wildcard support
        String [] strFields = {"username", "email", "firstName", "lastName"};
        StringPath [] stringPaths = {qUser.username, qUser.email, qUser.firstName, qUser.lastName};

        // building the query from each of the fields if the search contains the specified field
        query.andAllStringQueries(strFields, stringPaths, queryParams);

        // adding query support for date based searches
        query.andDefaultDatePathBehaviour(qUser.createdAt, qUser.updatedAt, queryParams);

        // adding sorting
        Sort sort = this.createSort(UserConstants.DEFAULT_SORT_IS_ASCENDING, UserConstants.DEFAULT_SORT_FIELD, queryParams);

        Pageable pageable = this.createPageable(
                0, 100, UserConstants.MAX_PAGE_SIZE, sort, queryParams);
        Page<User> page = this.userRepository.findAll(query, pageable);

        return page;
    }
}
