package com.waverchat.api.v1.authentication;


import com.waverchat.api.v1.util.AuthenticatedEndpoint;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String URI = request.getRequestURI();

        for (AuthenticatedEndpoint authEp : AuthenticatedRoutesManager.getInstance().getAuthenticatedEndpoints()) {
            if (authEp.matchedByEndpoint(URI)) {

            }
        }

        return true;
    }



}
