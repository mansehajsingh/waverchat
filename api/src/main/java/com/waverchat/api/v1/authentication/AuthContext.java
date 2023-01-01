package com.waverchat.api.v1.authentication;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class AuthContext {
    private Long requestingUserId;

    public void setRequestingUserId(Long requestingUserId) {
        this.requestingUserId = requestingUserId;
    }

    public Long getRequestingUserId() {
        return this.requestingUserId;
    }

}
