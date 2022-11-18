package com.waverchat.api.v1.authentication.session;

import com.waverchat.api.v1.EnvironmentVariablesAccessor;
import com.waverchat.api.v1.EnvironmentVariablesAccessor;
import com.waverchat.api.v1.applicationresource.user.User;
import com.waverchat.api.v1.applicationresource.user.UserService;
import com.waverchat.api.v1.authentication.AuthUtils;
import com.waverchat.api.v1.authentication.session.http.AllSessionsDeletionRequest;
import com.waverchat.api.v1.authentication.session.http.SessionCreationRequest;
import com.waverchat.api.v1.authentication.session.http.SessionResponse;
import com.waverchat.api.v1.exceptions.ResourceNotFoundException;
import com.waverchat.api.v1.http.response.MessageResponse;
import com.waverchat.api.v1.util.RequestUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

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
            user = this.userService.getByEmailIgnoreCase(sessionCreationRequest.getEmail());
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
        String refreshToken = AuthUtils.issueRefreshToken(session.getId(), user.getId());

        Cookie cookie = new Cookie("waverchat_session", refreshToken);
        cookie.setSecure(Boolean.parseBoolean(new EnvironmentVariablesAccessor().get("useHttps")));
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1/sessions/refresh");
        cookie.setMaxAge(60 * 60 * 24 * 365 * 10); // 10 years

        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SessionResponse(accessToken));
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name="waverchat_session", defaultValue = "nosession") String refreshToken) {
        if (refreshToken.equals("nosession")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("No refresh token provided."));
        }

        Claims claims;

        try {
            claims = AuthUtils.getClaimsFromToken(refreshToken);
        } catch (SignatureException | MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid refresh token."));
        }

        // checking if refresh token is expired
        if (claims.getExpiration().before(new Date())) {
            this.sessionService.deleteSessionById(UUID.fromString(claims.getId()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Refresh token expired."));
        }

        // if this fails, the session was probably invalidated in the db
        try {
            this.sessionService.getById(UUID.fromString(claims.getId()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("No session exists with this id."));
        }

        String accessToken = AuthUtils.issueAccessToken(UUID.fromString(claims.getId()), UUID.fromString(claims.getSubject()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new SessionResponse(accessToken));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllSessionsByUser(
            HttpServletRequest request,
            @Valid @RequestBody AllSessionsDeletionRequest deletionRequest
    ) {
        UUID requestingUser = RequestUtil.getRequestingUser(request).get();

        if (!deletionRequest.getUserId().equals(requestingUser.toString())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Not authorized to delete sessions for this user."));
        }

        User user;

        try {
            user = this.userService.getById(requestingUser).get();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No user exists with this id"));
        }

        this.sessionService.deleteSessionsByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("All sessions deleted successfully for the provided user id."));
    }


}
