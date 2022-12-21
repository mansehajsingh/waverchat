package com.waverchat.api.v1.resources.usercreationconfirmation;

import com.waverchat.api.v1.resources.user.UserConstants;
import com.waverchat.api.v1.framework.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name = "user_creation_confirmations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationConfirmation extends AbstractEntity {

    @NotNull
    private String email;

    @NotNull
    @Length(min = UserConstants.MIN_USERNAME_LENGTH, max = UserConstants.MAX_USERNAME_LENGTH)
    private String username;

    @NotNull
    @NotEmpty
    private String passwordHash;

    // Does not exist in db, only used at the service level
    @Transient
    private String password;

    @NotNull
    @NotBlank
    @Length(min = UserConstants.MIN_FIRST_NAME_LENGTH, max = UserConstants.MAX_FIRST_NAME_LENGTH)
    private String firstName;

    @NotNull
    @Length(max = UserConstants.MAX_LAST_NAME_LENGTH)
    private String lastName;

    @NotNull
    private boolean superUser = false;

    @NotNull
    private boolean deleted = false;

}
