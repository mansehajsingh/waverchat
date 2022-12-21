package com.waverchat.api.v1.resources.usercreationconfirmation;

import com.waverchat.api.v1.framework.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCreationConfirmationRepository extends BaseRepository<UserCreationConfirmation, UUID> {

    Optional<UserCreationConfirmation> findById(UUID uuid);

    void deleteAllByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    void deleteById(UUID uuid);

    Optional<UserCreationConfirmation> findByUsernameIgnoreCase(String username);

}
