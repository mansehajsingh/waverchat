package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.resources.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findById(Long id);

    void deleteById(Long id);

    @Transactional
    void deleteByUser(User user);
}
