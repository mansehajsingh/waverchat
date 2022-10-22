package com.waverchat.api.v1.authentication;

import com.waverchat.api.v1.util.AuthenticatedEndpoint;

import java.util.List;

public class AuthenticatedRoutesManager {

    private static AuthenticatedRoutesManager instance;

    private List<AuthenticatedEndpoint> authenticatedEndpoints;

    private AuthenticatedRoutesManager () {
        AuthenticationConfig authConfig = new AuthenticationConfig();
        this.authenticatedEndpoints = authConfig.getAuthenticatedEndpointsFromConfig();
    }

    static protected AuthenticatedRoutesManager getInstance() {
        if (instance != null) return instance;
        instance = new AuthenticatedRoutesManager();
        return instance;
    }

    private void addEndpoint(String method, String endpoint) {
        authenticatedEndpoints.add(new AuthenticatedEndpoint(method, endpoint));
    }

    protected List<AuthenticatedEndpoint> getAuthenticatedEndpoints() {
        return this.authenticatedEndpoints;
    }

}
