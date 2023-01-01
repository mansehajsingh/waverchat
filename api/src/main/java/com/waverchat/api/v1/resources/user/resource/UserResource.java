package com.waverchat.api.v1.resources.user.resource;

import com.waverchat.api.v1.exceptions.ConflictException;
import com.waverchat.api.v1.exceptions.ForbiddenException;
import com.waverchat.api.v1.exceptions.NotFoundException;
import com.waverchat.api.v1.exceptions.ValidationException;
import com.waverchat.api.v1.framework.AbstractResource;
import com.waverchat.api.v1.http.response.PageResponse;
import com.waverchat.api.v1.resources.user.dto.request.UserUpdateRQ;
import com.waverchat.api.v1.resources.user.dto.response.UserGetAllRS;
import com.waverchat.api.v1.resources.user.dto.response.UserGetRS;
import com.waverchat.api.v1.resources.user.entity.User;
import com.waverchat.api.v1.resources.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.waverchat.api.v1.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/users")
public class UserResource extends AbstractResource {

    protected final static Logger log = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    public UserService userService;

    @GetMapping
    public ResponseEntity<PageResponse<User, UserGetAllRS>> getAll(
            @RequestParam Map<String, String> queryParams
    ) {
        Page<User> users = this.userService.findAll(queryParams);

        PageResponse<User, UserGetAllRS> responseBody =
                new PageResponse<User, UserGetAllRS>(users, user -> UserGetAllRS.from(user));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetRS> get(@PathVariable Long id) throws NotFoundException {
        User user = this.userService.findById(id);
        UserGetRS responseBody = UserGetRS.from(user);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable Long id, @RequestBody UserUpdateRQ updateRQ, HttpServletRequest request
    )
            throws NotFoundException, ForbiddenException, ValidationException, ConflictException
    {
        Long requestingUserId = this.authContext.getRequestingUserId();

        User user = this.userService.findById(id);

        if(!requestingUserId.equals(id)) {
            throw new ForbiddenException("Not authorized to update user information.");
        }

        User userUpdate = user.cloneFromDiff(updateRQ.toEntity());

        this.userService.update(user, userUpdate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
