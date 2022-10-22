package com.waverchat.api.v1.usercreationconfirmation;

import com.waverchat.api.v1.ApplicationEntity;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.user.UserConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
public class UserCreationConfirmation extends ApplicationEntity {

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

    public UserCreationConfirmation(String email, String username, String password, String firstName, String lastName) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.superUser = false;
        this.deleted = false;
    }

    public void validate() throws ValidationException {
        List<String> validationExceptionMessages = new ArrayList<>();

        // checking if the user provided valid credentials
        if (!UserCreationConfirmationValidationUtil.isValidEmail(this.getEmail())) {
            validationExceptionMessages.add("Email is invalid");
        }
        if (!UserCreationConfirmationValidationUtil.isValidPassword(this.getPasswordHash())) {
            validationExceptionMessages.add("Password is invalid");
        }
        if (!UserCreationConfirmationValidationUtil.isValidUsername(this.getUsername())) {
            validationExceptionMessages.add("Username is invalid.");
        }
        if (!UserCreationConfirmationValidationUtil.isValidFirstName(this.getFirstName())) {
            validationExceptionMessages.add("First name is invalid.");
        }
        if (!UserCreationConfirmationValidationUtil.isValidLastName(this.getLastName())) {
            validationExceptionMessages.add("Last name is invalid.");
        }

        // if there were any error messages added during validation we want to throw an exception
        // containing all those messages
        if (validationExceptionMessages.isEmpty()) {
            throw new ValidationException(validationExceptionMessages);
        }
    }

}
