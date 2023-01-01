package com.waverchat.api.v1.framework;

import com.waverchat.api.v1.authentication.AuthContext;
import com.waverchat.api.v1.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractResource {

    @Autowired
    protected AuthContext authContext;

    protected static Long extractRequestingUserId(HttpServletRequest request) {
        return RequestUtil.getRequestingUser(request);
    }

}
