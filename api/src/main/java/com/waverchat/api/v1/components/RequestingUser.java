package com.waverchat.api.v1.components;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestingUser {

    private UUID requestingUser;

    public UUID getId() { return this.requestingUser; }

    public void setId(UUID requestingUser) { this.requestingUser = requestingUser; }

}
