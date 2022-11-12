package com.waverchat.api.v1.applicationresource.user;

import com.waverchat.api.v1.applicationresource.usercreationconfirmation.UserCreationConfirmationUtil;
import com.waverchat.api.v1.authentication.session.Session;
import com.waverchat.api.v1.customframework.AbstractApplicationEntity;
import com.waverchat.api.v1.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractApplicationEntity {

    @NotNull
    private String email;

    @NotNull
    @Length(min = UserConstants.MIN_USERNAME_LENGTH, max = UserConstants.MAX_USERNAME_LENGTH)
    private String username;

    @NotNull
    @NotEmpty
    private String passwordHash;

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

    @OneToMany(mappedBy = "user")
    private Set<Session> sessions = new HashSet<>();

    public User (String email, String username, String passwordHash, String firstName, String lastName, boolean superUser, boolean deleted) {
        this.setEmail(email);
        this.setUsername(username);
        this.setPasswordHash(passwordHash);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setSuperUser(superUser);
        this.setDeleted(deleted);
    }

    @Override
    public void edit(Map<String, Object> requestBody) {
        if (requestBody.containsKey("username"))
            this.username = (String) requestBody.get("username");
        if (requestBody.containsKey("firstName"))
            this.firstName = (String) requestBody.get("firstName");
        if (requestBody.containsKey("lastName"))
            this.lastName = (String) requestBody.get("lastName");
    }

    @Override
    public void validateForEdit() throws ValidationException {
        List<String> validationExceptionMessages = new ArrayList<>();

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
