package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.applicationresource.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findById(UUID id);

    void deleteById(UUID uuid);

    @Transactional
    void deleteByUser(User user);
}
