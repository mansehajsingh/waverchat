package com.waverchat.api.v1.applicationresource.usercreationconfirmation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCreationConfirmationRepository extends JpaRepository<UserCreationConfirmation, UUID> {

    Optional<UserCreationConfirmation> findById(UUID uuid);

    void deleteAllByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    void deleteById(UUID uuid);

    Optional<UserCreationConfirmation> findByUsernameIgnoreCase(String username);

}
