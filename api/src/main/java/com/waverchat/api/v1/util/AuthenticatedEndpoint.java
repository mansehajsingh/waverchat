package com.waverchat.api.v1.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AuthenticatedEndpoint {

    private final String ANY = "[^\\/\\s]+";

    private final String DELIMITER = "\\/";

    private String originalEndpoint;
    private String endpoint;
    private String endpointWithTrailingSlash;

    public AuthenticatedEndpoint(String endpoint) {
        this.originalEndpoint = endpoint;
        this.endpoint = this.parseToMatchableEndpoint(endpoint);
        this.endpointWithTrailingSlash = this.endpoint + "\\/";
    }

    private String parseToMatchableEndpoint(String endpoint) {
        endpoint = "^" + endpoint + "$";
        endpoint = endpoint.replace("/", this.DELIMITER);
        endpoint = endpoint.replace("{}", this.ANY);
        return endpoint;
    }

    public boolean matchedByEndpoint(String epToMatch) {
        Pattern ep = Pattern.compile(this.endpoint);
        Pattern epwt = Pattern.compile(this.endpointWithTrailingSlash);
        Matcher epMatcher = ep.matcher(epToMatch);
        Matcher epwtMatcher = epwt.matcher(epToMatch);

        return epMatcher.matches() || epwtMatcher.matches();
    }

}
