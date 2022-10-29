package com.waverchat.api.v1.util;

import com.waverchat.api.v1.authentication.AuthUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

public class RequestUtil {

    public static Optional<UUID> getRequestingUser(HttpServletRequest request) {
        Object requestingUser = request.getAttribute("requestingUser");

        if (requestingUser == AuthUtils.Enumerables.ANONYMOUS_USER) {
            return Optional.ofNullable(null);
        }

        return Optional.ofNullable((UUID) requestingUser);
    }
}
