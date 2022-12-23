package com.waverchat.api.v1.resources.usercreationconfirmation;

import com.waverchat.api.v1.framework.BaseRepository;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCreationConfirmationRepository extends BaseRepository<UserCreationConfirmation, Long> {

    Optional<UserCreationConfirmation> findById(Long id);

    void deleteAllByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    void deleteById(Long id);

    Optional<UserCreationConfirmation> findByUsernameIgnoreCase(String username);

}
