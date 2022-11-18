package com.waverchat.api.v1.resources.user;

import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.List;

public class UserConstants {

    public static final int MIN_USERNAME_LENGTH = 4;
    public static final int MAX_USERNAME_LENGTH = 20;

    public static final int MIN_FIRST_NAME_LENGTH = 1;
    public static final int MAX_FIRST_NAME_LENGTH = 35;

    public static final int MAX_LAST_NAME_LENGTH = 35;

    public static final int MIN_PASSWORD_LENGTH = 6;

    public static final int MAX_PASSWORD_LENGTH = 500;

    public static final int MAX_PAGE_SIZE = 300;

    public static final String DEFAULT_SORT_FIELD = "createdAt";

    public static final boolean DEFAULT_SORT_IS_ASCENDING = false;

    public static final List<String> SUPPORTED_SORT_TAGS =
            Arrays.asList(new String[] {"email", "username", "firstName", "lastName", "createdAt", "updatedAt"});

    private UserConstants() {}

}
