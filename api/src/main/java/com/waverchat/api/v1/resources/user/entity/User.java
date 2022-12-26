package com.waverchat.api.v1.resources.user.entity;

import com.waverchat.api.v1.authentication.session.Session;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.AbstractEntity;
import com.waverchat.api.v1.resources.organizationmember.entity.OrganizationMember;
import com.waverchat.api.v1.resources.user.UserConstants;
import com.waverchat.api.v1.resources.usercreationconfirmation.UserCreationConfirmationUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class User extends AbstractEntity<User> {

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

    @OneToMany(mappedBy = "member")
    private Set<OrganizationMember> organizationMemberships = new HashSet<>();

    public User (String email, String username, String passwordHash, String firstName, String lastName, boolean superUser, boolean deleted) {
        this.setEmail(email);
        this.setUsername(username);
        this.setPasswordHash(passwordHash);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setSuperUser(superUser);
        this.setDeleted(deleted);
    }

    public User (String email, String username, String passwordHash, String firstName, String lastName) {
        this.setEmail(email);
        this.setUsername(username);
        this.setPasswordHash(passwordHash);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    @Override
    public User clone() {
        User clone = new User(this.email, this.username, this.passwordHash,
                this.firstName, this.lastName, this.superUser, this.deleted);

        clone.setId(this.id);
        clone.setPassword(this.password);
        clone.setCreatedAt(this.createdAt);
        clone.setUpdatedAt(this.updatedAt);

        return clone;
    }

    @Override
    public User cloneFromDiff(User diff) {
        User clone = this.clone();

        if (diff.getUsername() != null) {
            clone.setUsername(diff.getUsername());
        }

        if (diff.getFirstName() != null) {
            clone.setFirstName(diff.getFirstName());
        }

        if (diff.getLastName() != null) {
            clone.setLastName(diff.getLastName());
        }

        return clone;
    }

    @Override
    public void validateForUpdate() throws ValidationException {
         List<String> messages = new ArrayList<>();

         if (!UserCreationConfirmationUtil.isValidUsername(this.username)) {
             messages.add("Username is invalid.");
         }

         if (!UserCreationConfirmationUtil.isValidFirstName(this.firstName)) {
             messages.add("First name is invalid.");
         }

         if (!UserCreationConfirmationUtil.isValidLastName(this.lastName)) {
             messages.add("Last name is invalid");
         }

         if (messages.size() > 0)
             throw new ValidationException(messages);
     }

}
