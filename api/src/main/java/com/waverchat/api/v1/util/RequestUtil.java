package com.waverchat.api.v1.util;

import com.waverchat.api.v1.authentication.AuthUtils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static Long getRequestingUser(HttpServletRequest request) {
        Object requestingUser = request.getAttribute("requestingUser");

        if (requestingUser == AuthUtils.Enumerables.ANONYMOUS_USER) {
            return null;
        }

        return (Long) requestingUser;
    }
}
