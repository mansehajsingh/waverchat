package com.waverchat.api.v1.resources.user.resource;

import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.framework.AbstractResource;
import com.waverchat.api.v1.resources.user.dto.response.UserGetRS;
import com.waverchat.api.v1.resources.user.entity.User;
import com.waverchat.api.v1.resources.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.waverchat.api.v1.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/users")
public class UserResource extends AbstractResource {

    protected final static Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    public UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserGetRS> get(@PathVariable Long id) throws NotFoundException {
        User user = this.userService.findById(id);
        UserGetRS responseBody = UserGetRS.from(user);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
