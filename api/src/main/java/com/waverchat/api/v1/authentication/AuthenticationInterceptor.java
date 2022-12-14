package com.waverchat.api.v1.authentication;

import com.waverchat.api.v1.util.AuthenticatedEndpoint;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthContext authContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String URI = request.getRequestURI();
        String method = request.getMethod();

        // checking if uri falls into authenticated route scope
        for (AuthenticatedEndpoint authEp : AuthenticatedRoutesManager.getInstance().getAuthenticatedEndpoints()) {
            if (authEp.matchedByEndpoint(URI, method)) {
                String jws;

                // checking to see if something like a token exists in the authorization header
                if (
                        request.getHeader("Authorization") != null &&
                        request.getHeader("Authorization").length() > 7)
                {
                    jws = request.getHeader("Authorization").substring(7); // header comes as a string of the form "Bearer ey...."
                } else {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }

                Claims claims;

                // if the parsing fails, the token was invalid
                try {
                    claims = AuthUtils.getClaimsFromToken(jws);
                } catch (SignatureException | MalformedJwtException | ExpiredJwtException e) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }

                // if jwt is expired
                if (claims.getExpiration().before(new Date())) {
                    response.setStatus(440); // HttpStatus does not support code 440 https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html
                    return false;
                }

                authContext.setRequestingUserId(Long.parseLong(claims.getSubject()));
                return true;
            }
        }

        return true;
    }



}
