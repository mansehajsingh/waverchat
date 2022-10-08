package com.waverchat.api.v1.authentication;


import com.waverchat.api.v1.EnvironmentVariables;
import com.waverchat.api.v1.authentication.session.SessionConstants;
import com.waverchat.api.v1.components.RequestingUser;
import com.waverchat.api.v1.util.AuthenticatedEndpoint;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private RequestingUser requestingUser;

    @Autowired
    AuthenticationInterceptor(RequestingUser requestingUser) {
        this.requestingUser = requestingUser;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String URI = request.getRequestURI();

        // checking if uri falls into authenticated route scope
        for (AuthenticatedEndpoint authEp : AuthenticatedRoutesManager.getInstance().getAuthenticatedEndpoints()) {
            if (authEp.matchedByEndpoint(URI)) {
                // fetching signing key
                byte[] decodedKey = Base64.getDecoder()
                        .decode(EnvironmentVariables.get(SessionConstants.TOKEN_SECRET_KEY_ENV));
                Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, SessionConstants.SIGNING_ALGORITHM);

                String jws;

                // checking to see if something like a token exists in the authorization header
                if (request.getHeader("Authorization").length() > 7) {
                    jws = request.getHeader("Authorization").substring(8); // header comes as a string of the form "Bearer ey...."
                } else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }

                Claims claims;

                // if the parsing fails, the token was invalid
                try {
                    claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody();
                } catch (SignatureException e) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }

                // if jwt is expired
                if (claims.getExpiration().before(new Date())) {
                    response.setStatus(440); // http status does not support code 440 https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html
                    return false;
                }

                this.requestingUser.setId(UUID.fromString(claims.getSubject()));
                return true;
            }
        }

        return true;
    }



}
