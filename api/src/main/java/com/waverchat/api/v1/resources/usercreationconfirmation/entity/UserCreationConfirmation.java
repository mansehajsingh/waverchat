package com.waverchat.api.v1.resources.usercreationconfirmation.entity;

import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.AbstractEntity;
import com.waverchat.api.v1.framework.Loggable;
import com.waverchat.api.v1.resources.user.UserConstants;
import com.waverchat.api.v1.resources.usercreationconfirmation.UserCreationConfirmationUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private int verificationCode;

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

    @Transient
    private static final Random random = new Random();

    public static int generateVerificationCode() {
        return (100000 + random.nextInt(900000));
    }

    public UserCreationConfirmation(
            String email,
            String username,
            String password,
            String firstName,
            String lastName
    ) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        this.firstName = firstName;
        this.lastName = lastName;
        this.verificationCode = generateVerificationCode();
    }

    public Loggable loggable() {
        return new Loggable("UserCreationConfirmation")
                .line("id", this.id)
                .line("email", this.email)
                .line("username", this.username)
                .line("passwordHash", this.passwordHash)
                .line("firstName", this.firstName)
                .line("lastName", this.lastName)
                .line("verificationCode", this.verificationCode);
    }

    @Override
    public void validateForCreate() throws ValidationException {
        List<String> messages = new ArrayList<>();

        if (!UserCreationConfirmationUtil.isValidEmail(this.email)) {
            messages.add("Email is invalid.");
        }

        if (!UserCreationConfirmationUtil.isValidUsername(this.username)) {
            messages.add("Username is invalid.");
        }

        if (!UserCreationConfirmationUtil.isValidPassword(this.password)) {
            messages.add("Password is invalid.");
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
