package com.waverchat.api.v1.framework;

import com.waverchat.api.v1.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractResource {

    protected static Long extractRequestingUserId(HttpServletRequest request) {
        return RequestUtil.getRequestingUser(request);
    }

}
