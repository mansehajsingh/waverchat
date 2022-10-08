package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.authentication.AuthUtils;
import com.waverchat.api.v1.authentication.session.http.SessionCreationRequest;
import com.waverchat.api.v1.authentication.session.http.SessionCreationResponse;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.http.response.MessageResponse;
import com.waverchat.api.v1.user.User;
import com.waverchat.api.v1.user.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody SessionCreationRequest sessionCreationRequest
    ) {

        User user;

        // fetching the user by the email
        try {
            user = this.userService.getByEmail(sessionCreationRequest.getEmail());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("No user with this email exists."));
        }

        // checking if the password is correct
        if (!(BCrypt.checkpw(sessionCreationRequest.getPassword(), user.getPasswordHash()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid email or password."));
        }

        Session session = new Session(user);
        session = sessionService.createSession(session);

        String accessToken = AuthUtils.issueAccessToken(session.getId(), user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new SessionCreationResponse(accessToken));
    }

}
