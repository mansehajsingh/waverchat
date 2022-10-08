package com.waverchat.api.v1.authentication;

import com.waverchat.api.v1.util.AuthenticatedEndpoint;

import java.util.ArrayList;
import java.util.List;

public class AuthenticatedRoutesManager {

    private static AuthenticatedRoutesManager instance;

    private ArrayList<AuthenticatedEndpoint> authenticatedEndpoints;

    private AuthenticatedRoutesManager () {
        this.authenticatedEndpoints = new ArrayList<AuthenticatedEndpoint>();

        /* ATTN: Add authenticated endpoints below */

    }

    static protected AuthenticatedRoutesManager getInstance() {
        if (instance != null) return instance;
        instance = new AuthenticatedRoutesManager();
        return instance;
    }

    private void addEndpoint(String endpoint) {
        authenticatedEndpoints.add(new AuthenticatedEndpoint(endpoint));
    }

    protected ArrayList<AuthenticatedEndpoint> getAuthenticatedEndpoints() {
        return this.authenticatedEndpoints;
    }

}
