package com.waverchat.api.v1.applicationresource.usercreationconfirmation;

import com.waverchat.api.v1.applicationresource.user.UserConstants;
import com.waverchat.api.v1.customframework.AbstractApplicationEntity;
import com.waverchat.api.v1.exceptions.ValidationException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class UserCreationConfirmation extends AbstractApplicationEntity {

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

    public UserCreationConfirmation(Map<String, Object> requestBody) {
        this.email = (String) requestBody.get("email");
        this.username = (String) requestBody.get("username");
        this.password = (String) requestBody.get("password");
        this.firstName = (String) requestBody.get("firstName");
        this.lastName = (String) requestBody.get("lastName");
        this.superUser = false;
        this.deleted = false;
    }

    @Override
    public void validate() throws ValidationException {
        List<String> validationExceptionMessages = new ArrayList<>();

        // checking if the user provided valid credentials
        if (!UserCreationConfirmationUtil.isValidEmail(this.getEmail())) {
            validationExceptionMessages.add("Email is invalid");
        }
        if (!UserCreationConfirmationUtil.isValidPassword(this.getPassword())) {
            validationExceptionMessages.add("Password is invalid");
        }
        if (!UserCreationConfirmationUtil.isValidUsername(this.getUsername())) {
            validationExceptionMessages.add("Username is invalid.");
        }
        if (!UserCreationConfirmationUtil.isValidFirstName(this.getFirstName())) {
            validationExceptionMessages.add("First name is invalid.");
        }
        if (!UserCreationConfirmationUtil.isValidLastName(this.getLastName())) {
            validationExceptionMessages.add("Last name is invalid.");
        }

        // if there were any error messages added during validation we want to throw an exception
        // containing all those messages
        if (!validationExceptionMessages.isEmpty()) {
            throw new ValidationException(validationExceptionMessages);
        }
    }

}
