package com.waverchat.api.v1.util;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class RequestUtil {

    public static UUID getRequestingUser(HttpServletRequest request) {
        return (UUID) request.getAttribute("requestingUser");
    }
}
