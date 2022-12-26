package com.waverchat.api.v1.resources.user.dto.request;

import com.waverchat.api.v1.framework.DtoRQ;
import com.waverchat.api.v1.resources.user.entity.User;
import lombok.Data;

@Data
public class UserUpdateRQ implements DtoRQ<User> {


    private String username;

    private String firstName;

    private String lastName;

    @Override
    public User toEntity() {
        User userToUpdate = new User();
        userToUpdate.setUsername(this.username);
        userToUpdate.setFirstName(this.firstName);
        userToUpdate.setLastName(this.lastName);

        return userToUpdate;
    }
}
