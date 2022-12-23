package com.waverchat.api.v1.resources.user.entity;

import com.waverchat.api.v1.authentication.session.Session;
import com.waverchat.api.v1.framework.AbstractEntity;
import com.waverchat.api.v1.resources.organizationmember.entity.OrganizationMember;
import com.waverchat.api.v1.resources.user.UserConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
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
public class User extends AbstractEntity {

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

}
