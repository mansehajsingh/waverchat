package com.waverchat.api.v1.resources.user;

import com.waverchat.api.v1.framework.BaseRepository;
import com.waverchat.api.v1.resources.user.entity.User;
import com.waverchat.api.v1.util.query.AppQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long>, QuerydslPredicateExecutor<User> {

    default Page<User> findAll(AppQuery query, Pageable pageable) {
        return findAll(query.getBuilder(), pageable);
    }

    Optional<User> findById(Long id);

    Optional<User> findByIdAndDeleted(Long id, boolean deleted);

    Optional<User> findByEmailIgnoreCaseAndDeleted(String email, boolean deleted);

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

}
