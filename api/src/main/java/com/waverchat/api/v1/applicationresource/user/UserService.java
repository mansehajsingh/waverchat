package com.waverchat.api.v1.applicationresource.user;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.waverchat.api.v1.customframework.AbstractApplicationService;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.util.PageableFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<User> getAll(Map<String, String> pathVariables, Map<String, String> queryParams) {
        QUser qUser = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();

        if (queryParams.containsKey("username")) {
            String qUsername = queryParams.get("username");

            if (qUsername.startsWith("*") && qUsername.endsWith("*"))
                builder.and(qUser.username.contains(qUsername.substring(1, qUsername.length() - 1)));

            else if (qUsername.startsWith("*"))
                builder.and(qUser.username.endsWith(qUsername.substring(1)));

            else if (qUsername.endsWith("*"))
                builder.and(qUser.username.startsWith(qUsername.substring(0, qUsername.length() - 1)));

            else builder.and(qUser.username.eq(qUsername));
        }

        Pageable pageable = PageableFactory.createPageable(0, 100, queryParams, UserConstants.MAX_PAGE_SIZE);
        Iterable<User> iter = userRepository.findAll(builder, pageable);
        return Lists.newArrayList(iter);
    }
}
