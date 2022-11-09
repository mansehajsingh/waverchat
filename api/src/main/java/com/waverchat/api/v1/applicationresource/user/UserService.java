package com.waverchat.api.v1.applicationresource.user;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.waverchat.api.v1.customframework.AbstractApplicationService;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.util.PageableFactory;
import com.waverchat.api.v1.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        BooleanBuilder builder = new BooleanBuilder();

        String [] strFields = {"username", "email", "firstName", "lastName"};
        StringPath [] stringPaths = {qUser.username, qUser.email, qUser.firstName, qUser.lastName};

        for (int i = 0; i < strFields.length; i++) {
            String field = strFields[i];
            if (queryParams.containsKey(field)) {
                String q = queryParams.get(field);
                QueryUtil.applyStringQueryWithWildcards(builder, stringPaths[i], q);
            }
        }

        Pageable pageable = PageableFactory.createPageable(0, 100, queryParams, UserConstants.MAX_PAGE_SIZE);
        Page<User> page = userRepository.findAll(builder, pageable);

        return page;
    }
}
