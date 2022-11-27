package com.waverchat.api.v1.customframework;

import java.util.*;

public class RQRSLifecycleProperties<T> {

    private Map<String, String> queryParams;

    private Map<String, UUID> pathVariableIdentifiers;

    private UUID requestingUserId;

    private Map<String, Object> requestBody;

    private Map<Object, Object> attached;

    public RQRSLifecycleProperties() {
        this.queryParams = new HashMap<>();
        this.pathVariableIdentifiers = new HashMap<>();
        this.requestBody = new HashMap<>();
    }

    public Map<String, String> getQueryParams() {
        return this.queryParams;
    }

    public Map<String, UUID> getPathVariableIds() {
        return this.pathVariableIdentifiers;
    }

    public Map<String, Object> getRequestBody() {
        return this.requestBody;
    }

    public UUID getRequestingUserId() throws NoSuchElementException {
        if (requestingUserId != null) {
            return requestingUserId;
        }
        throw new NoSuchElementException();
    }

    public void setRequestingUserId(Optional<UUID> requestingUserId) {
        if (requestingUserId.isPresent()) {
            this.requestingUserId = requestingUserId.get();
        }
    }

    public void setQueryParameters(Map<String, String> queryParameters) {
        this.queryParams = queryParameters;
    }

    public void setPathVariableIds(Map<String, String> pathVariables) throws IllegalArgumentException {
        for (String key : pathVariables.keySet()) {
            if (key == "id") continue;

            UUID val = UUID.fromString(pathVariables.get(key));
            this.pathVariableIdentifiers.put(key, val);
        }
    }

    public void setRequestBody(Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }

    public boolean hasRequestingUserId() {
        return this.requestingUserId != null;
    }

    public void attach(Object key, Object value) {
        if (this.attached == null)
            this.attached = new HashMap<>();

        this.attached.put(key, value);
    }

    public Object getAttached(Object key) {
        return this.attached.get(key);
    }

}
