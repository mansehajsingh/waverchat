package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.EnvironmentVariables;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
            @Valid @RequestBody SessionCreationRequest sessionCreationRequest,
            HttpServletResponse response
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
        String refreshToken = AuthUtils.issueRefreshToken(session.getId());

        Cookie cookie = new Cookie("waverchat_session", refreshToken);
        cookie.setSecure(Boolean.parseBoolean(EnvironmentVariables.get("useHttps")));
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1/sessions/refresh");
        cookie.setMaxAge(60 * 60 * 24 * 365 * 10); // 10 years

        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SessionCreationResponse(accessToken));
    }

}