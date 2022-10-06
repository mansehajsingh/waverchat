package com.waverchat.api.v1.UserCreationConfirmation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCreationConfirmationRepository extends JpaRepository<UserCreationConfirmation, UUID> {

    Optional<UserCreationConfirmation> findById(UUID uuid);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
