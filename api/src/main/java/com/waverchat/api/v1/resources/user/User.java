package com.waverchat.api.v1.resources.user;

import com.waverchat.api.v1.customframework.RequestProperties;
import com.waverchat.api.v1.resources.organization.Organization;
import com.waverchat.api.v1.resources.usercreationconfirmation.UserCreationConfirmationUtil;
import com.waverchat.api.v1.authentication.session.Session;
import com.waverchat.api.v1.customframework.AbstractApplicationEntity;
import com.waverchat.api.v1.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "user")
    private Set<Session> sessions = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Organization> organizations = new HashSet<>();

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
    public void edit(RequestProperties props) {
        if (props.getRequestBody().containsKey("username"))
            this.username = (String) props.getRequestBody().get("username");
        if (props.getRequestBody().containsKey("firstName"))
            this.firstName = (String) props.getRequestBody().get("firstName");
        if (props.getRequestBody().containsKey("lastName"))
            this.lastName = (String) props.getRequestBody().get("lastName");
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
