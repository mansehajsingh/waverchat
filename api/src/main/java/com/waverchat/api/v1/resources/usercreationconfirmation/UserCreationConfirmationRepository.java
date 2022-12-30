package com.waverchat.api.v1.resources.usercreationconfirmation;

import com.waverchat.api.v1.framework.BaseRepository;
import com.waverchat.api.v1.resources.usercreationconfirmation.entity.UserCreationConfirmation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Performs batch delete of user creation confirmations that have a created_at time stamp
     * before expiryTimeStamp
     * @param expiryTimeStamp
     */
    @Modifying
    @Query(
            value = "DELETE FROM " + UserCreationConfirmation.TABLE_NAME +
                    " as ucc WHERE ucc.created_at <= to_timestamp(:expiry, 'YYYY-MM-DD HH24:MI:SS');",
            nativeQuery = true
    )
    void batchDeleteExpired(@Param("expiry") String expiryTimeStamp);

}